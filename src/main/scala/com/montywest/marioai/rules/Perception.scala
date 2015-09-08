package com.montywest.marioai.rules

import ch.idsia.benchmark.mario.environments.Environment
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene
import Math.{min, max}
import scala.language.implicitConversions
import scala.language.postfixOps

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

/***
 * Perceives Mario's current mode.
 * Mario's mode is reduced by colliding with enemies or collecting green mushrooms.
 * It can be raised to BIG by a mushroom and FIRE by a fire flower.
 */
case object MarioMode extends BytePerception(0, 2) {
  val SMALL: Byte = 0; val LARGE: Byte = 1; val FIRE: Byte = 2;
  
  def apply(environment: Environment): Byte = {
    min( max(environment.getMarioMode(), 0), limit).toByte
  }
}

/***
 * Perceives if Mario can Jump.
 * This is true if the jump key is not pressed and Mario is on ground or wall sliding.
 */
case object JumpAvailable extends BoolPerception(1) {
  def apply(environment: Environment): Byte = {
    if(environment.isMarioAbleToJump) 1 else 0
  }
}

/***
 * Perceives whether or not Mario is standing on the ground
 */
case object OnGround extends BoolPerception(2) {
  def apply(environment: Environment): Byte = {
    if(environment.isMarioOnGround) 1 else 0
  }
}

/***
 * Perceives whether or not there is an enemy to Mario's left.
 * The perception works with in the declared box coordinates.
 */
case object EnemyLeft extends BoolPerception(3) {
  val AREA_UL = (-2,-2); val AREA_BR = (1, -1);
  
  def apply(environment: Environment): Byte = {
    if(Perception.enemyInBoxRelativeToMario(environment, AREA_UL, AREA_BR)) 
      1 else 0  
  }
}

/***
 * Perceives whether or not there is an enemy to Mario's upper right.
 * The perception works with in the two declared box coordinates.
 */
case object EnemyUpperRight extends BoolPerception(4) {
  val AREA_1_UL = (-3, 0); val AREA_1_BR = (-1, 3);
  val AREA_2_UL = (-3, 3); val AREA_2_BR = (-2,5);
  
  def apply(environment: Environment): Byte = {
    if(Perception.enemyInBoxRelativeToMario(environment, AREA_1_UL, AREA_1_BR) ||
    Perception.enemyInBoxRelativeToMario(environment, AREA_2_UL, AREA_2_BR))
      1 else 0
  }
}

/***
 * Perceives whether or not there is an enemy to Mario's lower right.
 * The perception works with in the two declared box coordinates.
 */
case object EnemyLowerRight extends BoolPerception(5) {
  val AREA_1_UL = (0, 1); val AREA_1_BR = (1, 2);
  val AREA_2_UL = (-1, 2); val AREA_2_BR = (2, 2);
  
  def apply(environment: Environment): Byte = {
    if(Perception.enemyInBoxRelativeToMario(environment, AREA_1_UL, AREA_1_BR) ||
    Perception.enemyInBoxRelativeToMario(environment, AREA_2_UL, AREA_2_BR))
      1 else 0
  }
}


/***
 * Perceives whether or not there is an obstacle ahead of Mario.
 * It checks for rises in terrain, or steps in terrain in two boxes.
 * If there is a pit this perception is nullified.
 */
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

/***
 * Perceives whether or not there is a pit ahead of Mario.
 * Returns NONE if there are no pits, FAR and CLOSE is there is one in a certain number of columns relative to mario.
 */
case object PitAhead extends BytePerception(7, 2) {
  val NONE: Byte = 0; val CLOSE: Byte = 1; val FAR: Byte = 2;
  val COL_CLOSE_L = 1; val COL_CLOSE_R = 1; val COL_FAR_L = 2; val COL_FAR_R = 2
  
  def apply(environment: Environment): Byte = {
    val pitOp: Option[Int] = Perception.getOpens(environment, COL_CLOSE_L, COL_FAR_R).headOption
    pitOp match {
      case None => NONE
      case Some(x) if (x >= COL_CLOSE_L && x <= COL_CLOSE_R) => CLOSE
      case Some(x) if (x >= COL_FAR_L && x <= COL_FAR_R) => FAR
      case _ => NONE
    }
  }
}

/***
 * Perceives whether or not there is a pit directly below Mario.
 */
case object PitBelow extends BoolPerception(8) {
  val COL_L = 0; val COL_R = 0;
  
  def apply(environment: Environment): Byte = {
    if(Perception.pitRelativeToMario(environment, COL_L, COL_R))
      1 else 0
  }
}

/***
 * Perceives the direction Mario is moving on the horizontal plane.
 */
case object MovingX extends BytePerception(9, 2) {
  val STILL: Byte = 0; val LEFT: Byte = 1; val RIGHT: Byte = 2;

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

/***
 * Perceives the direction Mario is moving on the vertical plane.
 */
case object MovingY extends BytePerception(10, 2) {  
  val STILL: Byte = 0; val DOWN: Byte = 1; val UP: Byte = 2;

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
  
  /***
   * Extractor object, matches the perception on index.
   * Allows for code:
   * n: Int match {
   *  case Perception(perception) => perception...
   *  ...
   * }
   */
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
  
  /***
   * Checks if there is an enemy in a box relative to Mario.
   */
  def enemyInBoxRelativeToMario(environment: Environment, a: (Int, Int), b: (Int, Int)): Boolean = {
    val enemies = environment.getEnemiesObservationZ(2); //Z-index 2 gives 1 for enemy, 0 for anything else
    val test = (grid: Array[Array[Byte]], tup: Tuple2[Int, Int]) => {
      val x = grid(tup._1)(tup._2)
      x == 1
    }
    
    checkBox(enemies, test, getMarioPos(environment), a, b)
  }
  
  /***
   * Checks if there is an obstacle (rise in terrain) in a box relative to Mario.
   */
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

  /***
   * Checks if there is an step (fall then rise in terrain) in a box relative to Mario.
   */
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
  
  /***
   * Checks whether a box is filled with terrain, realtive to Mario
   */
  def obstacleFillBoxRelativeToMario(environment: Environment, a: (Int, Int), b: (Int, Int)): Boolean = {
    val level = environment.getLevelSceneObservationZ(2); //
    val test = (grid: Array[Array[Byte]], tup: Tuple2[Int, Int]) => {
    	val x = grid(tup._1)(tup._2)
      !obs(x)
    }      

    checkBox(level, test, getMarioPos(environment), a, b, false)
    
  }
  
  /***
   * Checks if there is an pit in columns relative to Mario.
   */
  def pitRelativeToMario(environment: Environment, a: Int, b: Int): Boolean = {
    val opens = this.getOpens(environment, a, b)
    !opens.isEmpty
  }
  
  /***
   * Helper function for box checks
   */
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
  
  /***
   * Gets open columns below Mario
   */
  def getOpens(environment: Environment, a: Int, b: Int): List[Int] = {
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
  
  /***
   * Byte value for terrain
   */
  private def obs(b: Byte): Boolean = {
    b == 1 || b == GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH
  }
  
  /***
   * Gets Mario's position on scene.
   */
  private def getMarioPos(environment: Environment): (Int, Int) = {
    val marioPos = environment.getMarioEgoPos;
    (marioPos(EGO_POS_ROW_INDEX), marioPos(EGO_POS_COL_INDEX))
  }
}