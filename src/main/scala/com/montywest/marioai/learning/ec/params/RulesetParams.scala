package com.montywest.marioai.learning.ec.params

import com.montywest.marioai.learning.ec.DynamicSpeciesParameters
import com.montywest.marioai.rules._

class RulesetParams extends DynamicSpeciesParameters {

  val ruleLength = Rule.TOTAL_LENGTH
  val conditionLength = Conditions.LENGTH
  val actionLength = MWAction.LENGTH
  
  def getIndexType(index: Int): IndexType = (index % ruleLength) match {
    case n if n < conditionLength => Condition
    case _ => Action
  } 
  
  def runOnIndexes(indexType: IndexType, indexLimit: Int)(op: Int=>Unit): Unit =  indexType match {
    case Condition => {
      for (
        i <- 0 until indexLimit by ruleLength;
        j <- 0 until conditionLength
      ){op(i+j)}
    }
    case Action => {
      for(
        i <- 0 until indexLimit by ruleLength;
        j <- conditionLength until ruleLength
      ){op(i+j)}
    }
  }
  
  val favouredCondition = Conditions.DONT_CARE
  val favouredAction = MWAction.ACTION_FALSE
  
  override def minGene(index: Int): Option[Int] = getIndexType(index) match {
    case Condition => Some(Conditions.DONT_CARE)
    case Action => Some(Math.min(MWAction.ACTION_FALSE, MWAction.ACTION_TRUE))
  } 
  
  override def maxGene(index: Int): Option[Int] = getIndexType(index) match {
    case Action => Some(Math.max(MWAction.ACTION_FALSE, MWAction.ACTION_TRUE))
    case Condition => Some(Conditions.getLimitForIndex((index % ruleLength)).toInt);
  }
  
}

sealed trait IndexType

case object Condition extends IndexType
case object Action extends IndexType