package com.montywest.marioai.rules

import scala.annotation.tailrec
import scala.collection.mutable.WrappedArray

class Ruleset( val rules: Seq[Rule], val defaultAction: MWAction, favourHigher: Boolean = true) {

  import scala.collection.mutable.Map
  val ruleUsage: Map[Int, Int] = Map();
  
  def length = rules.length
  
  private val newRuleBetter: (Int, Int) => Boolean = {
    if(favourHigher)
      (best:Int, newScore: Int) => best<newScore
    else 
      (best:Int, newScore: Int) => best<=newScore
  }
  
  def getBestExAction(observation: Observation): ExAction = {
    
    @tailrec
    def getBestRuleRecu(ls: Seq[Rule], best: Option[(Int, Rule)] = None, bestScore: Int = 0, index: Int = 0): Option[(Int, Rule)] = ls match {
      case Nil => best
      case (r +: ts) => {
        val newScore = r.scoreAgainst(observation)
        if (newRuleBetter(bestScore, newScore))
          getBestRuleRecu(ts, Some((index, r)), newScore, index+1)
        else
          getBestRuleRecu(ts, best, bestScore, index+1)
      }
    }
    
    getBestRuleRecu(rules) match {
      case None => {
        this.incrementRuleUsage(-1)
        ExAction(defaultAction)
      }
      case Some((i,r)) => {
        this.incrementRuleUsage(i)
        r.getExAction
      }
    }
  }
  
  def getVectorRep(withDefaultAction: Boolean = true): Vector[Byte] = {
    if (!withDefaultAction)
      (rules.map { r => r.getVectorRep } toVector).flatten
    else 
      (rules.map { r => r.getVectorRep } toVector).flatten ++
      Vector.fill(Conditions.LENGTH)(-1: Byte) ++ defaultAction
  }
  
  private def incrementRuleUsage(index: Int): Unit = {
    ruleUsage.get(index) match {
      case None => ruleUsage.put(index, 1)
      case Some(x) => ruleUsage.put(index, x+1)
    }
  }
  
  def resetRuleUsage = {
    ruleUsage.clear()
  }
  
  override def toString: String = {
    rules.zipWithIndex map { 
        case (r:Rule, i:Int) => {
          val sep = if (i<10) " " else ""
          sep	+ i + ". " + r.toString()
        }
      } mkString(
          "    " + Rule.PRINT_HEADER + "\n",
          "\n", 
          "\n-1. " + Rule(Vector.fill(Conditions.LENGTH)(-1: Byte) ++ defaultAction).toString())
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