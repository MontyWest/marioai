package com.montywest.marioai.rules

import org.scalatest.BeforeAndAfterAll
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers
import org.scalatest.FlatSpec
import ch.idsia.benchmark.mario.environments.Environment
import ch.idsia.benchmark.mario.engine.sprites.Mario

class ExActionTest extends FlatSpec with Matchers with MockFactory with BeforeAndAfterAll {

  "LENGTH" should "be equals number of keys in marioengine" in {
    assert(Environment.numberOfKeys == ExAction.LENGTH)
  }
  
  "ExAction indexes" should "match those in marioengine" in {
    assert(Environment.MARIO_KEY_DOWN == ExAction.KEY_DOWN)
    assert(Environment.MARIO_KEY_JUMP == ExAction.KEY_JUMP)
    assert(Environment.MARIO_KEY_RIGHT == ExAction.KEY_RIGHT)
    assert(Mario.KEY_UP == ExAction.KEY_UP)
    assert(Environment.MARIO_KEY_SPEED == ExAction.KEY_SPEED)
    assert(Environment.MARIO_KEY_LEFT == ExAction.KEY_LEFT)
  }
  
  "apply" should "take KeyPress array and return ExAction" in {
    val arr: Array[Boolean] = Array.tabulate(ExAction.LENGTH)(_ match {
      case ExAction.KEY_LEFT => true
      case ExAction.KEY_JUMP => true 
      case _ => false
    }) 
    
    assertResult(arr.deep) {
      ExAction(KeyLeft, KeyJump).deep
    }
  }
  
  "build" should "take KeyPress set and return ExAction" in {
    val arr: Array[Boolean] = Array.tabulate(ExAction.LENGTH)(_ match {
      case ExAction.KEY_LEFT => true
      case ExAction.KEY_JUMP => true 
      case _ => false
    }) 
    
    assertResult(arr.deep) {
      ExAction.build(Set(KeyLeft, KeyJump)).deep
    }
  }
  
  "apply" should "take MWAction and produce ExAction" in {
    val arr: Array[Boolean] = Array.tabulate(ExAction.LENGTH)(_ match {
      case ExAction.KEY_LEFT => true
      case ExAction.KEY_JUMP => true 
      case _ => false
    }) 
    
    val mwa: MWAction = Vector.tabulate(MWAction.LENGTH)(_ match {
      case MWAction.LEFT_INDEX => MWAction.ACTION_TRUE
      case MWAction.JUMP_INDEX => MWAction.ACTION_TRUE
      case _ => MWAction.ACTION_FALSE
    })
    
    assert(arr.deep == ExAction(mwa).deep)
  }
}