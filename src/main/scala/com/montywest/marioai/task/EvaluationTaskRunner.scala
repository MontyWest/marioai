package com.montywest.marioai.task

import java.io.File
import com.montywest.marioai.agents.MWReactiveAgent
import com.montywest.marioai.learning.ec.params.EvaluationParamsUtil
import ec.util.ParameterDatabase
import com.montywest.marioai.agents.MWReactiveAgent
import com.montywest.marioai.agents.MWRulesetFileAgent
import ch.idsia.agents.Agent
import com.montywest.marioai.agents.MWReactiveAgent

object EvaluationTaskRunner {
  
  val LEVEL_SEED_KEY = "-seed";
  val LEVEL_SEED_DEFAULT = 52;

  val NUMBER_OF_LEVELS_KEY = "-nol";
  val NUMBER_OF_LEVELS_DEFAULT = 20;
  
  val AGENT_FILE_KEY = "-agent"
  val AGENT_FILE_DEFAULT = "agents/forward-jumping.agent"
  
  val PARAMS_KEY = "-params"
  val PARAMS_FROM_FILE = "fromFile"
  val PARAMS_COMP = "comp"
  val BASE_OPTIONS_DEFAULT = MWLevelOptions.defaultOptions
  val UPDATE_OPTIONS_DEFAULT = MWLevelOptions.noUpdate
  val EVAL_MULTS_DEFAULT = MWEvaluationMultipliers.defaultEvaluationMultipliers
  
  val PARAMS_FILE_KEY = "-paramsFile"
  val PARAMS_FILE_DEFAULT = "params/ruleset-learn.params"

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
      
    
    val agent: Agent = 
      MWRulesetFileAgent.fromFile(getStringArg(AGENT_FILE_KEY) match {
        case None => {
          println("Using default agent - ")
          AGENT_FILE_DEFAULT
        }
        case Some(s) => s
      })
    println("Agent: " + agent.getName + "\n")
    
    val numberOfLevelsA = getIntArg(LEVEL_SEED_KEY) match {
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
    println("Number of levels: " + numberOfLevels + "\n")

    val baseLevelOptions = params._1
    println(baseLevelOptions.toString)
    
    val updateOptionsFn = params._2
//    var opt = baseLevelOptions
//    for(i <- 0 until numberOfLevels) {
//      opt = updateOptionsFn(i, opt)
//      println("" +i+ " iter " + opt.toString)
//      println
//    }
    
    val evals = params._3
    println(evals.toString)
    
      

    
    val task = MWEvaluationTask(numberOfLevels, evals, baseLevelOptions, updateOptionsFn, vis, args.drop(argsDrop))
                                  .withAgent(agent)
                                  .withLevelSeed(levelSeed)
                                  
    task.evaluate
    println(task.getStatistics());
  }
  
}