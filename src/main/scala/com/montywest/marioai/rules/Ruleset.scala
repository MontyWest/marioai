package com.montywest.marioai.rules

import scala.annotation.tailrec
import scala.collection.mutable.WrappedArray

class Ruleset( val rules: Seq[Rule], val defaultAction: MWAction, favourHigher: Boolean = true) {

  private val newRuleBetter: (Int, Int) => Boolean = {
    if(favourHigher)
      (best:Int, newScore: Int) => best<newScore
    else 
      (best:Int, newScore: Int) => best<=newScore
  }
  
  def getBestExAction(observation: Observation): ExAction = {
    
    @tailrec
    def getBestRuleRecu(ls: Seq[Rule], best: Option[Rule] = None, bestScore: Int = 0, index: Int = 0, bestIndex: Option[Int] = None): Option[Rule] = ls match {
      case Nil => {
//        val i = bestIndex match {
//          case None => "def"
//          case Some(x) => "" + (2*x + 25)
//        }
//        println("Rule chosen - " + i)
        best
      }
      case (r +: ts) => {
        val newScore = r.scoreAgainst(observation)
        if (newRuleBetter(bestScore, newScore))
          getBestRuleRecu(ts, Some(r), newScore, index+1, Some(index))
        else
          getBestRuleRecu(ts, best, bestScore, index+1, bestIndex)
      }
    }
    
    getBestRuleRecu(rules) match {
      case None => ExAction(defaultAction)
      case Some(r) => r.getExAction
    }
  }
  
  def getVectorRep(withDefaultAction: Boolean = true): Vector[Byte] = {
    if (!withDefaultAction)
      (rules.map { r => r.getVectorRep } toVector).flatten
    else 
      (rules.map { r => r.getVectorRep } toVector).flatten ++
      Vector.fill(Conditions.LENGTH)(-1: Byte) ++ defaultAction
  }
  
  
  override def toString: String = {
    (rules map { r => r.toString() } mkString("\n")) + "\n" +
    Rule(Vector.fill(Conditions.LENGTH)(-1: Byte) ++ defaultAction).toString()
  }

  override def hashCode: Int =
    this.getVectorRep(true).hashCode()
  
  
  override def equals(other: Any): Boolean = other match {
    case that: Rule =>
      that.getVectorRep == this.getVectorRep(true)
    case _ => false
  }
}


object Ruleset {
  
  val FALLBACK_ACTION = MWAction(KeyRight, KeySpeed, KeyJump)
  
  def apply(rules: Seq[Rule], defaultAction: Set[KeyPress]): Ruleset = {
    new Ruleset(rules, MWAction.build(defaultAction))
  }
  
  def build(vec: Vector[Byte], fallbackDefaultAction: MWAction = FALLBACK_ACTION): Ruleset = {
    val twodim = vec.grouped(Rule.TOTAL_LENGTH).toVector
    val lastIndex = twodim.length - 1
    if (twodim(lastIndex).length == Rule.TOTAL_LENGTH) {
      val lastIndexSplit = twodim(lastIndex).splitAt(Conditions.LENGTH)
      //Default Included
      if (lastIndexSplit._1.forall { _ == (-1: Byte) }) {
        new Ruleset(twodim.slice(0, lastIndex) map { vr => Rule(vr) } toSeq,
                    lastIndexSplit._2)
      }
      // No Default 
      else {
        new Ruleset(twodim map { vr => Rule(vr) } toSeq,
                    fallbackDefaultAction)
      }
      
    } else throw new IllegalArgumentException("Malformed vector representation of ruleset")
  }
  
  def buildFromArray(arr: Array[Byte], fallbackAction: MWAction): Ruleset = {
    val wa: WrappedArray[Byte] = arr
    Ruleset.build(wa.toVector, fallbackAction)
  }
  
}