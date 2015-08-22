package com.montywest.marioai.learning.ec.vector

import ec.EvolutionState
import ec.util.Parameter
import com.montywest.marioai.learning.ec.params.RulesetParams
import com.montywest.marioai.learning.ec.params.Condition
import com.montywest.marioai.learning.ec.params.Action
import ec.vector.VectorSpecies

class RulesetSpecies extends DynamicParameterIntegerVectorSpecies {

  var favourMutation: Array[Boolean] = Array.fill(1)(false)
  var favourByte: Array[Byte] = Array.fill(1)(0: Byte)
  var favourProbability: Array[Double] = Array.fill(1)(0.5d)
  
  
  override def setup(state: EvolutionState, base: Parameter) {
    
    setupGenome(state, base);
    
    favourMutation = Array.fill(genomeSize)(false)
    favourProbability = Array.fill(genomeSize)(0.5)
    favourByte = Array.fill(genomeSize)(0)
    

    super.setup(state, base)
  }
  
  override def dynamicParameterOverride(state: EvolutionState, base: Parameter, default: Parameter): Unit = {
    super.dynamicParameterOverride(state, base, default)

    val dpc = dynamicParamsClassOpt match {
        case Some(p) => p match {
          case p: RulesetParams => {
            if (genomeSize % p.ruleLength != 0)
              state.output.fatal("Genomesize must be a multiple of rule length (" + p.ruleLength + ")",
                                  base.push(VectorSpecies.P_GENOMESIZE),
                                  base.push(VectorSpecies.P_GENOMESIZE))
            p
          }
          case _ => {
            state.output.fatal("Dynamical parameter class must be a subclass of RulesetParams for RulesetSpecies")
            null 
          }
        }
        case None => {
          state.output.fatal("Dynamical parameter class RulesetParams is required for RulesetSpecies")
          null 
        }
      }
    
    
    if(state.parameters.exists(base.push(RulesetSpecies.P_CONDITION), default.push(RulesetSpecies.P_CONDITION))) {
      dpc.runOnIndexes(Condition, genomeSize){
        (x: Int) => loadParametersForGene(state, x, base.push(RulesetSpecies.P_CONDITION), default.push(RulesetSpecies.P_CONDITION), "")
      }
    }
    
    if(state.parameters.exists(base.push(RulesetSpecies.P_ACTION), default.push(RulesetSpecies.P_ACTION))) {
      dpc.runOnIndexes(Action, genomeSize){
        (x: Int) => loadParametersForGene(state, x, base.push(RulesetSpecies.P_ACTION), default.push(RulesetSpecies.P_ACTION), "")
      }
    }
  }
  
  override def loadParametersForGene(state: EvolutionState, index: Int, base: Parameter, default: Parameter, postfix: String): Unit = {
    super.loadParametersForGene(state, index, base, default, postfix)
    
    // Check whether using favour mutation by checking for favour probability
    if(state.parameters.exists(base.push(RulesetSpecies.P_FAVOUR_PROBABILITY), default.push(RulesetSpecies.P_FAVOUR_PROBABILITY))) {
      favourMutation(index) = true;
      
      //Get favoured byte from params, if not found then get from dynamic params class
      val b: Byte = {
        if (state.parameters.exists(base.push(RulesetSpecies.P_FAVOUR_BYTE), default.push(RulesetSpecies.P_FAVOUR_BYTE))) {
    			dynamicParamsClassOpt match {
      			case Some(p: RulesetParams) => {
      				p.getIndexType(index) match {
        				case Condition => p.favouredCondition
        				case Action => p.favouredAction
      				}
      			}
      			case _ => {
      				state.output.fatal("Dynamic paramater class RulesetParams is required for RulesetSpecies")
              -1
      			}
    			}
    		} else state.parameters.getInt(base.push(RulesetSpecies.P_FAVOUR_BYTE), default.push(RulesetSpecies.P_FAVOUR_BYTE)).toByte
      }
      
      //Get favour probability from params file
      val d: Double = state.parameters.getDouble(base.push(RulesetSpecies.P_FAVOUR_PROBABILITY), default.push(RulesetSpecies.P_FAVOUR_PROBABILITY), -1.0)
      if (d == -1.0) {
        state.output.fatal(RulesetSpecies.P_FAVOUR_BYTE + " specified without " + RulesetSpecies.P_FAVOUR_PROBABILITY, 
                            base.push(RulesetSpecies.P_FAVOUR_PROBABILITY), 
                            default.push(RulesetSpecies.P_FAVOUR_PROBABILITY))
      } else if (d > 1.0 || d < 0.0) {
        state.output.fatal(RulesetSpecies.P_FAVOUR_PROBABILITY + " must be between 0.0 and 1.0 inclusive", 
                            base.push(RulesetSpecies.P_FAVOUR_PROBABILITY), 
                            default.push(RulesetSpecies.P_FAVOUR_PROBABILITY))
      }
      favourByte(index) = b
      favourProbability(index) = d
    } else {
      favourMutation(index) = false
    }
  }
  
}

object RulesetSpecies {
  val P_CONDITION = "condition"
  val P_ACTION = "action"
  val P_FAVOUR_BYTE = "favour_byte"
  val P_FAVOUR_PROBABILITY = "favour_probability"
}