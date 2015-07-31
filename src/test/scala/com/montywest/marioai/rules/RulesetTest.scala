package com.montywest.marioai.rules

import org.scalatest.BeforeAndAfterAll
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers
import org.scalatest.FlatSpec

class RulesetTest extends FlatSpec with Matchers with MockFactory with BeforeAndAfterAll {

  "apply" should "take (a sequence of rules and) a default action as be able construct ruleset with correct MWAction" in {
    val defaultAction: MWAction = Ruleset(Seq(Rule.BLANK_RULE), Set(KeyLeft, KeyJump)).defaultAction
    
    val expected: Vector[Byte] = Vector.tabulate(4)( _ match {
      case MWAction.LEFT_INDEX => MWAction.ACTION_TRUE
      case MWAction.JUMP_INDEX => MWAction.ACTION_TRUE
      case _ => MWAction.ACTION_FALSE
    })
    
    assert(expected == defaultAction);
  }
  
  "build" should "take a vector of bytes and produce ruleset (with default action)" in {
    val vec = Vector[Byte](-1, 1, 1, 1, 0,-1,-1,-1, 1, 0, 2  ,   1, 0, 1, 0,
                            1,-1, 0, 0, 1, 1,-1, 1,-1,-1,-1  ,   0, 0, 1, 0,
                           -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1  ,   0, 1, 0, 0)
                     
    val ruleset = Ruleset.build(vec)

    
    assert(Rule(Vector[Byte](-1, 1, 1, 1, 0,-1,-1,-1, 1, 0, 2), Vector(1, 0, 1, 0)) == ruleset.rules(0))
    assert(Rule(Vector[Byte]( 1,-1, 0, 0, 1, 1,-1, 1,-1,-1,-1), Vector(0, 0, 1, 0)) == ruleset.rules(1))
    assert(Vector(0, 1, 0, 0) == ruleset.defaultAction)
    
  }
  
  "build" should "take a vector of bytes and produce ruleset (with specified default action)" in {
    val vec = Vector[Byte](-1, 1, 1, 1, 0,-1,-1,-1, 1, 0, 2  ,   1, 0, 1, 0,
                            1,-1, 0, 0, 1, 1,-1, 1,-1,-1,-1  ,   0, 0, 1, 0,
                            2,-1,-1,-1, 1,-1,-1,-1, 0,-1,-1  ,   0, 1, 0, 0)
                     
    val ruleset = Ruleset.build(vec, Vector(0, 1, 1, 0))

    
    assert(Rule(Vector[Byte](-1, 1, 1, 1, 0,-1,-1,-1, 1, 0, 2), Vector(1, 0, 1, 0)) == ruleset.rules(0))
    assert(Rule(Vector[Byte]( 1,-1, 0, 0, 1, 1,-1, 1,-1,-1,-1), Vector(0, 0, 1, 0)) == ruleset.rules(1))
    assert(Rule(Vector[Byte]( 2,-1,-1,-1, 1,-1,-1,-1, 0,-1,-1), Vector(0, 1, 0, 0)) == ruleset.rules(2))
    assert(Vector(0, 1, 1, 0) == ruleset.defaultAction)
    
  }
  
  "build" should "take a vector of bytes and produce ruleset (without any default action)" in {
    val vec = Vector[Byte](-1, 1, 1, 1, 0,-1,-1,-1, 1, 0, 2  ,   1, 0, 1, 0,
                            1,-1, 0, 0, 1, 1,-1, 1,-1,-1,-1  ,   0, 0, 1, 0,
                            2,-1,-1,-1, 1,-1,-1,-1, 0,-1,-1  ,   0, 1, 0, 0)
                     
    val ruleset = Ruleset.build(vec)

    
    assert(Rule(Vector[Byte](-1, 1, 1, 1, 0,-1,-1,-1, 1, 0, 2), Vector(1, 0, 1, 0)) == ruleset.rules(0))
    assert(Rule(Vector[Byte]( 1,-1, 0, 0, 1, 1,-1, 1,-1,-1,-1), Vector(0, 0, 1, 0)) == ruleset.rules(1))
    assert(Rule(Vector[Byte]( 2,-1,-1,-1, 1,-1,-1,-1, 0,-1,-1), Vector(0, 1, 0, 0)) == ruleset.rules(2))
    assert(Ruleset.FALLBACK_ACTION == ruleset.defaultAction)
    
  }
  
  "build (with default) then getVectorRep (with default)" should "be opposite operations" in {
    val vec = Vector[Byte](-1, 1, 1, 1, 0,-1,-1,-1, 1, 0, 2  ,   1, 0, 1, 0,
                            1,-1, 0, 0, 1, 1,-1, 1,-1,-1,-1  ,   0, 0, 1, 0,
                           -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1  ,   0, 1, 0, 0)
                     
    val ruleset = Ruleset.build(vec)

    
    assert(vec == ruleset.getVectorRep(true));
    
  }
  
  "build (without default) then getVectorRep (without default)" should "be opposite operations" in {
    val vec = Vector[Byte](-1, 1, 1, 1, 0,-1,-1,-1, 1, 0, 2  ,   1, 0, 1, 0,
                            1,-1, 0, 0, 1, 1,-1, 1,-1,-1,-1  ,   0, 0, 1, 0,
                            2,-1,-1,-1, 1,-1,-1,-1, 0,-1,-1  ,   0, 1, 0, 0)
                     
    val ruleset = Ruleset.build(vec)

    
    assert(vec == ruleset.getVectorRep(false));
  }
  
  "getBestExAction" should "return best fitting action as ExAction" in {
    val vec = Vector[Byte]( 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1  ,   0, 0, 0, 1,
                            0, 0,-1, 0,-1,-1,-1,-1,-1,-1,-1  ,   0, 0, 1, 0,
                            0, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1  ,   0, 0, 1, 1,
                            0, 0, 0,-1, 0, 0,-1,-1,-1,-1,-1  ,   0, 1, 0, 0,
                            0, 0, 0, 0,-1, 0, 0, 0, 1,-1,-1  ,   0, 1, 0, 1,
                            0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1  ,   0, 1, 1, 1,
                           -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1  ,   1, 0, 0, 0)
                           
   val ruleset = Ruleset.build(vec)                         
    
   assert(ExAction(Vector[Byte](0, 1, 0, 0)).deep == ruleset.getBestExAction(Vector[Byte](0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)).deep)
   assert(ExAction(Vector[Byte](0, 0, 0, 1)).deep == ruleset.getBestExAction(Vector[Byte](0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)).deep)
   assert(ExAction(Vector[Byte](0, 0, 1, 0)).deep == ruleset.getBestExAction(Vector[Byte](0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1)).deep) 
   assert(ExAction(Vector[Byte](0, 1, 1, 1)).deep == ruleset.getBestExAction(Vector[Byte](0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1)).deep) 
   assert(ExAction(Vector[Byte](1, 0, 0, 0)).deep == ruleset.getBestExAction(Vector[Byte](1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1)).deep) 
  }
  
}