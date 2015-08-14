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
    new SystemOfValuesAdapter(values.distance,
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
    MWEvaluationMultipliers(1, 1024, 32, 16, 64, 42, 4, 17, 12, 58, 8, 24, 58, 10)
}
