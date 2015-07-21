package com.montywest.marioai.agents

import com.montywest.marioai.rules._

import ch.idsia.agents.Agent
import ch.idsia.benchmark.mario.engine.sprites.Mario
import ch.idsia.benchmark.mario.environments.Environment

class MWForwardJumpingAgent extends MWObservationAgent with Agent {

  var name = "MW Forward Jumping Agent"
  
  override def getAction: Array[Boolean] = {
    action(Mario.KEY_RIGHT) = true;
    val bool = observation(JumpAvailable) == 1 || observation(OnGround) == 0
    action(Mario.KEY_SPEED) = bool;
    action(Mario.KEY_JUMP) = bool
        
    action
  }
  
  override def getName: String = name
  
  override def setName(name: String) = {this.name = name}
}