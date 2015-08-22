package com.montywest.marioai.learning.ec.vector.breed

import ec.BreedingPipeline
import ec.vector.VectorDefaults
import ec.util.Parameter
import ec.Individual
import ec.EvolutionState
import com.montywest.marioai.learning.ec.vector.RulesetSpecies
import ec.vector.ByteVectorIndividual
import ec.util.MersenneTwisterFast
import scala.collection.mutable.WrappedArray

class RulesetMutationPipeline extends BreedingPipeline {
  
  override def defaultBase: Parameter = VectorDefaults.base.push(RulesetMutationPipeline.P_RULESET_MUTATION)
  override def numSources: Int =  RulesetMutationPipeline.NUM_SOURCES
  
//  var mutateMu: Boolean = false
//  
//  override def setup(state: EvolutionState, base: Parameter): Unit = {
//    val default: Parameter = defaultBase
//    if (state.parameters.exists(base.push(RulesetMutationPipeline.P_MUTATE_MU), default.push(RulesetMutationPipeline.P_MUTATE_MU))) {
//      mutateMu = state.parameters.getBoolean(base.push(RulesetMutationPipeline.P_MUTATE_MU), default.push(RulesetMutationPipeline.P_MUTATE_MU), false)
//    }
//  }
  
  override def produce(min: Int, 
                       max: Int, 
                       start: Int, 
                       subpopulation: Int,
                       inds: Array[Individual],
                       state: EvolutionState,
                       thread: Int): Int = {
    
    val source = sources(0)
        
    val n: Int = source.produce(min, max, start, subpopulation, inds, state, thread)
    
    // Check source of inds, clone if not a breeder
    val end = n+start
    source match {
      case bp: BreedingPipeline =>
      case _ => {
        for(i <- start until end) {
          inds(i) = inds(i).clone.asInstanceOf[Individual]
        }
      }
    }
    
    // CHECK INDIV AND SPECIES CLASS
    val exampleInd = inds(start)
    val rulesetSpecies: RulesetSpecies =
      (exampleInd, exampleInd.species) match {
        case (i: ByteVectorIndividual, s: RulesetSpecies) => s
        case _ => {
          state.output.fatal("RulesetMutationPipeline needs species of RulesetSpecies and individuals of ByteVectorIndividual")
          null
        }
      }
    state.output.exitIfErrors
    
    //MUTATE
    for(q <- start until end) {
      inds(q) match {
        case vecInd: ByteVectorIndividual => {
           inds(q) = this.mutateIndividual(state, thread, vecInd, rulesetSpecies)
        }
        case _ => {
          state.output.fatal("RulesetMutationPipeline needs species of RulesetSpecies and individuals of ByteVectorIndividual")
        }
      } 
    }
    
    n
  }
  
  protected def mutateIndividual(state: EvolutionState, thread: Int, vecInd: ByteVectorIndividual, species: RulesetSpecies): ByteVectorIndividual = {
    for (n <- 0 until vecInd.genome.length) {
      if (state.random(thread).nextBoolean(species.mutationProbability(n))) {
        try {
          if (species.favourMutation(n)) {
            vecInd.genome(n) = getRandomByteFavouring(
              species.favourByte(n),
              species.favourProbability(n),
              species.minGene(n).toByte,
              species.maxGene(n).toByte,
              state.random(thread)
            )
          } else {
            vecInd.genome(n) = getRandomByte(
              species.minGene(n).toByte,
              species.maxGene(n).toByte,
              state.random(thread)
            )    
          }
        } catch {
          case e: IllegalArgumentException => 
            state.output.fatal("Min is larger than max for gene " + n + ", possible overflow error.")
        }
      }
    }
    vecInd.evaluated = false
    vecInd
  }
  
  protected def getRandomByteFavouring(favour: Byte, favourProbability: Double, min: Byte, max: Byte, random: MersenneTwisterFast): Byte = {
    if (random.nextBoolean(favourProbability)) {
      favour
    } else {
      getRandomByte(min, max, random, favour)
    }
  }
  
  protected def getRandomByte(min: Byte, max: Byte, random: MersenneTwisterFast, exceptions: Byte*): Byte = {
		var b: Byte = 0
    if (min > max) throw new IllegalArgumentException()
    else if (exceptions.length == 0) {
      do {
        b = random.nextByte
        } while (b < min || b > max);
      b
    } else if (exceptions.length == 1) {
      val exception = exceptions(0);
      do {
        b = random.nextByte
        } while (b < min || b > max || b == exception);
      b
    } else {
      do {
        b = random.nextByte
        } while (b < min || b > max || exceptions.contains(b));
    }
    b
  }
}

object RulesetMutationPipeline {
  val P_RULESET_MUTATION = "ruleset-mutation";
  val NUM_SOURCES = 1;
}