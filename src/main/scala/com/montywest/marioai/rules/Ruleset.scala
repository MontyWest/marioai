package com.montywest.marioai.rules

import scala.annotation.tailrec
import scala.language.implicitConversions
import scala.language.postfixOps
import scala.collection.mutable.WrappedArray
import scala.collection.mutable.Map

/***
 * Container for an agent's rules.
 * Includes a default action if no rules match the current environment
 */
class Ruleset( val rules: Seq[Rule], val defaultAction: MWAction, favourHigher: Boolean = true) {
  
  /***
   * Logs which rules are used and how many times.
   */
  val ruleUsage: Map[Int, Int] = Map();
  
  def length = rules.length
  
  /***
   * Returns true is second argument is strictly higher than first argument.
   * If they are equal then result return is NOT favourHigher.
   */
  private val newRuleBetter: (Int, Int) => Boolean = {
    if(favourHigher)
      (best:Int, newScore: Int) => best<newScore
    else 
      (best:Int, newScore: Int) => best<=newScore
  }
  
  /***
   * Applies observation ot rule set
   * Returns the action that belongs the highest scoring rule,
   * as an ExAction.
   */
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
  
  /***
   * Returns the vector representation of the ruleset,
   * defaultAction can be removed with parameter.
   * If included it is represented by rule contain all DONT_CARE for conditions
   */
  def getVectorRep(withDefaultAction: Boolean = true): Vector[Byte] = {
    if (!withDefaultAction)
      (rules.map { r => r.getVectorRep } toVector).flatten
    else 
      (rules.map { r => r.getVectorRep } toVector).flatten ++
      Vector.fill(Conditions.LENGTH)(-1: Byte) ++ defaultAction
  }
  
  /***
   * Increments the map store of rule usage for rule index
   */
  private def incrementRuleUsage(index: Int): Unit = {
//    println("Rule used: " + index)
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
  
  /***
   * Builds a ruleset from a set of rules and default action keypresses
   */
  def apply(rules: Seq[Rule], defaultAction: Set[KeyPress]): Ruleset = {
    new Ruleset(rules, MWAction.build(defaultAction))
  }
  
  /***
   * Builds a ruleset from its vector representation.
   * If the vector contains a DONT_CARE rule as its final rule, then this is used as default action
   * A fallback default action can be passed that is used if a default action is not present.
   */
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
  
  /***
   * Builds a ruleset from its array representation.
   * If the array contains a DONT_CARE rule as its final rule, then this is used as default action
   * A fallback default action can be passed that is used if a default action is not present.
   */
  def buildFromArray(arr: Array[Byte], fallbackAction: MWAction): Ruleset = {
    val wa: WrappedArray[Byte] = arr
    Ruleset.build(wa.toVector, fallbackAction)
  }
  
}