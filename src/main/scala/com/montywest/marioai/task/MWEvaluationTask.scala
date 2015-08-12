package com.montywest.marioai.task

import ch.idsia.tools.MarioAIOptions
import ch.idsia.agents.Agent

class MWEvaluationTask(val numberOfLevels: Int, 
                 val evalValues: MWEvaluationMultipliers, 
                 val baseLevelOptions: MWLevelOptions, 
                 val updateOptionsFunc: (Int, MWLevelOptions) => MWLevelOptions, 
                 val visualisation: Boolean = true, 
                 val args: Array[String] = Array.empty)
                 
                   extends MWBasicTask("MWMainPlayTask", {
                     val marioAIOptions = new MarioAIOptions(args)
                     marioAIOptions.setVisualization(visualisation)
                     marioAIOptions
                   }) with EvaluationTask {

  
  override def updateOptions(episode: Int, options: MWLevelOptions): MWLevelOptions = updateOptionsFunc(episode, options)
  
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