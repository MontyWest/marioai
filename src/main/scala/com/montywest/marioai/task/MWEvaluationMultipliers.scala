package com.montywest.marioai.task

import scala.language.implicitConversions
import ch.idsia.benchmark.tasks.SystemOfValues

class MWEvaluationMultipliers(
  val distance: Int,
  val win: Int,
  val mode: Int,
  val coins: Int,
  val flowerFire: Int,
  val kills: Int,
  val killedByFire: Int,
  val killedByShell: Int,
  val killedByStomp: Int,
  val mushroom: Int,
  val timeLeft: Int,
  val hiddenBlock: Int,
  val greenMushroom: Int,
  val stomp: Int) {
  
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
    new MWEvaluationMultipliers(1, 2048, 16, 16, 64, 58, 42, 4, 17, 12, 8, 24, 58, 10)
  
  val compEvaluationMulipliers = defaultEvaluationMultipliers
  
  val zeroEvaluationMultipliers =
    new MWEvaluationMultipliers(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
}
