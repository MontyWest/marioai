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
  object Conditions {
    val DONT_CARE: Byte = -1;
    val LENGTH = Perception.NUMBER_OF_PERCEPTIONS  
    
    def validateCondition(cond: (Perception, Byte)): Boolean = cond match {
      case (bp : BoolPerception, b) => (bp.TRUE == b) || (bp.FALSE == b)
      case (ip : BytePerception, b) => (0 <= b) && (b < ip.limit)
    }
    
    def apply(conditions: Map[Perception, Byte]): Conditions = {
      if (conditions.forall(validateCondition)) {
        val perMap = conditions.map { case (p, b) => (p.index, b) }
        Vector.tabulate(Conditions.LENGTH)( (i: Int) =>  
          perMap get(i) match {
            case None => Conditions.DONT_CARE
            case Some(b: Byte) => b
          }
        )
      } else throw new IllegalArgumentException("Perception byte value out of range")
    }
  }
  
  
  type ExAction = Array[Boolean]
  object ExAction {
    import ch.idsia.benchmark.mario.environments.Environment
    import ch.idsia.benchmark.mario.engine.sprites.Mario
    
    val KEY_LEFT = Mario.KEY_LEFT
    val KEY_RIGHT = Mario.KEY_RIGHT
    val KEY_UP = Mario.KEY_UP
    val KEY_DOWN = Mario.KEY_DOWN
    val KEY_JUMP = Mario.KEY_JUMP
    val KEY_SPEED = Mario.KEY_SPEED
    
    val LENGTH = Environment.numberOfKeys
    
    
    def apply(keys: KeyPress*): ExAction = {
      val ac: ExAction = Array.fill(LENGTH)(false)
      keys.foreach { 
        case KeyLeft => ac(KEY_LEFT) = true;
        case KeyRight => ac(KEY_RIGHT) = true;
        case KeyJump => ac(KEY_JUMP) = true;
        case KeySpeed => ac(KEY_SPEED) = true;
      }
      ac
    }
    
    def build(keys: Set[KeyPress]): ExAction = {
      Array.tabulate(LENGTH)( (i: Int) => i match {
        case KEY_LEFT => keys.contains(KeyLeft)
        case KEY_RIGHT => keys.contains(KeyRight)
        case KEY_JUMP => keys.contains(KeyJump)
        case KEY_SPEED => keys.contains(KeySpeed)
        case _ => false
      })
    }
    
    implicit def apply(mwAction: MWAction): ExAction = {
      Array.tabulate(LENGTH)((x: Int) => 
        x match {
          case KEY_LEFT => MWAction.ACTION_TRUE == mwAction(MWAction.LEFT_INDEX)
          case KEY_RIGHT => MWAction.ACTION_TRUE == mwAction(MWAction.RIGHT_INDEX)
          case KEY_JUMP => MWAction.ACTION_TRUE == mwAction(MWAction.JUMP_INDEX)
          case KEY_SPEED => MWAction.ACTION_TRUE == mwAction(MWAction.SPEED_INDEX)
          case _ => false
        }
      ) 
    }
  }
  
  type MWAction = Vector[Byte]
  object MWAction {
    val LENGTH = 4
    val LEFT_INDEX = 0;
    val RIGHT_INDEX = 1;
    val JUMP_INDEX = 2;
    val SPEED_INDEX = 3;
    
    val ACTION_TRUE: Byte = 1;
    val ACTION_FALSE: Byte = 0;
    
    def apply(keys: KeyPress*): MWAction = {
      build(keys.toSet)
    }
    
    def build(keys: Set[KeyPress]): MWAction = {
      val actIndexSet = keys.map { getKeyPressIndex }
      
      Vector.tabulate(LENGTH)( (i: Int) => 
        if (actIndexSet.contains(i)) ACTION_TRUE
        else ACTION_FALSE
      )
    }
    
    def getKeyPressIndex(kp: KeyPress): Int = kp match {
      case KeyLeft => LEFT_INDEX
      case KeyRight => RIGHT_INDEX
      case KeyJump => JUMP_INDEX
      case KeySpeed => SPEED_INDEX
    }
  }
  
}