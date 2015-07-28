package com.montywest.marioai.rules

import org.scalatest.BeforeAndAfterAll
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers
import org.scalatest.FlatSpec
import ch.idsia.benchmark.mario.environments.Environment
import ch.idsia.benchmark.mario.engine.sprites.Mario
import com.montywest.marioai.fixtures.RulePackageFixtures

class RuleTest extends FlatSpec with Matchers with MockFactory with BeforeAndAfterAll {

  "getCondition" should "return Conditions of length equal to number of perceptions" in {
    assert(Perception.NUMBER_OF_PERCEPTIONS == Rule.BLANK_RULE.getConditions.length)
  }
  
  "getExAction" should "return ExAction of length equal to number of keys" in {
    assert(Environment.numberOfKeys == Rule.BLANK_RULE.getExAction.length)
  }
  
  "getMWAction" should "return MWAction of length equal to MWAction length" in {
    assert(MWAction.LENGTH == Rule.BLANK_RULE.getMWAction.length)
  }
  
  "apply" should "take map of conditions and create rule the gives the correct Conditions instance" in {
    val conditions = Rule(Map(MarioMode -> MarioMode.FIRE, EnemyLowerRight -> EnemyLowerRight.FALSE, MovingY -> MovingY.DOWN), Set.empty).getConditions
    assert(MarioMode.FIRE == conditions(MarioMode.index))
    assert(EnemyLowerRight.FALSE == conditions(EnemyLowerRight.index))
    assert(MovingY.DOWN == conditions(MovingY.index))
    
    assert(conditions.zipWithIndex.forall { case (b: Byte, i: Int) => {
     Set(MarioMode.index, EnemyLowerRight.index, MovingY.index).contains(i) ||
     b == Conditions.DONT_CARE
    }})
  }
  
  "apply" should "validate condition map to test bytes are in perception range" in {
    intercept[Exception] {
      val conditions = Rule(Map(MarioMode -> MarioMode.FIRE, EnemyLowerRight -> EnemyLowerRight.FALSE, MovingY -> (MovingY.limit + 5).toByte), Set.empty).getConditions
    }  
  }
  
  "apply" should "take set of actions and create rule that gives correct the ExActions instance" in {
    val action = Rule(Map.empty, Set(KeyLeft, KeyJump)).getExAction
    
    assert(action(Mario.KEY_LEFT))
    assert(action(Mario.KEY_JUMP))
    assert(!action(Mario.KEY_DOWN))
    assert(!action(Mario.KEY_RIGHT))
    assert(!action(Mario.KEY_UP))
    assert(!action(Mario.KEY_SPEED))
  }
  
    "apply" should "take set of actions and create rule that gives correct the MWActions instance" in {
    val action = Rule(Map.empty, Set(KeyLeft, KeyJump)).getMWAction
    
    assert(action(MWAction.LEFT_INDEX) == MWAction.ACTION_TRUE)
    assert(action(MWAction.JUMP_INDEX) == MWAction.ACTION_TRUE)
    
    assert(action(MWAction.RIGHT_INDEX)== MWAction.ACTION_FALSE)
    assert(action(MWAction.SPEED_INDEX)== MWAction.ACTION_FALSE)

  }
  
  "score" should "return count observations that match conditions" in {
     val rule = Rule(Map(MarioMode -> MarioMode.FIRE, EnemyLowerRight -> EnemyLowerRight.FALSE, MovingY -> MovingY.DOWN), Set.empty)
     
     val observation = RulePackageFixtures.getObservation(
         Map(MarioMode -> MarioMode.FIRE, 
             EnemyLowerRight -> EnemyLowerRight.FALSE, 
             MovingY -> MovingY.DOWN,
             MovingX -> MovingX.RIGHT,
             PitBelow -> PitBelow.TRUE)
     )
     
     assertResult(3: Int) {
       rule.scoreAgainst(observation)
     }
  }
  
  "score" should "return -1 if any unmatching conditions" in {
     val rule = Rule(Map(MarioMode -> MarioMode.FIRE, EnemyLowerRight -> EnemyLowerRight.FALSE, MovingY -> MovingY.DOWN), Set.empty)
     
     val observation = RulePackageFixtures.getObservation(
         Map(MarioMode -> MarioMode.FIRE, 
             EnemyLowerRight -> EnemyLowerRight.FALSE, 
             MovingY -> MovingY.UP,
             MovingX -> MovingX.RIGHT,
             PitBelow -> PitBelow.TRUE)
     )
     
     assertResult(-1: Int) {
       rule.scoreAgainst(observation)
     }
  }

}