package com.montywest.marioai.task

import com.montywest.marioai.agents.MWReactiveRulesetAgent
import com.montywest.marioai.agents.MWReactiveAgent
import ch.idsia.agents.controllers.ForwardJumpingAgent

object EvaluationTaskRunner extends App {

  val levelSeed = 52;
  
  val numberOfLevels = 20;
  val baseLevelOptions = MWLevelOptions.defaultOptions
  val evals = MWEvaluationMultipliers.defaultEvaluationMultipliers
  val updateOptionsFunc: (Int, MWLevelOptions) => MWLevelOptions = (i: Int, options: MWLevelOptions) => {
    options.withLevelType(i%2).withLevelDifficulty(i/5)
  }
  
  //MWReactiveRulesetAgent - 65456 - 2 wins
  
  val task = new MWEvaluationTask(numberOfLevels, evals, baseLevelOptions, updateOptionsFunc, true)
                                .withAgent(new MWReactiveRulesetAgent())
                                .withLevelSeed(levelSeed)
                                
  task.evaluate
  println(task.getStatistics());
}