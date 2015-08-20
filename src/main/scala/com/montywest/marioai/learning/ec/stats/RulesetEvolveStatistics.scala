package com.montywest.marioai.learning.ec.stats

import ec.Statistics
import ec.EvolutionState
import ec.util.Parameter
import java.io.File
import java.io.IOException
import ec.Individual
import ec.vector.ByteVectorIndividual
import ec.simple.SimpleFitness
import com.montywest.marioai.learning.ec.eval.EvolvedAgentRulesetEvaluator

class RulesetEvolveStatistics extends Statistics {

  var genLog: Int = 0
  var finalLog: Int = 0
  var agentFilename: Option[String] = None
  
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
    
    agentFilename = {
      val str = state.parameters.getStringWithDefault(base.push(P_FINAL_AGENT_FILE), null, "")
      if (str == "") {
        None
      } else {
        Some(str)
      }
    }
    
  }
  
  override def postEvaluationStatistics(state: EvolutionState): Unit = {
    super.postEvaluationStatistics(state)
    
    val genNum = state.generation
    state.output.println("------------------- GENERATION " + genNum + " -------------------\n", genLog)
    
    val levelSeed: Int = state.evaluator.p_problem match {
      case eare: EvolvedAgentRulesetEvaluator => eare.taskSeeds.apply(genNum)
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
    state.output.println(all + 
        "\nLevel Seed    : " + levelSeed + 
        "\nAverage Score : " + avScore + 
        "\nBest Score    : " + bestScore + 
        "\nBest Agent    :-" +
        "\n    " + agentStr + 
        "\n-----------------------------------------------------\n\n", genLog)
  } 
  
  
}


object RulesetEvolveStatistics {
  
  val P_GENFILE = "gen-file"
  val P_FINALFILE = "final-file"
  val P_FINAL_AGENT_FILE = "final-agent-file"
}