package com.montywest.marioai.rules

import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ConditionsTest extends FlatSpec with Matchers with MockFactory with BeforeAndAfterAll {

  "validateCondition" should "should return true if condition is within perception range" in {
    
    assert(Conditions.validateCondition((MarioMode, MarioMode.LARGE)))
    assert(Conditions.validateCondition((OnGround, 0: Byte)))
    assert(Conditions.validateCondition((EnemyLeft, 1: Byte)))
    assert(Conditions.validateCondition((MovingX, MovingX.RIGHT)))
    
  }
  
  
  "validateCondition" should "should return false if condition is outside perception range" in {
    
    assert(!Conditions.validateCondition((EnemyUpperRight, -2: Byte)))
    assert(!Conditions.validateCondition((MovingY, (MovingY.limit + 5).toByte)))
    
  }
  
  "apply" should "turn a map in Conditions (with DONT_CARE as default)" in {
    
    val vec: Vector[Byte] = Vector.tabulate(Conditions.LENGTH)( _ match {
      case MarioMode() => 2
      case OnGround() => 1
      case ObstacleAhead() => 1
      case MovingX() => 2
      case _ => Conditions.DONT_CARE
    })
    
    assertResult(vec) {
      Conditions(Map(MarioMode -> 2,
                   OnGround -> 1,
                   ObstacleAhead -> 1,
                   MovingX -> 2)) 
    }
    
  }
  
  "apply" should "throw exception if passed a perception out of range" in {
    intercept[Exception] {
      Conditions(Map(MarioMode -> 2,
                   OnGround -> 1,
                   ObstacleAhead -> 1,
                   MovingX -> (MovingX.limit + 5).toByte)) 
    }
  }
}