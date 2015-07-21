package com.montywest.marioai.agents

import ch.idsia.agents.Agent
import ch.idsia.benchmark.mario.environments.Environment
import ch.idsia.agents.controllers.ForwardJumpingAgent
import ch.idsia.agents.controllers.BasicMarioAIAgent
import com.montywest.marioai.rules.PitAhead
import com.montywest.marioai.rules.PitBelow
import com.montywest.marioai.rules.EnemyLowerRight
import com.montywest.marioai.rules.EnemyUpperRight
import com.montywest.marioai.rules.EnemyLeft
import ch.idsia.benchmark.mario.engine.sprites.Mario
import com.montywest.marioai.rules.ObstacleAhead
import ch.idsia.agents.controllers.human.HumanKeyboardAgent

class TestAgent extends HumanKeyboardAgent with Agent {


    
    override def integrateObservation(env: Environment) {
      super.integrateObservation(env)
      println("Movement- " + env.getMarioMovement.apply(0) + " : " + env.getMarioMovement.apply(1))
    }
}