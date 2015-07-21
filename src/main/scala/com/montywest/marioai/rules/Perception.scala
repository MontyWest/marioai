package com.montywest.marioai.rules

import ch.idsia.benchmark.mario.environments.Environment
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene

abstract sealed class Perception(val index: Int) {
  def apply(environment: Environment): Byte
}

/**
 * An Byte Perception returns an Byte between 0 (inclusive) and limit (exclusive)
 */
abstract sealed class BytePerception(index: Int, val limit: Byte) extends Perception(index)

/**
 * A Bool Perception returns either true or false
 */
abstract sealed class BoolPerception(index: Int) extends Perception(index) {
  val TRUE: Byte = 1
  val FALSE: Byte = 0
}


case object MarioMode extends BytePerception(0, 3) {
  
  val SMALL: Byte = 0; val BIG: Byte = 1; val FIRE: Byte = 2;
  
  /***
   * Returns:
   *  0 for Small
   *  1 for Big
   *  2 for Fire
   */
  def apply(environment: Environment): Byte = {
    environment.getMarioStatus().toByte
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
  def apply(environment: Environment): Byte = {
    if(Perception.enemyInBoxRelativeToMario(environment, (2, -1), (-3,-3))) // Minus = (Up,Left) | Plus = (Down,Right)
      1 else 0  
  }
}
case object EnemyUpperRight extends BoolPerception(4) {
  def apply(environment: Environment): Byte = {
    if(Perception.enemyInBoxRelativeToMario(environment, (-1, 0), (-3, 1)) ||
    Perception.enemyInBoxRelativeToMario(environment, (-2, 2), (-3, 3)))// Minus = (Up,Left) | Plus = (Down,Right)
      1 else 0
  }
}
case object EnemyLowerRight extends BoolPerception(5) {
  def apply(environment: Environment): Byte = {
    if(Perception.enemyInBoxRelativeToMario(environment, (0, 0), (3, 4))) // Minus = (Up,Left) | Plus = (Down,Right)
      1 else 0
  }
}
case object ObstacleAhead extends BoolPerception(6) {
  def apply(environment: Environment): Byte = {
    if(Perception.obstacleInBoxRelativeToMario(environment, (0,1), (-1,4)))
      1 else 0
  }
}
case object PitAhead extends BoolPerception(7) {
  def apply(environment: Environment): Byte = {
    if(Perception.pitRelativeToMario(environment, 1, 4))
      1 else 0
  }
}
case object PitBelow extends BoolPerception(8) {
  def apply(environment: Environment): Byte = {
    if(Perception.pitRelativeToMario(environment, -1, 1))
      1 else 0
  }
}
case object MovingX extends BytePerception(9, 3) {
  
  val STILL: Byte = 0; val LEFT: Byte = 1; val RIGHT: Byte = 2;
  /***
   * 0 = No movement
   * 1 = Left
   * 2 = Right
   */
  def apply(environment: Environment): Byte = {
    environment.getMarioMovement.apply(0) match {
      case 0 => 0
      case -1 => 1
      case 1 => 2
    }
  }
}


object Perception {
  
  val NUMBER_OF_PERCEPTIONS = 10;
  
  implicit def per2int(perception: Perception): Int = perception.index
  
  //Not sure about this method...
  def fromInt(n: Int): Perception = n match {
    case 0 => MarioMode
    case 1 => JumpAvailable
    case 2 => OnGround
    case 3 => EnemyLeft
    case 4 => EnemyUpperRight
    case 5 => EnemyLowerRight
    case 6 => ObstacleAhead
    case 7 => PitAhead
    case 8 => PitBelow
    case 9 => MovingX
    case _ => throw new IllegalArgumentException(s"Looking for perception $n, but there are only $NUMBER_OF_PERCEPTIONS.")
  }
  
  val EGO_POS_ROW_INDEX = 0;
  val EGO_POS_COL_INDEX = 1;
  
  def enemyInBoxRelativeToMario(environment: Environment, a: (Int, Int), b: (Int, Int)): Boolean = {
    val enemies = environment.getEnemiesObservationZ(2); //Z-index 2 gives 1 for enemy, 0 for anything else
    val test = (x: Byte) => x == 1;
    
    checkBox(enemies, test, getMarioPos(environment), a, b)
  }
  
  def obstacleInBoxRelativeToMario(environment: Environment, a: (Int, Int), b: (Int, Int)): Boolean = {
    val level = environment.getLevelSceneObservationZ(2); //
    val test = (x: Byte) => x == 1 || x == GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH;
    
    checkBox(level, test, getMarioPos(environment), a, b)
  }
  
  def obstacleFillBoxRelativeToMario(environment: Environment, a: (Int, Int), b: (Int, Int)): Boolean = {
    val level = environment.getLevelSceneObservationZ(2); //
    val test = (x: Byte) => x != 1 && x != GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH;
    
    checkBox(level, test, getMarioPos(environment), a, b, false)
  }
  
  def pitRelativeToMario(environment: Environment, a: Int, b: Int): Boolean = {
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
    !opens.isEmpty
  }
  
  private def getMarioPos(environment: Environment): (Int, Int) = {
    val marioPos = environment.getMarioEgoPos;
    (marioPos(EGO_POS_ROW_INDEX), marioPos(EGO_POS_COL_INDEX))
  }
  
  private def checkBox(grid: Array[Array[Byte]], test: Byte=>Boolean, mario: (Int, Int), a: (Int, Int), b: (Int, Int), ret: Boolean = true): Boolean = {
    import Math.min
    import Math.max
    
    val relARow = min(grid.length-1, max(0, (a._1 + mario._1)))
    val relACol = min(grid(0).length-1, max(0, (a._2 + mario._2)))
    val relBRow = min(grid.length-1, max(0, (b._1 + mario._1)))
    val relBCol = min(grid(0).length-1, max(0, (b._2 + mario._2)))
    
    for {
      i <- min(relARow, relBRow) to max(relARow, relBRow)
      j <- min(relACol, relBCol) to max(relARow, relBCol)
      if (test(grid(i)(j)))
    }{
        return ret
    }
    !ret
  }
}