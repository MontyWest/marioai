package com.montywest.marioai.agents

import com.montywest.marioai.rules.Ruleset
import ch.idsia.agents.Agent
import com.montywest.marioai.rules.KeyJump
import com.montywest.marioai.rules.KeySpeed
import com.montywest.marioai.rules.KeyRight
import com.montywest.marioai.rules.JumpAvailable
import com.montywest.marioai.rules.Rule
import com.montywest.marioai.rules.EnemyLowerRight
import com.montywest.marioai.rules.EnemyUpperRight
import com.montywest.marioai.rules.MarioMode
import com.montywest.marioai.rules.OnGround
import com.montywest.marioai.rules.KeyLeft
import com.montywest.marioai.rules.ObstacleAhead
import com.montywest.marioai.rules.PitAhead
import com.montywest.marioai.rules.PitBelow
import com.montywest.marioai.rules.MovingX

class MWReactiveRulesetAgent extends MWRulesetAgent(MWReactiveRulesetAgent.reactiveRuleset) with Agent {

  var name = "MW Reactive Ruleset Agent";
  
  def getName = name
  
  def setName(name: String) = this.name = name

  println(MWReactiveRulesetAgent.reactiveRuleset.getVectorRep(true).mkString(" "))
}


object MWReactiveRulesetAgent {
  
  //1521300 - 97
  
  val reactiveRuleset: Ruleset = 
    Ruleset(
      Seq(
          
//        Rule(Map(OnGround -> OnGround.TRUE, JumpAvailable -> JumpAvailable.FALSE), Set(KeyRight, KeySpeed)),
//        Rule(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
//        Rule(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed))
//
//        
        Rule.build(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, MarioMode -> MarioMode.LARGE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, MarioMode -> MarioMode.LARGE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, MarioMode -> MarioMode.LARGE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, MarioMode -> MarioMode.FIRE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, MarioMode -> MarioMode.FIRE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(EnemyLowerRight -> EnemyLowerRight.TRUE, MarioMode -> MarioMode.FIRE), Set(KeyJump, KeyRight, KeySpeed)),
        
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE, MarioMode -> MarioMode.LARGE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE, MarioMode -> MarioMode.LARGE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE, MarioMode -> MarioMode.LARGE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE, MarioMode -> MarioMode.FIRE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE, MarioMode -> MarioMode.FIRE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE, MarioMode -> MarioMode.FIRE), Set(KeyJump, KeyRight, KeySpeed)),

        Rule.build(Map(PitAhead -> PitAhead.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitAhead -> PitAhead.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(PitAhead -> PitAhead.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitAhead -> PitAhead.TRUE, MarioMode -> MarioMode.LARGE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitAhead -> PitAhead.TRUE, MarioMode -> MarioMode.LARGE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(PitAhead -> PitAhead.TRUE, MarioMode -> MarioMode.LARGE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitAhead -> PitAhead.TRUE, MarioMode -> MarioMode.FIRE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitAhead -> PitAhead.TRUE, MarioMode -> MarioMode.FIRE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(PitAhead -> PitAhead.TRUE, MarioMode -> MarioMode.FIRE), Set(KeyJump, KeyRight, KeySpeed)),
        
        Rule.build(Map(PitBelow -> PitBelow.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(PitBelow -> PitBelow.TRUE, EnemyUpperRight -> EnemyUpperRight.FALSE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.TRUE, MarioMode -> MarioMode.LARGE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.TRUE, MarioMode -> MarioMode.LARGE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(PitBelow -> PitBelow.TRUE, MarioMode -> MarioMode.LARGE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.TRUE, MarioMode -> MarioMode.FIRE, JumpAvailable -> JumpAvailable.TRUE), Set(KeyJump, KeyRight, KeySpeed)),
        Rule.build(Map(PitBelow -> PitBelow.TRUE, MarioMode -> MarioMode.FIRE, JumpAvailable -> JumpAvailable.FALSE, OnGround -> OnGround.TRUE), Set(KeyLeft)),
        Rule.build(Map(PitBelow -> PitBelow.TRUE, MarioMode -> MarioMode.FIRE), Set(KeyJump, KeyRight, KeySpeed)),
        
        Rule.build(Map(EnemyLowerRight -> EnemyLowerRight.TRUE), Set(KeyLeft)),
        Rule.build(Map(ObstacleAhead -> ObstacleAhead.TRUE), Set(KeyLeft)),
        Rule.build(Map(PitAhead -> PitAhead.TRUE), Set(KeyLeft)),
        Rule.build(Map(PitBelow -> PitBelow.TRUE), Set(KeyLeft)),
        
        Rule.build(Map(OnGround -> OnGround.TRUE), Set(KeyRight, KeySpeed))
      ),
      Set(KeyRight, KeyJump, KeySpeed)
    )
  
}