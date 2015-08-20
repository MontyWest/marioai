package com.montywest.marioai.agents

import com.montywest.marioai.rules.KeyLeft
import com.montywest.marioai.rules.PitAhead
import com.montywest.marioai.rules.PitBelow
import com.montywest.marioai.rules.KeyRight
import com.montywest.marioai.rules.EnemyUpperRight
import com.montywest.marioai.rules.Ruleset
import com.montywest.marioai.rules.JumpAvailable
import com.montywest.marioai.rules.ObstacleAhead
import com.montywest.marioai.rules.MarioMode
import com.montywest.marioai.rules.EnemyLowerRight
import com.montywest.marioai.rules.OnGround
import com.montywest.marioai.rules.KeySpeed
import com.montywest.marioai.rules.KeyJump
import com.montywest.marioai.rules.Rule
import com.montywest.marioai.rules.MovingX
import com.montywest.marioai.rules.MovingY

object AgentWriter extends App {

  val ruleset: Ruleset =
    Ruleset(
      Seq(
        Rule.build(Map(PitBelow -> PitBelow.TRUE), Set(KeyJump, KeyRight, KeySpeed)),

        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.TRUE, ObstacleAhead -> ObstacleAhead.TRUE, JumpAvailable -> JumpAvailable.TRUE, OnGround -> OnGround.TRUE), 
            Set(KeyJump, KeyRight)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.TRUE, ObstacleAhead -> ObstacleAhead.TRUE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.FALSE), 
            Set(KeyRight)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.TRUE, ObstacleAhead -> ObstacleAhead.FALSE, JumpAvailable -> JumpAvailable.TRUE, OnGround -> OnGround.TRUE), 
            Set(KeyRight, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.TRUE, ObstacleAhead -> ObstacleAhead.FALSE, JumpAvailable -> JumpAvailable.TRUE, OnGround -> OnGround.TRUE, MovingX -> MovingX.RIGHT), 
            Set(KeyRight, KeySpeed, KeyJump)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.TRUE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), 
            Set(KeyRight, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.TRUE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.FALSE, MovingX -> MovingX.RIGHT, MovingY -> MovingY.STILL), 
            Set(KeyLeft, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.TRUE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.FALSE, MovingX -> MovingX.RIGHT, MovingY -> MovingY.DOWN), 
            Set(KeyLeft, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.TRUE, OnGround -> OnGround.FALSE), 
            Set(KeyRight, KeySpeed, KeyJump)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.FALSE, EnemyLowerRight -> EnemyLowerRight.TRUE, MarioMode -> MarioMode.SMALL, EnemyUpperRight -> EnemyUpperRight.TRUE, MovingX -> MovingX.RIGHT), 
            Set(KeyLeft, KeyJump)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.FALSE, EnemyLowerRight -> EnemyLowerRight.TRUE, MarioMode -> MarioMode.SMALL, EnemyUpperRight -> EnemyUpperRight.TRUE, MovingX -> MovingX.STILL), 
            Set(KeyRight, KeySpeed)),         
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.FALSE, EnemyLowerRight -> EnemyLowerRight.TRUE, OnGround -> OnGround.FALSE, MovingX -> MovingX.RIGHT, MovingY -> MovingY.DOWN), 
            Set(KeyLeft, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.FALSE, EnemyLowerRight -> EnemyLowerRight.TRUE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE),
            Set(KeyRight)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.FALSE, EnemyLowerRight -> EnemyLowerRight.TRUE),
            Set(KeyJump, KeyRight)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.FALSE, ObstacleAhead -> ObstacleAhead.TRUE, EnemyUpperRight -> EnemyUpperRight.TRUE), 
            Set(KeyJump, KeyLeft)),         
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.FALSE, ObstacleAhead -> ObstacleAhead.TRUE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE),
            Set(KeyRight)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.FALSE, ObstacleAhead -> ObstacleAhead.TRUE),
            Set(KeyRight, KeyJump, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.FALSE, PitAhead -> PitAhead.FALSE, ObstacleAhead -> ObstacleAhead.TRUE, MovingX -> MovingX.RIGHT),
            Set(KeyJump, KeyRight)),
            
        Rule.build(Map(OnGround -> OnGround.TRUE), Set(KeyRight, KeySpeed))),
      Set(KeyRight))

  MWRulesetFileAgent.toFile(args(0), new MWRulesetAgent(args(0), ruleset), true)

}