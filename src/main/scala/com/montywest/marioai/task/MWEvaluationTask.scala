package com.montywest.marioai.task

import ch.idsia.tools.MarioAIOptions
import ch.idsia.agents.Agent

class MWEvaluationTask(val numberOfLevels: Int, 
                 val evalValues: MWEvaluationMultipliers, 
                 val baseLevelOptions: MWLevelOptions, 
                 val updateOptionsFunc: (Int, MWLevelOptions) => MWLevelOptions, 
                 val visualisation: Boolean, 
                 val args: Array[String])
                 
                   extends MWBasicTask("MWMainPlayTask", {
                     val marioAIOptions = new MarioAIOptions(args)
                     marioAIOptions.setVisualization(visualisation)
                     marioAIOptions
                   }) with EvaluationTask {

  
  override def updateOptions(episode: Int, options: MWLevelOptions): MWLevelOptions = updateOptionsFunc(episode, options)
  
  override def updateMarioAIOptions(episode: Int, options: MarioAIOptions): MarioAIOptions = {
    options.setLevelRandSeed(options.getLevelRandSeed + episode)
    options
  }
  
  override def getFitness: Int = {
    localEvaluationInfo.computeWeightedFitness(evalValues)
  }
  
  override def evaluate: Int = {
    doEpisodes
    getFitness
  }
  
  override def withAgent(agent: Agent): MWEvaluationTask = {
    super.injectAgent(agent)
    this
  }
  
  override def withLevelSeed(seed: Int): MWEvaluationTask = {
    super.injectLevelSeed(seed)
    this
  }
}

object MWEvaluationTask {
  
  def apply(numberOfLevels: Int, 
            evalValues: MWEvaluationMultipliers, 
            baseLevelOptions: MWLevelOptions, 
            updateOptionsFunc: (Int, MWLevelOptions) => MWLevelOptions, 
            visualisation: Boolean = true, 
            args: Array[String] = Array.empty): MWEvaluationTask = {
    
    new MWEvaluationTask(numberOfLevels, evalValues, baseLevelOptions, updateOptionsFunc, visualisation, args)
  }
  
}