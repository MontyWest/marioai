package com.montywest.marioai.task

import ch.idsia.benchmark.tasks.SystemOfValues

case class MWEvaluationMultipliers (
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
      val stomp: Int
)

object MWEvaluationMultipliers {
  implicit def toAdapter(values: MWEvaluationMultipliers): SystemOfValuesAdapter = {
    new SystemOfValuesAdapter(values)
  }
}
