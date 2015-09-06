package com.montywest.marioai.task

import java.io.File
import com.montywest.marioai.agents.MWReactiveAgent
import com.montywest.marioai.learning.ec.params.EvaluationParamsUtil
import ec.util.ParameterDatabase
import com.montywest.marioai.agents.MWReactiveAgent
import com.montywest.marioai.agents.MWRulesetAgentIO
import ch.idsia.agents.Agent
import com.montywest.marioai.agents.MWReactiveAgent
import com.montywest.marioai.agents.MWRulesetAgent
import com.montywest.marioai.learning.ec.eval.AgentRulesetEvaluator
import java.io.IOException
import java.io.FileWriter
import com.montywest.marioai.rules.Rule
import com.montywest.marioai.agents.MWHumanAgent

object EvaluationTaskRunner {
  
  val LEVEL_SEED_KEY = "-seed";
  val LEVEL_SEED_DEFAULT = 52;
  
  val LEVEL_SEED_RUNS_KEY = "-seedRuns";
  val LEVEL_SEED_RUNS_DEFAULT = 100;

  val OUTFILE_KEY = "-outFile";
  val OUTFILE_DEFAULT = "eval-task.out";
  
  val NUMBER_OF_LEVELS_KEY = "-nol";
  val NUMBER_OF_LEVELS_DEFAULT = 20;
  
  val AGENT_FILE_KEY = "-agent"
  val AGENT_HUMAN = "human"
  val AGENT_FILE_DEFAULT = "agents/forward-jumping.agent"
  
  val PARAMS_KEY = "-params"
  val PARAMS_FROM_FILE = "fromFile"
  val PARAMS_COMP = "comp"
  val BASE_OPTIONS_DEFAULT = MWLevelOptions.defaultOptions
  val UPDATE_OPTIONS_DEFAULT = MWLevelOptions.noUpdate
  val EVAL_MULTS_DEFAULT = MWEvaluationMultipliers.defaultEvaluationMultipliers
  
  val PARAMS_FILE_KEY = "-paramsFile"
  val PARAMS_FILE_DEFAULT = "params/vischeck.params"

  def run(args: Array[String], vis: Boolean): Unit = {
    var argsDrop = 0;
    
    def getIntArg(key: String): Option[Int] = {
      args.indexOf(key) match {
        case -1 => None
        case x if x == args.length => throw new IllegalArgumentException("Key " + key + "was specied with no value")
        case x => {
          argsDrop = argsDrop + 2
          Some(args(x+1).toInt)
        }
      }
    }
    
    def getStringArg(key: String): Option[String] = {
      args.indexOf(key) match {
        case -1 => None
        case x if x == args.length => throw new IllegalArgumentException("Key " + key + "was specied with no value")
        case x => 
          argsDrop = argsDrop + 2
          Some(args(x+1))
      }
    }
    
    
    
    val levelSeed: Int = getIntArg(LEVEL_SEED_KEY) match {
      case None => {
        println("Using default level seed - ")
        LEVEL_SEED_DEFAULT
      }  
      case Some(x) => x
    }
    println("Level seed: " + levelSeed + "\n");
    
    val outfile: String =  getStringArg(OUTFILE_KEY) match {
      case Some(s) => s
      case None => OUTFILE_DEFAULT;
    }
    var seedRuns: Option[(Int, Int, Int, Int)] = getIntArg(LEVEL_SEED_RUNS_KEY) match {
      case None => None
      case Some(x) => Some(x, levelSeed, 0, 1)
    }
    if (seedRuns.isDefined) println("Seed runs activated - doing " + seedRuns.get._1)  

    
    
    val agent: Agent = getStringArg(AGENT_FILE_KEY) match {
      case Some(AGENT_HUMAN) => new MWHumanAgent();
      case Some(s) => MWRulesetAgentIO.fromFile(s)
      case None => MWRulesetAgentIO.fromFile(AGENT_FILE_DEFAULT)
    }
    println("Agent: " + agent.getName + "\n")
    
    val numberOfLevelsA = getIntArg(NUMBER_OF_LEVELS_KEY) match {
        case None => {
          println("Using default number of levels - ")
          NUMBER_OF_LEVELS_DEFAULT
        }
        case Some(x) => x
      }
    
    val params: (MWLevelOptions, (Int, MWLevelOptions) => MWLevelOptions, MWEvaluationMultipliers, Int) =
      getStringArg(PARAMS_KEY) match {
        case Some(PARAMS_COMP) => {
          (MWLevelOptions.compOptions, MWLevelOptions.compUpdate(levelSeed), MWEvaluationMultipliers.compEvaluationMulipliers, 512)
        }
        case Some(PARAMS_FROM_FILE) => {
          val paramsFile = getStringArg(PARAMS_FILE_KEY) match {
            case None => {
              println("Loading params from default file - ")
              PARAMS_FILE_DEFAULT
            }
            case Some(s) => s
          }
          
          println("Loading params from file: " + paramsFile);
          val pd = new ParameterDatabase(new File(paramsFile).getAbsoluteFile)
          
          val paramLevelBase = EvaluationParamsUtil.getLevelParamsBase(pd)
          if (paramLevelBase.isEmpty)
            throw new IllegalArgumentException("Params file must contain either '"
              + EvaluationParamsUtil.P_EVAL_BASE + "." + EvaluationParamsUtil.P_LEVEL_BASE + " = true' or '"
              + EvaluationParamsUtil.P_LEVEL_BASE + " = true'")
          
          val paramMultsBase = EvaluationParamsUtil.getMultsParamsBase(pd)
          if (paramMultsBase.isEmpty)
            throw new IllegalArgumentException("Params file must contain either '"
              + EvaluationParamsUtil.P_EVAL_BASE + "." + EvaluationParamsUtil.P_MULTS_BASE + " = true' or '"
              + EvaluationParamsUtil.P_MULTS_BASE + " = true'")
          
          val numberOfLevelsB = EvaluationParamsUtil.getNumberOfLevels(pd, paramLevelBase.get) match {
            case None => numberOfLevelsA
            case Some(x) => {
              println("Overriding number of levels from param file - ")
              x
            }
          }

          val seedParamBase = EvaluationParamsUtil.getSeedParamBase(pd);
          if (seedParamBase.isDefined && seedRuns.isDefined) {
            val xSeed = EvaluationParamsUtil.getLevelSeeds(pd, seedParamBase.get, levelSeed, 0, 1)
        		seedRuns = Some((seedRuns.get._1, xSeed._1, xSeed._2, xSeed._3))            
          }

          
          ( EvaluationParamsUtil.getBaseLevelOptions(pd, paramLevelBase.get),
            EvaluationParamsUtil.getUpdateLevelOptionsFunction(pd, paramLevelBase.get, numberOfLevelsB),
            EvaluationParamsUtil.getEvaluationMutlipliers(pd, paramMultsBase.get),
            numberOfLevelsB)
        }
          
        case _ => {
          (BASE_OPTIONS_DEFAULT, UPDATE_OPTIONS_DEFAULT, EVAL_MULTS_DEFAULT, numberOfLevelsA)
        }
      }
    val numberOfLevels: Int = params._4
//    println("Number of levels: " + numberOfLevels + "\n")

    val baseLevelOptions = params._1
//    println(baseLevelOptions.toString)
    
    val updateOptionsFn = params._2
//    var opt = baseLevelOptions.clone
//    for(i <- 0 until numberOfLevels) {
//      opt = updateOptionsFn(i, opt)
//      println("" +i+ " iter " + opt.toString)
//      println
//    }
    
    val evals = params._3
//    println(evals.toString)
    
      
    val task = MWEvaluationTask(numberOfLevels, evals, baseLevelOptions, updateOptionsFn, vis, args.drop(argsDrop))
                .withLevelSeed(levelSeed).withAgent(agent)

    println("Running...")
    if (seedRuns.isDefined) {
      var fitnessSum = 0
      
      var prevSeed = seedRuns.get._2; 
      def memmedTS(g: Int): Int = {
        val ns = prevSeed + seedRuns.get._3 + (g*seedRuns.get._4)
        prevSeed = ns
        ns
      }
      var writerOpt: Option[FileWriter] = None;
      try {
      	writerOpt = Some(new FileWriter(outfile))
  			val writer = writerOpt.get
        
        for (i <- 0 until seedRuns.get._1) {
          
          //RUN
        	val seed = memmedTS(i)
          agent match {case a: MWRulesetAgent => a.resetRuleUsage; case _ =>}
          val fit = task.withLevelSeed(seed).evaluate
          
          fitnessSum = fitnessSum + fit
          val all = "~all~ " + i + "," + seed + "," + fit
          println("Evalled: " + fit + " - LS: " + seed)
          writer.append(all + "\n")
        } 
        val avg = fitnessSum.toDouble/seedRuns.get._1.toDouble
        println("\n\nAverage: " + avg)
        writer.append("\n\nAverage: " + avg)
        
        writer.flush()
      } catch {
        case e: IOException => throw new IllegalArgumentException("File inaccessible or is a folder, or error on flush", e)
      } finally {
        if (writerOpt.isDefined) writerOpt.get.close
      }
      
    } else {
      println
      println("Stats:")
      println
      println("Agent " + agent.getName + "'s ruleset :-")
      agent match {case a: MWRulesetAgent => println(a.ruleset); case _ =>}
      task.evaluate
      println
      agent match {case a: MWRulesetAgent => {
        for (i <- -1 until a.ruleset.length) {
      	  a.ruleset.ruleUsage.get(i) match {
      	  case None =>
      	  case Some(n) => println("Rule " + i + " used " + n + " times.")
      	  }
        }
      }; case _ =>}
      println
      println(task.getStatistics());
    }

  }
  
}