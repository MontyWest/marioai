package com.montywest.marioai.agents

import ch.idsia.agents.Agent
import com.montywest.marioai.rules.Ruleset
import com.montywest.marioai.rules.ExAction

class MWRulesetAgent(var name: String, val ruleset: Ruleset) extends MWObservationAgent with Agent {
  
  def getName = name
  def setName(name: String) = this.name = name
  
  override def getAction: ExAction = {
    ruleset.getBestExAction(observation)
  }
    
} 

object MWRulesetAgent {
  def apply(name: String, ruleset: Ruleset): MWRulesetAgent = {
    new MWRulesetAgent(name, ruleset)
  }
}