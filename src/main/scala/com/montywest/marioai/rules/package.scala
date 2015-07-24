package com.montywest.marioai

package object rules {
  
	type Observation = Vector[Byte]
  object Observation {
    import ch.idsia.benchmark.mario.environments.Environment
    
    def apply(environment: Environment): Observation = {
      Vector.tabulate(Perception.NUMBER_OF_PERCEPTIONS)
        {n: Int => n match {
            case Perception(perception) => perception(environment)
            case _ => throw new IllegalArgumentException
          }
        }
    }

    val BLANK_OBSERVATION: Observation  = Vector.fill(Perception.NUMBER_OF_PERCEPTIONS)(0)    
  }
  
  type Conditions = Vector[Byte]
  
  
  type Action = Array[Boolean]
  object Action {
    import ch.idsia.benchmark.mario.environments.Environment
    import ch.idsia.benchmark.mario.engine.sprites.Mario
    
    def apply(keys: KeyPress*): Action = {
      val ac: Action = Array.fill(Environment.numberOfKeys)(false)
      keys.foreach { 
        case KeyLeft => ac(Mario.KEY_LEFT) = true;
        case KeyRight => ac(Mario.KEY_RIGHT) = true;
        case KeyJump => ac(Mario.KEY_JUMP) = true;
        case KeySpeed => ac(Mario.KEY_SPEED) = true;
      }
      ac
    }
  }
  
  
  type RuleSet = List[Rule]

  
}