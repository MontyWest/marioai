package com.montywest.marioai.rules

import org.scalatest.BeforeAndAfterAll
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers
import org.scalatest.FlatSpec

class MWActionTest extends FlatSpec with Matchers with MockFactory with BeforeAndAfterAll {

  "getKeyPressIndex" should "return correct index for KeyPress" in {
    assert(MWAction.LEFT_INDEX == MWAction.getKeyPressIndex(KeyLeft))
    assert(MWAction.RIGHT_INDEX == MWAction.getKeyPressIndex(KeyRight))
    assert(MWAction.JUMP_INDEX == MWAction.getKeyPressIndex(KeyJump))
    assert(MWAction.SPEED_INDEX == MWAction.getKeyPressIndex(KeySpeed))
  }
  
  "apply" should "take KeyPress array and return MWAction" in {
    val vec: Vector[Byte] = Vector.tabulate(MWAction.LENGTH)(_ match {
      case MWAction.LEFT_INDEX => MWAction.ACTION_TRUE
      case MWAction.JUMP_INDEX => MWAction.ACTION_TRUE
      case _ => MWAction.ACTION_FALSE
    }) 
    
    assertResult(vec) {
      MWAction(KeyLeft, KeyJump)
    }
  }
  
  "build" should "take KeyPress set and return MWAction" in {
    val vec: Vector[Byte] = Vector.tabulate(MWAction.LENGTH)(_ match {
      case MWAction.RIGHT_INDEX => MWAction.ACTION_TRUE
      case MWAction.SPEED_INDEX => MWAction.ACTION_TRUE
      case _ => MWAction.ACTION_FALSE
    }) 
    
    assertResult(vec) {
      MWAction.build(Set(KeyRight, KeySpeed))
    }
  }
}