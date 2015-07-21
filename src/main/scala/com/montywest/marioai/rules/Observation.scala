package com.montywest.marioai.rules

import ch.idsia.benchmark.mario.environments.Environment

object Observation {

  def getObservation(environment: Environment): Observation = {
    Vector.tabulate(Perception.NUMBER_OF_PERCEPTIONS)
      {n: Int => Perception.fromInt(n) match {
          case MarioMode => MarioMode(environment)
          case JumpAvailable => JumpAvailable(environment)
          case ObstacleAhead => ObstacleAhead(environment)
          case PitBelow => PitBelow(environment)
          case PitAhead => PitAhead(environment)
          case EnemyLeft => EnemyLeft(environment)
          case EnemyUpperRight => EnemyUpperRight(environment)
          case EnemyLowerRight => EnemyLowerRight(environment)
          case OnGround => OnGround(environment)
          case MovingX => MovingX(environment)
        }
      }
  }
  
  val BLANK_OBSERVATION: Observation  = Vector.fill(Perception.NUMBER_OF_PERCEPTIONS)(0)
  
}