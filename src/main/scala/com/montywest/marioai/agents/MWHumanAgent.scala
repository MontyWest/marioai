package com.montywest.marioai.agents

import com.montywest.marioai.rules.Observation

import ch.idsia.agents.Agent
import ch.idsia.agents.controllers.human.HumanKeyboardAgent
import ch.idsia.benchmark.mario.environments.Environment

class MWHumanAgent extends HumanKeyboardAgent with Agent {

    var observation: Observation = Observation.BLANK_OBSERVATION
    
    override def integrateObservation(env: Environment) {
      super.integrateObservation(env)
      observation = Observation(env)
   
    }
}