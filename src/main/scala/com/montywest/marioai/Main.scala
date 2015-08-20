package com.montywest.marioai

import com.montywest.marioai.task.EvaluationTaskRunner
import ec.Evolve
import com.montywest.marioai.agents.MWRulesetFileAgent
import com.montywest.marioai.agents.MWReactiveRulesetAgent

object Main extends App {

  val MODE_KEY = "-mode"
  val LEARN = "learn"
  val WATCH = "watch"
  val EVAL = "eval"
  
  if (args.length < 2 || 
      args(0) != MODE_KEY ||
      (args(1) != LEARN && args(1) != WATCH && args(1) != EVAL)) {
    throw new IllegalArgumentException("First two arguments must be '" + MODE_KEY + " {learn|play|eval}'.")
  }
    
  args(1) match {
    case LEARN => {
      println("Running learning...")
      Evolve.main(args.drop(2))
    }
    case WATCH => {
      println("Running watch...")
      EvaluationTaskRunner.run(args.drop(2), true)
    }
    case EVAL => {
      println("Running evaluation...")
      EvaluationTaskRunner.run(args.drop(2), false)
    }
  }
}