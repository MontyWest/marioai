package com.montywest.marioai.rules

import scala.annotation.migration
import scala.annotation.tailrec

class Rule private[Rule] (vector: Vector[Byte]) {

  def getVectorRep: Vector[Byte] = {
    vector
  }
  
  def getConditions: Conditions = {
    vector.slice(0, Conditions.LENGTH)
  }
  
  def getExAction: ExAction = {
    ExAction(getMWAction)     
  }
  
  def getMWAction: MWAction = {
    vector.slice(Conditions.LENGTH, Rule.TOTAL_LENGTH)
  }
  
  def scoreAgainst(observation: Observation): Int = {
    if (observation.length != Conditions.LENGTH)
      throw new IllegalArgumentException("observation is a different length to conditions")
    @tailrec
    def scoreRecu(i: Int, sum: Int = 0): Int =  {
      if (i == Conditions.LENGTH) sum
      else getConditions(i) match {
        case Conditions.DONT_CARE => scoreRecu(i+1, sum)
        case b if b == observation(i) => scoreRecu(i+1, sum+1)
        case _ => -1
      }
    }
    scoreRecu(0)
  }
  
  override def toString(): String = {
    val b2str = (b: Byte) => b match {
      case -1 => "_"
      case b => b.toString()
    }
    
    (getConditions.map { b2str } mkString(" ")) + " | " + (getMWAction map { _.toString } mkString(" "))
  }
  
  override def hashCode: Int =
    this.getVectorRep.hashCode()
  
  
  override def equals(other: Any): Boolean = other match {
    case that: Rule =>
      that.getVectorRep == this.getVectorRep
    case _ => false
  }
  
    
}

object Rule {
  
  val TOTAL_LENGTH = Conditions.LENGTH + MWAction.LENGTH
  
  //Creational  
  private val ACTION_LEFT_INDEX = MWAction.LEFT_INDEX + Conditions.LENGTH;
  private val ACTION_RIGHT_INDEX = MWAction.RIGHT_INDEX + Conditions.LENGTH;
  private val ACTION_JUMP_INDEX = MWAction.JUMP_INDEX + Conditions.LENGTH;
  private val ACTION_SPEED_INDEX = MWAction.SPEED_INDEX + Conditions.LENGTH;
  
  val BLANK_RULE = new Rule(
      Vector.fill(Conditions.LENGTH)(Conditions.DONT_CARE) ++ Vector.fill(MWAction.LENGTH)(MWAction.ACTION_FALSE)
  )
  
  def apply(vec: Vector[Byte]): Rule = {
    if (vec.length == Conditions.LENGTH + MWAction.LENGTH)
      new Rule(vec)
    else 
      throw new IllegalArgumentException("Rule vector length incorrect")
  }
  
  def build(conditionMap: Map[Perception, Byte], actionSet: Set[KeyPress]): Rule = {
	  new Rule(Conditions(conditionMap) ++ MWAction(actionSet))
  }

  def apply(conditions: Conditions, action: MWAction): Rule = {
    if ((conditions.length == Conditions.LENGTH) && (action.length == MWAction.LENGTH)) {
    	new Rule(conditions ++ action);      
    } else {
      throw new IllegalArgumentException("Rule vector length incorrect")
    }
  }
  
}