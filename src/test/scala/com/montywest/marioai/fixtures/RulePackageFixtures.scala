package com.montywest.marioai.fixtures

import com.montywest.marioai.rules.Observation
import com.montywest.marioai.rules.Perception

object RulePackageFixtures {

  def getObservation(obs: Map[Perception, Byte]): Observation = {
    val map = obs.map { case (p, b) => (p.index, b) }
    Vector.tabulate(Perception.NUMBER_OF_PERCEPTIONS){ (i: Int) => 
        map get(i) match {
          case None => 0
          case Some(b: Byte) => b
      }
    }
  }
}