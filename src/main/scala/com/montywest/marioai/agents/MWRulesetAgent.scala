package com.montywest.marioai.agents

import ch.idsia.agents.Agent
import com.montywest.marioai.rules.Ruleset
import com.montywest.marioai.rules.ExAction

abstract class MWRulesetAgent(val ruleset: Ruleset) extends MWObservationAgent with Agent {
  
  override def getAction: ExAction = {
    ruleset.getBestExAction(observation)
  }
  
}