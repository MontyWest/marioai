package com.montywest.marioai.agents

import com.montywest.marioai.rules.Action
import com.montywest.marioai.rules.EnemyLowerRight
import com.montywest.marioai.rules.EnemyUpperRight
import com.montywest.marioai.rules.JumpAvailable
import com.montywest.marioai.rules.KeyJump
import com.montywest.marioai.rules.KeyLeft
import com.montywest.marioai.rules.KeyRight
import com.montywest.marioai.rules.KeySpeed
import com.montywest.marioai.rules.MarioMode
import com.montywest.marioai.rules.ObstacleAhead
import com.montywest.marioai.rules.OnGround
import com.montywest.marioai.rules.Perception.per2int
import com.montywest.marioai.rules.PitAhead
import com.montywest.marioai.rules.PitBelow

import ch.idsia.agents.Agent

class MWReactiveAgent extends MWObservationAgent with Agent {

  var name = "MW Reactive Agent"

  override def getAction: Array[Boolean] = {
       
    val jumpSafe: Boolean = (observation(EnemyUpperRight) == EnemyUpperRight.FALSE) ||
                            (observation(MarioMode) != MarioMode.SMALL)
                             
    val jumpNeeded: Boolean = (observation(EnemyLowerRight) == EnemyLowerRight.TRUE) ||
                              (observation(ObstacleAhead) == ObstacleAhead.TRUE) || 
                              (observation(PitAhead) == PitAhead.TRUE) || 
                              (observation(PitBelow) == PitAhead.TRUE)
    
    val jumpPossible: Boolean = (observation(JumpAvailable) == JumpAvailable.TRUE)
                                
    val jumpResetNeeded: Boolean = (observation(JumpAvailable) == JumpAvailable.FALSE) &&
                                   (observation(OnGround) == OnGround.TRUE) 
                                   
    val onGround: Boolean = (observation(OnGround) == OnGround.TRUE)
                           
     //1522622 - 96
                                   
    if (jumpNeeded && jumpSafe) {
      if (jumpPossible) {
         Action(KeyJump, KeyRight, KeySpeed)
      } else if (jumpResetNeeded) {
         Action(KeyLeft)
         
      } else {
         Action(KeyJump, KeyRight, KeySpeed)
      }
    } else if (jumpNeeded) {
      Action(KeyLeft)
    } else {
      if (onGround) {
        Action(KeyRight, KeySpeed)
      } else {
        Action(KeyJump, KeyRight, KeySpeed)
      }
    }
  }
  
  override def reset = {

  }
  
  override def getName: String = name
  
  override def setName(name: String) = {this.name = name}
}

  
