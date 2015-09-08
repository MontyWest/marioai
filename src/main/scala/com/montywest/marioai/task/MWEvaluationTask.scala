package com.montywest.marioai.task

import ch.idsia.tools.MarioAIOptions
import ch.idsia.agents.Agent
import ch.idsia.tools.EvaluationInfo

class MWEvaluationTask(val numberOfLevels: Int, 
                       val evalValues: MWEvaluationMultipliers, 
                       override val baseLevelOptions: MWLevelOptions, 
                       override val updateOptionsFunc: (Int, MWLevelOptions) => MWLevelOptions, 
                       override val visualisation: Boolean, 
                       override val args: Array[String],
                       override val saveLevelScores: Boolean)
                           extends MWBasicTask("MWMainPlayTask", baseLevelOptions, updateOptionsFunc, visualisation, args, saveLevelScores) with EvaluationTask {

  private var baseLevelSeed: Int = 0;
    
  override def nextLevelSeed(episode: Int, lastSeed: Int) =  {
    (3*episode) + lastSeed
  }
  
  override def getFitness: Int = {
    localEvaluationInfo.computeWeightedFitness(evalValues)
  }
  
  override def evaluate: Int = {
    doEpisodes
    getFitness
  }
  
  override def withAgent(agent: Agent): MWEvaluationTask = {
    super.injectAgent(agent, true)
    this
  }
  
  override def withLevelSeed(seed: Int): MWEvaluationTask = {
    baseLevelSeed = seed
    super.injectLevelSeed(seed, true)
    this
  }
  
  override def getLevelSeed: Int = {
    options.getLevelRandSeed
  }
  
  override def getBaseLevelSeed: Int = {
    baseLevelSeed
  }
  
  override def getLevelScore(eval: EvaluationInfo): Int = {
    eval.computeWeightedFitness(evalValues)
  }
}

object MWEvaluationTask {
  
  def apply(numberOfLevels: Int, 
            evalValues: MWEvaluationMultipliers, 
            baseLevelOptions: MWLevelOptions, 
            updateOptionsFunc: (Int, MWLevelOptions) => MWLevelOptions, 
            visualisation: Boolean = true, 
            args: Array[String] = Array.empty,
            saveLevelScores: Boolean = false): MWEvaluationTask = {
    
    new MWEvaluationTask(numberOfLevels, evalValues, baseLevelOptions, updateOptionsFunc, visualisation, args, saveLevelScores)
  }
  
}