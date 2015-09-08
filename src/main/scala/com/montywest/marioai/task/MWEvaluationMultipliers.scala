package com.montywest.marioai.task

import scala.language.implicitConversions
import ch.idsia.benchmark.tasks.SystemOfValues

/***
 * Multipliers for several level playing
 * statistics.
 * Comments describe the statistic.
 * For example, if Mario completes the level
 * It will be win * 1, otherwise it will win * 0
 */
class MWEvaluationMultipliers(
  val distance: Int, // Distance travelled by Mario in pixels (16 pixels to a block)
  val win: Int,      // 1 for level complete, 0 otherwise
  val mode: Int,     // Mario's final mode on completion or death, 2-fire, 1-big, 0-small
  val coins: Int,    // Number of coins collected
  val flowerFire: Int, // Number fire flowers collected
  val kills: Int,    // Number of enemy kills
  val killedByFire: Int, // Number of kills by fireball
  val killedByShell: Int, // Number of kills by shell
  val killedByStomp: Int, // Number of kills by stomp
  val mushroom: Int, // Number of mushrooms collected
  val timeLeft: Int, // Mario seconds left on completion, 0 if level not completed
  val hiddenBlock: Int, // Number of hidden blocks hit
  val greenMushroom: Int, // Number of green mushrooms 
  val stomp: Int) // Unused
  {
  
  override def clone: MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }
  
  def withDistance(distance: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withWin(win: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withMode(mode: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withCoins(coins: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withFlowerFire(flowerFire: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withKills(kills: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withKilledByFire(killedByFire: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withKilledByShell(killedByShell: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withKilledByStomp(killedByStomp: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withMushroom(mushroom: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withTimeLeft(timeLeft: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withHiddenBlock(hiddenBlock: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withGreenMushroom(greenMushroom: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  def withStomp(stomp: Int): MWEvaluationMultipliers = {
    new MWEvaluationMultipliers(distance, win, mode, coins, flowerFire, kills, killedByFire, killedByShell, killedByStomp, mushroom, timeLeft, hiddenBlock, greenMushroom, stomp)
  }

  override def toString: String = {
    "Evaluation Multipliers :-\n" +
    "    distance: " + distance + "\n" +
    "    win: " + win + "\n" +
    "    mode: " + mode + "\n" +
    "    coins: " + coins + "\n" +
    "    flowerFire: " + flowerFire + "\n" +
    "    kills: " + kills + "\n" +
    "    killedByFire: " + killedByFire + "\n" +
    "    killedByShell: " + killedByShell + "\n" +
    "    killedByStomp: " + killedByStomp + "\n" +
    "    mushroom: " + mushroom + "\n" +
    "    timeLeft: " + timeLeft + "\n" +
    "    hiddenBlock: " + hiddenBlock + "\n" +
    "    greenMushroom: " + greenMushroom + "\n" +
    "    stomp: " + stomp + "\n"
  }
  
  override def equals(obj: Any): Boolean = {
    obj match {
      case other: MWEvaluationMultipliers => {
        if (other == null) false
        (distance == other.distance &&
        win == other.win &&
        mode == other.mode &&
        coins == other.coins &&
        flowerFire == other.flowerFire &&
        mushroom == other.mushroom &&
        kills == other.kills &&
        killedByFire == other.killedByFire &&
        killedByShell == other.killedByShell &&
        killedByStomp == other.killedByStomp &&
        timeLeft == other.timeLeft &&
        hiddenBlock == other.hiddenBlock &&
        greenMushroom == other.greenMushroom &&
        stomp == other.stomp)
      }
      case _ => false
    }
  } 
}

object MWEvaluationMultipliers {
  implicit def toAdapter(values: MWEvaluationMultipliers): SystemOfValuesAdapter = {
    new SystemOfValuesAdapter(
      values.distance,
      values.win,
      values.mode,
      values.coins,
      values.flowerFire,
      values.mushroom,
      values.kills,
      values.killedByFire,
      values.killedByShell,
      values.killedByStomp,
      values.timeLeft,
      values.hiddenBlock,
      values.greenMushroom,
      values.stomp)
  }

  val defaultEvaluationMultipliers = 
    new MWEvaluationMultipliers(
        1,    //Distance
        2048, //Win
        16,   //Mode
        16,   //Coins
        64,   //FlowerFire
        58,   //Mushroom
        42,   //Kills
        4,    //KilledByFire
        17,   //KilledByShell
        12,   //KilledByStomp
        8,    //TimeLeft
        24,   //HiddenBlock
        58,   //GreenMushroom
        10    //Stomp
    )   
  
  val compEvaluationMulipliers = defaultEvaluationMultipliers
  
  val zeroEvaluationMultipliers =
    new MWEvaluationMultipliers(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
}
