package com.montywest.marioai.agents

import com.montywest.marioai.rules.Action
import com.montywest.marioai.rules.JumpAvailable
import com.montywest.marioai.rules.KeyJump
import com.montywest.marioai.rules.KeyRight
import com.montywest.marioai.rules.KeySpeed
import com.montywest.marioai.rules.OnGround
import com.montywest.marioai.rules.Perception.per2int

import ch.idsia.agents.Agent

class MWForwardJumpingAgent extends MWObservationAgent with Agent {

  var name = "MW Forward Jumping Agent"
  
  //1522958 - 96
  
  override def getAction: Array[Boolean] = {
    if ( observation(JumpAvailable) == 1 || observation(OnGround) == 0 ) {
      Action(KeyRight, KeySpeed, KeyJump)
    } else {
      Action(KeyRight)
    }
  }
  
  override def getName: String = name
  
  override def setName(name: String) = {this.name = name}
}