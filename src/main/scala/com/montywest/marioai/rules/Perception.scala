package com.montywest.marioai.rules

import ch.idsia.benchmark.mario.environments.Environment
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene
import Math.{min, max}
import com.montywest.marioai.util.PrintUtils

abstract sealed class Perception(val index: Int) {
  def apply(environment: Environment): Byte
  def unapply(index: Int): Boolean = index == this.index
}

/**
 * An Byte Perception returns an Byte between 0 (inclusive) and limit (inclusive)
 */
abstract sealed class BytePerception(index: Int, val limit: Byte) extends Perception(index)

/**
 * A Bool Perception returns either true or false
 */
abstract sealed class BoolPerception(index: Int) extends Perception(index) {
  val TRUE: Byte = 1
  val FALSE: Byte = 0
}


case object MarioMode extends BytePerception(0, 2) {
  
  val SMALL: Byte = 0; val LARGE: Byte = 1; val FIRE: Byte = 2;
  
  /***
   * Returns:
   *  0 for Small
   *  1 for Big
   *  2 for Fire
   */
  def apply(environment: Environment): Byte = {
    min( max(environment.getMarioStatus(), 0), limit-1).toByte
  }
}
case object JumpAvailable extends BoolPerception(1) {
  def apply(environment: Environment): Byte = {
    if(environment.isMarioAbleToJump) 1 else 0
  }
}
case object OnGround extends BoolPerception(2) {
  def apply(environment: Environment): Byte = {
    if(environment.isMarioOnGround) 1 else 0
  }
}
case object EnemyLeft extends BoolPerception(3) {
  val AREA_UL = (-2,-2); val AREA_BR = (1, -1); // Minus = (Up,Left) | Plus = (Down,Right)
  
  def apply(environment: Environment): Byte = {
    if(Perception.enemyInBoxRelativeToMario(environment, AREA_UL, AREA_BR)) 
      1 else 0  
  }
}
case object EnemyUpperRight extends BoolPerception(4) {
  val AREA_1_UL = (-3, 0); val AREA_1_BR = (-1, 3);
  val AREA_2_UL = (-3, 3); val AREA_2_BR = (-2,5);
  
  def apply(environment: Environment): Byte = {
    if(Perception.enemyInBoxRelativeToMario(environment, AREA_1_UL, AREA_1_BR) ||
    Perception.enemyInBoxRelativeToMario(environment, AREA_2_UL, AREA_2_BR))
      1 else 0
  }
}
case object EnemyLowerRight extends BoolPerception(5) {
  val AREA_1_UL = (0, 1); val AREA_1_BR = (1, 2);
  val AREA_2_UL = (-1, 2); val AREA_2_BR = (2, 2);
  
  def apply(environment: Environment): Byte = {
    if(Perception.enemyInBoxRelativeToMario(environment, AREA_1_UL, AREA_1_BR) ||
    Perception.enemyInBoxRelativeToMario(environment, AREA_2_UL, AREA_2_BR))
      1 else 0
  }
  
}
case object ObstacleAhead extends BoolPerception(6) {
  val AREA_OBS_UL = (-2, 1); val AREA_OBS_BR = (0, 3);//4
  val AREA_STEP_UL = (1, 1); val AREA_STEP_BR = (2, 3);//5
  
  def apply(environment: Environment): Byte = {
    if(Perception.obstacleInBoxRelativeToMario(environment, AREA_OBS_UL, AREA_OBS_BR) ||
        Perception.stepInBoxRelativeToMario(environment, AREA_STEP_UL, AREA_STEP_BR) 
        )
      1 else 0
  }
  
}
case object PitAhead extends BoolPerception(7) {
  val COL_L = 1; val COL_R = 2
  
  def apply(environment: Environment): Byte = {
    if(Perception.pitRelativeToMario(environment, COL_L, COL_R))
      1 else 0
  }
  
}
case object PitBelow extends BoolPerception(8) {
  val COL_L = 0; val COL_R = 0;
  
  def apply(environment: Environment): Byte = {
    if(Perception.pitRelativeToMario(environment, COL_L, COL_R))
      1 else 0
  }
  
}
case object MovingX extends BytePerception(9, 2) {
  
  val STILL: Byte = 0; val LEFT: Byte = 1; val RIGHT: Byte = 2;
  /***
   * 0 = No movement
   * 1 = Left
   * 2 = Right
   */
  def apply(environment: Environment): Byte = {
    environment.getMarioMovement.apply(0) match {
      case 0 => {
        0
      }
      case -1 => {
        1
      }
      case 1 => 2
      case _ => 0
    }
  }
  

}
case object MovingY extends BytePerception(10, 2) {
  
  val STILL: Byte = 0; val DOWN: Byte = 1; val UP: Byte = 2;
  /***
   * 0 = No movement
   * 1 = DOWN
   * 2 = UP
   */
  def apply(environment: Environment): Byte = {
    environment.getMarioMovement.apply(1) match {
      case 0 => 0
      case 1 => 1
      case -1 => 2
      case _ => 0
    }
  }
  
}


object Perception {
  
  val NUMBER_OF_PERCEPTIONS = 11;
  
  implicit def per2int(perception: Perception): Int = perception.index
  
  def unapply(n: Int): Option[Perception] = n match {
    case MarioMode() => Option(MarioMode)
    case JumpAvailable() => Option(JumpAvailable)
    case OnGround() => Option(OnGround)
    case EnemyLeft() => Option(EnemyLeft)
    case EnemyUpperRight() => Option(EnemyUpperRight)
    case EnemyLowerRight() => Option(EnemyLowerRight)
    case ObstacleAhead() => Option(ObstacleAhead)
    case PitAhead() => Option(PitAhead)
    case PitBelow() => Option(PitBelow)
    case MovingX() => Option(MovingX)
    case MovingY() => Option(MovingY)
    case _ => None
  }
  
  val EGO_POS_ROW_INDEX = 0;
  val EGO_POS_COL_INDEX = 1;
  
  def enemyInBoxRelativeToMario(environment: Environment, a: (Int, Int), b: (Int, Int)): Boolean = {
    val enemies = environment.getEnemiesObservationZ(2); //Z-index 2 gives 1 for enemy, 0 for anything else
    val test = (grid: Array[Array[Byte]], tup: Tuple2[Int, Int]) => {
      val x = grid(tup._1)(tup._2)
      x == 1
    }
    
    checkBox(enemies, test, getMarioPos(environment), a, b)
  }
  
  def obstacleInBoxRelativeToMario(environment: Environment, a: (Int, Int), b: (Int, Int)): Boolean = {
    val level = environment.getLevelSceneObservationZ(2); //
    val test = (grid: Array[Array[Byte]], tup: Tuple2[Int, Int]) => {
      val x = grid(tup._1)(tup._2)
      obs(x)
    }
    
    val opens = this.getOpens(environment, a._2, b._2);
    
    val b2 = (b._1, {
      opens.headOption match {
        case None => b._2
        case Some(x) => x-1
      }
    })
    
    if (b2._2 < a._2) {
      false
    } else {
      checkBox(level, test, getMarioPos(environment), a, b2)
    }
  }

  def stepInBoxRelativeToMario(environment: Environment, a: (Int, Int), b: (Int, Int)): Boolean = {
    val level = environment.getLevelSceneObservationZ(2); //
    val test = (grid: Array[Array[Byte]], tup: Tuple2[Int, Int]) => {
      val y = tup._2; val x = tup._1;
      (y != Math.max(a._2, b._2)) && obs(grid(x)(y+1)) && !obs(grid(x)(y))
    }   

    val opens = this.getOpens(environment, a._2, b._2);
    val b2 = (b._1, {
      opens.headOption match {
        case None => b._2
        case Some(x) => x-1
      }
    })
    
    if (b2._2 < a._2) {
      false
    } else {
      checkBox(level, test, getMarioPos(environment), a, b2)
    }
  }
  
  def obstacleFillBoxRelativeToMario(environment: Environment, a: (Int, Int), b: (Int, Int)): Boolean = {
    val level = environment.getLevelSceneObservationZ(2); //
    val test = (grid: Array[Array[Byte]], tup: Tuple2[Int, Int]) => {
    	val x = grid(tup._1)(tup._2)
      !obs(x)
    }      

    checkBox(level, test, getMarioPos(environment), a, b, false)
    
  }
  
  private def getOpens(environment: Environment, a: Int, b: Int): List[Int] = {
    val level = environment.getLevelSceneObservationZ(2); //
    val test = (x: Byte) => x == 0 || x == GeneralizerLevelScene.COIN_ANIM;
    val bottomRow = level.length - 1
    val mario = getMarioPos(environment)
    
    import Math.min
    import Math.max
    
    val left = max(0, min(a, b) + mario._2)
    val right = min(level(0).length, max(a, b) + mario._2)
        
    var opens: List[Int] = left to right toList
    
    for {
        i <- mario._1 + 1 to bottomRow
        if (!opens.isEmpty)
    }{
      opens = opens.filter { j => level(i)(j) == 0 || level(i)(j) == GeneralizerLevelScene.COIN_ANIM }
    }
    opens.map { x => x - mario._2 }
  }
  
  def pitRelativeToMario(environment: Environment, a: Int, b: Int): Boolean = {
    val opens = this.getOpens(environment, a, b)
    !opens.isEmpty
  }
  
  private def getMarioPos(environment: Environment): (Int, Int) = {
    val marioPos = environment.getMarioEgoPos;
    (marioPos(EGO_POS_ROW_INDEX), marioPos(EGO_POS_COL_INDEX))
  }
  
  private def checkBox(grid: Array[Array[Byte]], test: (Array[Array[Byte]], (Int, Int))=>Boolean, mario: (Int, Int), a: (Int, Int), b: (Int, Int), ret: Boolean = true): Boolean = {
    import Math.min
    import Math.max
    
    val relARow = min(grid.length-1, max(0, (a._1 + mario._1)))
    val relACol = min(grid(0).length-1, max(0, (a._2 + mario._2)))
    val relBRow = min(grid.length-1, max(0, (b._1 + mario._1)))
    val relBCol = min(grid(0).length-1, max(0, (b._2 + mario._2)))
    
    for {
      i <- min(relARow, relBRow) to max(relARow, relBRow)
      j <- min(relACol, relBCol) to max(relARow, relBCol)
      if (test(grid, (i, j)))
    }{
        return ret
    }
    !ret
  }
  
  def obs(b: Byte): Boolean = {
    b == 1 || b == GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH
  }
}