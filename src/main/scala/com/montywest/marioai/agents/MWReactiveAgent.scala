package com.montywest.marioai.agents

import ch.idsia.agents.Agent
import com.montywest.marioai.rules.JumpAvailable
import com.montywest.marioai.rules.OnGround
import ch.idsia.benchmark.mario.engine.sprites.Mario
import ch.idsia.benchmark.mario.environments.Environment
import com.montywest.marioai.rules.EnemyUpperRight
import com.montywest.marioai.rules.PitBelow
import com.montywest.marioai.rules.PitAhead
import com.montywest.marioai.rules.EnemyLowerRight
import com.montywest.marioai.rules.ObstacleAhead
import com.montywest.marioai.rules.MarioMode
import com.montywest.marioai.rules.MovingX

class MWReactiveAgent extends MWObservationAgent with Agent {

  var name = "MW Reactive Agent"
  
  override def getAction: Array[Boolean] = {
    
    val jumpBlock: Boolean = (observation(MarioMode) == MarioMode.SMALL) &&
                             (observation(EnemyUpperRight) == EnemyUpperRight.TRUE)
                             
    val jumpNeeded: Boolean = (observation(EnemyLowerRight) == EnemyLowerRight.TRUE)||
                              (observation(ObstacleAhead) == ObstacleAhead.TRUE) || 
                              (observation(PitAhead) == PitAhead.TRUE) || 
                              (observation(PitBelow) == PitAhead.TRUE)
    
    val jumpPossible: Boolean = (observation(JumpAvailable) == JumpAvailable.TRUE) ||
                                (observation(OnGround) == OnGround.FALSE)
                           
                                
    if (jumpNeeded && !jumpBlock) {
      if (jumpPossible) {
         action(Mario.KEY_JUMP) = true
         action(Mario.KEY_RIGHT) = true
         action(Mario.KEY_LEFT) = false
         action(Mario.KEY_SPEED) = true;
      } else {
         action(Mario.KEY_JUMP) = false
         action(Mario.KEY_RIGHT) = false
         action(Mario.KEY_LEFT) = true
         action(Mario.KEY_SPEED) = false
      }
    } else if (jumpNeeded) {
      action(Mario.KEY_RIGHT) = false
      action(Mario.KEY_LEFT) = true
      action(Mario.KEY_SPEED) = false
      action(Mario.KEY_JUMP) = false
    } else {  
    	action(Mario.KEY_JUMP) = false
      action(Mario.KEY_RIGHT) = true
      action(Mario.KEY_LEFT) = false
      action(Mario.KEY_SPEED) = true;
    }

    
    action
  }
  
  override def reset = {
    action = Array.fill(Environment.numberOfKeys)(false)
    action(Mario.KEY_RIGHT) = true;
    action(Mario.KEY_SPEED) = true;
  }
  
  override def getName: String = name
  
  override def setName(name: String) = {this.name = name}
}