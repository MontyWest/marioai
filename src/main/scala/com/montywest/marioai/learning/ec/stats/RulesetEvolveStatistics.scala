package com.montywest.marioai.learning.ec.stats

import ec.Statistics
import ec.EvolutionState
import ec.util.Parameter
import java.io.File
import java.io.IOException
import ec.Individual
import ec.vector.ByteVectorIndividual
import ec.simple.SimpleFitness
import com.montywest.marioai.learning.ec.eval.AgentRulesetEvaluator
import com.montywest.marioai.rules.Ruleset
import com.montywest.marioai.agents.MWRulesetAgent
import ch.idsia.agents.Agent
import com.montywest.marioai.agents.MWRulesetAgentIO

class RulesetEvolveStatistics extends Statistics {

  var genLog: Int = 0
  var finalLog: Int = 0
  var bestAgentFilename: Option[String] = None
  var bestAgentLimit: Int = 0
  var finalAgentFilename: Option[String] = None
  var diffAgentFilename: Option[String] = None
  var diffAgentLimit: Int = 0
  var overallBestIndividual: Option[(Int, ByteVectorIndividual)] = None
  var currentBestIndividual: Option[(Int, ByteVectorIndividual)] = None
  var biggestDiffIndividual: Option[(Int, Double, ByteVectorIndividual)] = None

  private var milliCheckpoint: Long = System.currentTimeMillis();
  
  override def setup(state: EvolutionState, base: Parameter): Unit = {
    super.setup(state, base)
    
    import RulesetEvolveStatistics._
    val genFile: File = state.parameters.getFile(base.push(P_GENFILE), null)
    if (genFile != null) {
      try {
        genLog = state.output.addLog(genFile, true)
      } catch {
        case e: IOException => state.output.fatal("An IOException occurred while making log for file " + genFile + ":\n" + e)
      }
    }
    
    val finalFile: File = state.parameters.getFile(base.push(P_FINALFILE), null)
    if (finalFile != null) {
      try {
        finalLog = state.output.addLog(finalFile, true)
      } catch {
        case e: IOException => state.output.fatal("An IOException occurred while making log for file " + finalFile + ":\n" + e)
      }
    }
    
    finalAgentFilename = {
      val str = state.parameters.getStringWithDefault(base.push(P_FINAL_AGENT_FILE), null, "")
      if (str == "") {
        None
      } else {
        Some(str)
      }
    }
    
    bestAgentFilename = {
      val str = state.parameters.getStringWithDefault(base.push(P_BEST_AGENT_FILE), null, "")
      if (str == "") {
        None
      } else {
        Some(str)
      }
    }
    
    bestAgentLimit = state.parameters.getIntWithDefault(base.push(P_BEST_AGENT_LIMIT), null, 0)
    
    diffAgentFilename = {
      val str = state.parameters.getStringWithDefault(base.push(P_DIFF_AGENT_FILE), null, "")
      if (str == "") {
        None
      } else {
        Some(str)
      }
    }
    
    diffAgentLimit = state.parameters.getIntWithDefault(base.push(P_DIFF_AGENT_LIMIT), null, 0)
    
    
    milliCheckpoint = System.currentTimeMillis()
  }
  
  override def finalStatistics(state: EvolutionState, result: Int): Unit = {
    super.finalStatistics(state, result);
    
    //Write last population to finalLog
    state.population.subpops(0).printSubpopulationForHumans(state, finalLog)
    
    val fallback = state.evaluator.p_problem.asInstanceOf[AgentRulesetEvaluator].fallbackAction
    
    //Write best and last best to agent files
    if (bestAgentFilename.isDefined && overallBestIndividual.isDefined) {
      val bestRuleset: Ruleset = Ruleset.buildFromArray(overallBestIndividual.get._2.genome, fallback)
      val bestAgent: MWRulesetAgent = MWRulesetAgent("best-learnt", bestRuleset)
      MWRulesetAgentIO.toFile(bestAgentFilename.get, bestAgent, true)
      state.output.println("Best Agent Generation: " + overallBestIndividual.get._1.toString, finalLog)
    }
    
    if (diffAgentFilename.isDefined && biggestDiffIndividual.isDefined) {
      val diffRuleset: Ruleset = Ruleset.buildFromArray(biggestDiffIndividual.get._3.genome, fallback)
      val diffAgent: MWRulesetAgent = MWRulesetAgent("diff-learnt", diffRuleset)
      MWRulesetAgentIO.toFile(diffAgentFilename.get, diffAgent, true)
      state.output.println("Diff Agent Generation: " + biggestDiffIndividual.get._1.toString, finalLog)
    }
    
    //Write best and last best to agent files
    if (finalAgentFilename.isDefined && currentBestIndividual.isDefined) {
      val currentRuleset: Ruleset = Ruleset.buildFromArray(currentBestIndividual.get._2.genome, fallback)
      val currentAgent: MWRulesetAgent = MWRulesetAgent("final-learnt", currentRuleset)
      MWRulesetAgentIO.toFile(finalAgentFilename.get, currentAgent, true)
    }
    
  }
  
  override def postEvaluationStatistics(state: EvolutionState): Unit = {
    super.postEvaluationStatistics(state)
    
    val genNum = state.generation
    state.output.println("------------------- GENERATION " + genNum + " -------------------\n", genLog)
    
    val levelSeed: Int = state.evaluator.p_problem match {
      case eare: AgentRulesetEvaluator => eare.taskSeeds.apply(genNum)
    }
    var bestScore: Double = 0.0
    var bestInd: Option[ByteVectorIndividual] = None
    var fitnessSum: Double = 0.0
    
    val indivs = state.population.subpops(0).individuals
    val popSize: Int = indivs.length
    indivs.foreach { (i: Individual) => i match {
      case ind: ByteVectorIndividual => {
        ind.fitness match {
          case f: SimpleFitness => {
            fitnessSum = f.fitness + fitnessSum
            if (f.fitness > bestScore) {
              bestScore = f.fitness
              bestInd = Some(ind)
            }
          }
          case _ => state.output.fatal("This statistics class (RulesetEvolveStatistics) requires individuals with SimpleFitness")
        }
      }
      case _ => state.output.fatal("This statistics class (RulesetEvolveStatistics) requires a ByteVectorIndividual")
    }}
    val delim = ","

    val avScore = fitnessSum / popSize.toDouble
    val agentStr = {
      if (bestInd.isDefined) {
        val genome = bestInd.get.genome
        val sb = StringBuilder.newBuilder
        sb.append(genome(0))
        for(i <- 1 until genome.length) sb.append(delim + genome(i).toString)
        sb.toString()
      } else "none"
    }
    
    val all = "~all~ " + genNum + delim + levelSeed + delim + avScore + delim + bestScore
    state.output.message(all)
    
    val timeNowMillis = System.currentTimeMillis()
    val timeTaken = ((timeNowMillis - milliCheckpoint) / 1000d).toInt
    state.output.message("Time taken: " + timeTaken)
    milliCheckpoint = timeNowMillis
    
    state.output.println(all + 
        "\nLevel Seed    : " + levelSeed + 
        "\nAverage Score : " + avScore + 
        "\nBest Score    : " + bestScore + 
        "\nBest Agent    :-" +
        "\n    " + agentStr + 
        "\n-----------------------------------------------------\n\n", genLog)
        
     if (bestInd.isDefined) {
       currentBestIndividual = Some(genNum, bestInd.get)
       if (genNum >= bestAgentLimit) {
         overallBestIndividual match {
           case Some((_: Int, bvi: ByteVectorIndividual)) => bvi.fitness match {
             case f: SimpleFitness => {
               if (bestScore >= f.fitness()) {
                 overallBestIndividual = Some(genNum, bestInd.get)
               }
             }
             case _ => state.output.fatal("This statistics class (RulesetEvolveStatistics) requires individuals with SimpleFitness")
           }
           case None => overallBestIndividual = Some(genNum, bestInd.get)
         }
       }
       
       if (genNum >= diffAgentLimit) {
         biggestDiffIndividual match {
           case Some((_: Int, diff: Double, bvi: ByteVectorIndividual)) => {
             if ((bestScore - avScore) >= diff) {
               biggestDiffIndividual = Some(genNum, bestScore - avScore, bestInd.get)
             }
           }
           case None => biggestDiffIndividual = Some(genNum, bestScore - avScore, bestInd.get)
         }
       }
     }
  } 
}


object RulesetEvolveStatistics {
  
  val P_GENFILE = "gen-file"
  val P_FINALFILE = "final-file"
  val P_FINAL_AGENT_FILE = "final-agent-file"
  val P_BEST_AGENT_FILE = "best-agent-file"
  val P_BEST_AGENT_LIMIT = "best-agent-limit"
  val P_DIFF_AGENT_FILE = "diff-agent-file"
  val P_DIFF_AGENT_LIMIT = "diff-agent-limit"
}