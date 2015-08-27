package com.montywest.marioai.agents

import com.montywest.marioai.rules.PitBelow
import com.montywest.marioai.rules.KeyRight
import com.montywest.marioai.rules.Ruleset
import com.montywest.marioai.rules.OnGround
import com.montywest.marioai.rules.KeySpeed
import com.montywest.marioai.rules.KeyJump
import com.montywest.marioai.rules.Rule

object AgentWriter extends App {

  val ruleset: Ruleset =
    Ruleset(
      Seq(
        Rule.build(Map(PitBelow -> PitBelow.TRUE), Set(KeyJump, KeyRight, KeySpeed)),

        Rule.build(Map(OnGround -> OnGround.TRUE), Set(KeyRight, KeySpeed))),
      Set(KeyRight))

  MWRulesetAgentIO.toFile(args(0), new MWRulesetAgent(args(0), ruleset), true)
  
//  MWRulesetAgentIO.toFile("hard-998", MWRulesetAgentIO.fromFile(args(0)), true)
  
}