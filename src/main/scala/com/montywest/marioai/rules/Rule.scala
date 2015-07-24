package com.montywest.marioai.rules

import ch.idsia.benchmark.mario.engine.sprites.Mario
import ch.idsia.benchmark.mario.environments.Environment

class Rule private[Rule] (vector: Vector[Byte]) {

  def getConditions: Conditions = {
    vector.slice(0, Rule.CONDITION_LENGTH)
  }
  
  def getAction: Action = {
    Array.tabulate(Rule.EXTERNAL_ACTION_LENGTH)((x: Int) => {
      x match {
        case Mario.KEY_LEFT => Rule.ACTION_TRUE == vector(Rule.ACTION_LEFT_INDEX)
        case Mario.KEY_RIGHT => Rule.ACTION_TRUE == vector(Rule.ACTION_RIGHT_INDEX)
        case Mario.KEY_JUMP => Rule.ACTION_TRUE == vector(Rule.ACTION_JUMP_INDEX)
        case Mario.KEY_SPEED => Rule.ACTION_TRUE == vector(Rule.ACTION_SPEED_INDEX)
        case _ => false
      }
    })      
  }
  
  private def getInternalAction: Vector[Byte] = {
    vector.slice(Rule.CONDITION_LENGTH, Rule.CONDITION_LENGTH+Rule.INTERNAL_ACTION_LENGTH)
  }
  
  override def toString(): String = {
    val b2str = (b: Byte) => b match {
      case -1 => "_"
      case b => b.toString()
    }
    
    (getConditions.map { b2str } mkString(" ")) + "| " + (getInternalAction mkString(" "))
  }
}

object Rule {
  
  val DONT_CARE: Byte = -1;
  
  val ACTION_TRUE: Byte = 1;
  val ACTION_FALSE: Byte = 0;
  
  
  //Judgement
  
  private val CONDITION_LENGTH = Perception.NUMBER_OF_PERCEPTIONS  
  
  def score(observation: Observation, rule: Rule): Int = {
    if (observation.length != CONDITION_LENGTH)
      throw new IllegalArgumentException("observation is a different length to conditions")
    
    def scoreRecu(i: Int, sum: Int = 0): Int =  {
      if (i == CONDITION_LENGTH) sum
      else rule.getConditions(i) match {
        case DONT_CARE => scoreRecu(i+1, sum)
        case b if b == observation(i) => scoreRecu(i+1, sum+1)
        case _ => -1
      }
    }
    scoreRecu(0)
  }
  
  //Creational

		  
  private val EXTERNAL_ACTION_LENGTH = Environment.numberOfKeys
  
  private val INTERNAL_ACTION_LENGTH = 4
  private val ACTION_LEFT_INDEX = 0 + CONDITION_LENGTH;
  private val ACTION_RIGHT_INDEX = 1 + CONDITION_LENGTH;
  private val ACTION_JUMP_INDEX = 2 + CONDITION_LENGTH;
  private val ACTION_SPEED_INDEX = 3 + CONDITION_LENGTH;
  
  def apply(vec: Vector[Byte]): Rule = {
    if (vec.length == CONDITION_LENGTH + INTERNAL_ACTION_LENGTH)
      new Rule(vec)
    else 
      throw new IllegalArgumentException("Rule vector length incorrect")
  }
  
  def apply(conditions: Map[Perception, Byte], actions: Set[KeyPress]): Rule = {
    if (conditions.forall(validateCondition)) {
      val perMap = conditions.map { case (p, b) => (p.index, b) }
      val actIndexSet = actions.map { getKeyPressIndex }
      
      new Rule(
        Vector.tabulate(CONDITION_LENGTH+INTERNAL_ACTION_LENGTH)( (i: Int) => 
          if (i < CONDITION_LENGTH) {
            perMap get(i) match {
              case None => DONT_CARE
              case Some(b: Byte) => b
            }
          } else {
            if (actIndexSet.contains(i)) ACTION_TRUE
            else ACTION_FALSE
          }
        )
      )
    } else throw new IllegalArgumentException("Perception byte value out of range")
  }
  
  private def validateCondition(cond: (Perception, Byte)): Boolean = cond match {
    case (bp : BoolPerception, b) => (bp.TRUE == b) || (bp.FALSE == b)
    case (ip : BytePerception, b) => (0 <= b) && (b < ip.limit)
  }
  
  private def getKeyPressIndex(kp: KeyPress): Int = kp match {
    case KeyLeft => ACTION_LEFT_INDEX
    case KeyRight => ACTION_RIGHT_INDEX
    case KeyJump => ACTION_JUMP_INDEX
    case KeySpeed => ACTION_SPEED_INDEX
  }
}