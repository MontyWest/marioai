package com.montywest.marioai.agents

import com.montywest.marioai.rules._

import ch.idsia.agents.Agent
import com.montywest.marioai.rules.Observation
import ch.idsia.benchmark.mario.environments.Environment

abstract class MWObservationAgent extends Agent {

  var observation: Observation = Observation.BLANK_OBSERVATION

  override def integrateObservation(environment: Environment) = {
    try {
      observation = Observation(environment);
    } catch {
      case e: Exception => println(e.getStackTrace); throw e
    }
  }

  override def giveIntermediateReward(reward: Float) = {}

  override def reset = {};

  override def setObservationDetails(rfWidth: Int, rfHeight: Int, egoRow: Int, egoCol: Int) = {}

}