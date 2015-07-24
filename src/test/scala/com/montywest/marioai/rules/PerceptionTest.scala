package com.montywest.marioai.rules

import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import ch.idsia.benchmark.mario.environments.Environment
import org.scalatest.Matchers
import ch.idsia.benchmark.mario.engine.sprites.Mario
import com.montywest.marioai.fixtures.EnvironmentFixtures
import com.montywest.marioai.util.RandomUtils
import com.montywest.marioai.util.PrintUtils
import org.scalatest.BeforeAndAfterAll

class PerceptionTest extends FlatSpec with Matchers with MockFactory with BeforeAndAfterAll {

	info("Starting...")
  var envStub = stub[Environment]
  
  override def beforeAll {
    envStub = stub[Environment]
  } 
  
  
  "MarioMode apply" should "return 0 for small Mario" in {
    
    (envStub.getMarioStatus _) when() returns(0)
    
    assertResult(0: Byte) {
      MarioMode(envStub)
    }
    assert(MarioMode.SMALL == (0: Byte))
  }
  
  "MarioMode apply" should "return 1 for Large Mario" in {
    
    (envStub.getMarioStatus _) when() returns(1)
    
    assertResult(1: Byte) {
      MarioMode(envStub)
    }
    assert(MarioMode.LARGE == (1: Byte))
  }
  
  "MarioMode apply" should "return 2 for FIRE Mario" in {
    
    (envStub.getMarioStatus _) when() returns(2)
    
    assertResult(2: Byte) {
      MarioMode(envStub)
    }
    assert(MarioMode.FIRE == (2: Byte))
  }
  
  "JumpAvailable apply" should "return 1 for true" in {
    
    (envStub.isMarioAbleToJump _) when() returns(true)
    
    assertResult(1: Byte) {
      JumpAvailable(envStub)
    }
  }
  
  "JumpAvailable apply" should "return 0 for false" in {
    
    (envStub.isMarioAbleToJump _) when() returns(false)
    
    assertResult(0: Byte) {
      JumpAvailable(envStub)
    }
  }
  
  "OnGround apply" should "return 1 for true" in {
    
    (envStub.isMarioOnGround _) when() returns(true)
    
    assertResult(1: Byte) {
      OnGround(envStub)
    }
  }
  
  "OnGround apply" should "return 0 for false" in {
    
    (envStub.isMarioOnGround _) when() returns(false)
    
    assertResult(0: Byte) {
      OnGround(envStub)
    }
  }
  
  "MovingX apply" should "return 0 for not moving" in {
    (envStub.getMarioMovement _) when() returns(Array(0, 1))
    
    assertResult(0: Byte) {
      MovingX(envStub)
    }
    assert(MovingX.STILL == (0: Byte))
  }
  
  "MovingX apply" should "return 1 for moving left" in {
    (envStub.getMarioMovement _) when() returns(Array(-1, 0))
    
    assertResult(1: Byte) {
      MovingX(envStub)
    }
    assert(MovingX.LEFT == (1: Byte))
  }
  
  "MovingX apply" should "return 2 for moving right" in {
    (envStub.getMarioMovement _) when() returns(Array(1, -1))
    
    assertResult(2: Byte) {
      MovingX(envStub)
    }
    assert(MovingX.RIGHT == (2: Byte))
  }
  
  "MovingY apply" should "return 0 for not moving" in {
    (envStub.getMarioMovement _) when() returns(Array(1, 0))
    
    assertResult(0: Byte) {
      MovingY(envStub)
    }
    assert(MovingY.STILL == (0: Byte))
  }
  
  "MovingY apply" should "return 1 for moving down" in {
    (envStub.getMarioMovement _) when() returns(Array(-1, -1))
    
    assertResult(1: Byte) {
      MovingY(envStub)
    }
    assert(MovingY.DOWN == (1: Byte))
  }
  
  "MovingY apply" should "return 2 for moving up" in {
    (envStub.getMarioMovement _) when() returns(Array(0, 1))
    
    assertResult(2: Byte) {
      MovingY(envStub)
    }
    assert(MovingY.UP == (2: Byte))
  }
  
  "EnemyLeft apply" should "return 1 if enemy in range to the left of mario" in {
    val enemy = RandomUtils.randomIntTupleInclusive(EnemyLeft.AREA_UL, EnemyLeft.AREA_BR)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      EnemyLeft(envStub)
    }
  }
  
  "EnemyLeft apply" should "return 0 if enemy too far to the left of mario" in {
    val enemy = (EnemyLeft.AREA_UL._1 + 1, EnemyLeft.AREA_UL._2 - 1) //One Down, One Left
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      EnemyLeft(envStub)
    }
  }
  
  "EnemyLeft apply" should "return 0 if enemy is to right of mario" in {
    val enemy = (0, 2)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      EnemyLeft(envStub)
    }
  }
  
  "EnemyUpperRight apply" should "return 1 if enemy in range to the top right of mario part 1" in {
    val enemy = RandomUtils.randomIntTupleInclusive(EnemyUpperRight.AREA_1_UL, EnemyUpperRight.AREA_1_BR)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      EnemyUpperRight(envStub)
    }
  }
  
  "EnemyUpperRight apply" should "return 1 if enemy in range to the top right of mario part 2" in {
    val enemy = RandomUtils.randomIntTupleInclusive(EnemyUpperRight.AREA_2_UL, EnemyUpperRight.AREA_2_BR)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      EnemyUpperRight(envStub)
    }
  }

  "EnemyUpperRight apply" should "return 0 if enemy is lower right of Mario" in {
    val enemy = (1, 1)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      EnemyUpperRight(envStub)
    }
  }
  
  "EnemyUpperRight apply" should "return 0 if enemy is to the left mario" in {
    val enemy = (0,-1)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      EnemyUpperRight(envStub)
    }
  }
  
  "EnemyLowerRight apply" should "return 1 if enemy in range to the top right of mario part 1" in {
    val enemy = RandomUtils.randomIntTupleInclusive(EnemyLowerRight.AREA_1_UL, EnemyLowerRight.AREA_1_BR)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      EnemyLowerRight(envStub)
    }
  }
  
  "EnemyLowerRight apply" should "return 1 if enemy in range to the top right of mario part 2" in {
    val enemy = RandomUtils.randomIntTupleInclusive(EnemyLowerRight.AREA_2_UL, EnemyLowerRight.AREA_2_BR)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      EnemyLowerRight(envStub)
    }
  }

  "EnemyLowerRight apply" should "return 0 if enemy is upper right of Mario" in {
    val enemy = (-1, -1)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      EnemyLowerRight(envStub)
    }
  }
  
  "EnemyLowerRight apply" should "return 0 if enemy is to the left mario" in {
    val enemy = (0,-1)
    (envStub.getEnemiesObservationZ _) when(*) returns(EnvironmentFixtures.getEnemyScene(enemy))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      EnemyLowerRight(envStub)
    }
  }
  
  "PitAhead apply" should "return 1 if there is a pit in range ahead of mario" in {
    val pitCols = Set(2, 3);
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getLevelSceneWithPit(1, pitCols))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      PitAhead(envStub)
    }
  }
  
  "PitAhead apply" should "return 1 if mario below terrain" in {
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.baseLevelScene(-2))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      PitAhead(envStub)
    }
  }
  
  "PitAhead apply" should "return 0 if there are no pits on screen" in {
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.baseLevelScene(1))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      PitAhead(envStub)
    }
  }
  
  "PitAhead apply" should "return 0 if there are only pits behind mario" in {
    val pitCols = Set(-7, -6, -3, -2, -1)
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getLevelSceneWithPit(3, pitCols))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      PitAhead(envStub)
    }
  }
  
  "PitBelow apply" should "return 1 if there is a pit below mario" in {
    val pitCols = Set(0, 1)
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getLevelSceneWithPit(3, pitCols))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      PitBelow(envStub)
    }
  }
  
  "PitBelow apply" should "return 0 if there is a pit too far ahead of mario" in {
    val pitCols = Set(PitBelow.COL_R+2, PitBelow.COL_R+3, PitBelow.COL_R+4)
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getLevelSceneWithPit(1, pitCols))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      PitBelow(envStub)
    }
  }
  
  "PitBelow apply" should "return 0 if there is a pit too far behind of mario" in {
    val pitCols = Set(PitBelow.COL_L-2, PitBelow.COL_L-3, PitBelow.COL_L-4)
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getLevelSceneWithPit(1, pitCols))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      PitBelow(envStub)
    }
  }
  
  "ObstacleAhead apply" should "return 1 if there is terrain in range to right of mario" in {
    val groundLevels = Set(1, 2); 
    val additions = Set((0,RandomUtils.randomIntIn(ObstacleAhead.AREA_OBS_UL._2, ObstacleAhead.AREA_OBS_BR._2)))
    
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getCustomLevelScene(groundLevels, Set.empty, additions))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      ObstacleAhead(envStub)
    }
  }
  
  "ObstacleAhead apply" should "return 0 if there is terrain to left of mario" in {
    val groundLevels = Set(1, 2); 
    val additions = Set((0, -1))
    
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getCustomLevelScene(groundLevels, Set.empty, additions))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      ObstacleAhead(envStub)
    }
  }
  
  "ObstacleAhead apply" should "return 0 if only terrain obstacles are too high above mario" in {
    val groundLevels = Set(1, 2); 
    val additions = Set((ObstacleAhead.AREA_OBS_UL._1-1 ,RandomUtils.randomIntIn(ObstacleAhead.AREA_OBS_UL._2, ObstacleAhead.AREA_OBS_BR._2)))
    
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getCustomLevelScene(groundLevels, Set.empty, additions))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      ObstacleAhead(envStub)
    }
  }
  
  "ObstacleAhead apply" should "return 0 if only terrain falls away" in {
    val groundLevels = Set(2,3); 
    val additions = Set((1,1), (1,0), (1,-1)) //Mario on small platform
    
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getCustomLevelScene(groundLevels, Set.empty, additions))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(0: Byte) {
      ObstacleAhead(envStub)
    }
  }
  
  "ObstacleAhead apply" should "return 1 if there is terrain falls and rises to right of mario (is stepped)" in {
    val groundLevels = Set(1, 2); 
    val randomIntInColRange = RandomUtils.randomIntEx(ObstacleAhead.AREA_OBS_UL._2, ObstacleAhead.AREA_OBS_BR._2)
    val removals = Set((1,randomIntInColRange), (1,randomIntInColRange+1)) //Small dip infront of mario
        
    (envStub.getLevelSceneObservationZ _) when(*) returns(EnvironmentFixtures.getCustomLevelScene(groundLevels, removals, Set.empty))
    (envStub.getMarioEgoPos _) when() returns(Array(EnvironmentFixtures.mario._1, EnvironmentFixtures.mario._2))

    assertResult(1: Byte) {
      ObstacleAhead(envStub)
    }
  }
  
  
}