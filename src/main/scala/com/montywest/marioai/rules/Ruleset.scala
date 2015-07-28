package com.montywest.marioai.rules

import scala.annotation.tailrec

class Ruleset( val rules: Seq[Rule], val defaultAction: MWAction, favourHigher: Boolean = true) {

  private val newRuleBetter: (Int, Int) => Boolean = {
    if(favourHigher)
      (best:Int, newScore: Int) => best<newScore
    else 
      (best:Int, newScore: Int) => best<=newScore
  }
  
  def getBestExAction(observation: Observation): ExAction = {
    
    @tailrec
    def getBestRuleRecu(ls: Seq[Rule], best: Option[Rule] = None, bestScore: Int = 0): Option[Rule] = ls match {
      case Nil => best
      case r :: ts => {
        val newScore = r.scoreAgainst(observation)
        if (newRuleBetter(bestScore, newScore))
          getBestRuleRecu(ts, Some(r), newScore)
        else
          getBestRuleRecu(ts, best, bestScore)
      }
    }
    
    getBestRuleRecu(rules) match {
      case None => ExAction(defaultAction)
      case Some(r) => r.getExAction
    }
  }
  
  def getVectorRep(withDefaultAction: Boolean = false): Vector[Byte] = {
    if (!withDefaultAction)
      (rules.map { r => r.getVectorRep } toVector).flatten
    else 
      (rules.map { r => r.getVectorRep } toVector).flatten ++
      Vector.fill(Conditions.LENGTH)(-1: Byte) ++ defaultAction
  }
  
  
  override def toString: String = {
    (rules map { r => r.toString() } mkString("\n")) + "\n" +
    Rule(Vector.fill(Conditions.LENGTH)(-1: Byte), defaultAction).toString()
  }

}


object Ruleset {
  
  private val FALLBACK_ACTION = MWAction(KeyRight, KeySpeed, KeyJump)
  
  def apply(rules: Seq[Rule], defaultAction: Set[KeyPress]): Ruleset = {
    new Ruleset(rules, MWAction(defaultAction))
  }
  
  def apply(vec: Vector[Byte]): Ruleset = {
    val twodim = vec.grouped(Rule.TOTAL_LENGTH).toVector
    val lastIndex = twodim.length - 1
    if (twodim(lastIndex).length == MWAction.LENGTH) {
      new Ruleset(twodim.slice(0, lastIndex) map { vr => Rule(vr) } toSeq,
                  twodim(lastIndex))
    } else if (twodim(lastIndex).length == Rule.TOTAL_LENGTH) {
      new Ruleset(twodim map { vr => Rule(vr) } toSeq,
                  FALLBACK_ACTION)
    } else throw new IllegalArgumentException("Malformed vector representation of ruleset")
  }
  
}