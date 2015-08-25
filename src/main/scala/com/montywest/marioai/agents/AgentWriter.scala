package com.montywest.marioai.agents

import com.montywest.marioai.rules.EnemyLowerRight
import com.montywest.marioai.rules.EnemyUpperRight
import com.montywest.marioai.rules.JumpAvailable
import com.montywest.marioai.rules.KeyJump
import com.montywest.marioai.rules.KeyLeft
import com.montywest.marioai.rules.KeyRight
import com.montywest.marioai.rules.KeySpeed
import com.montywest.marioai.rules.MarioMode
import com.montywest.marioai.rules.MovingX
import com.montywest.marioai.rules.MovingY
import com.montywest.marioai.rules.ObstacleAhead
import com.montywest.marioai.rules.OnGround
import com.montywest.marioai.rules.PitAhead
import com.montywest.marioai.rules.PitBelow
import com.montywest.marioai.rules.Rule
import com.montywest.marioai.rules.Ruleset

object AgentWriter extends App {

  val ruleset: Ruleset =
    Ruleset(
      Seq(
        Rule.build(Map(PitBelow -> PitBelow.TRUE), Set(KeyJump, KeyRight, KeySpeed)),

        Rule.build(Map(OnGround -> OnGround.TRUE), Set(KeyRight, KeySpeed))),
      Set(KeyRight))

  MWRulesetAgentIO.toFile(args(0), new MWRulesetAgent(args(0), ruleset), true)

}