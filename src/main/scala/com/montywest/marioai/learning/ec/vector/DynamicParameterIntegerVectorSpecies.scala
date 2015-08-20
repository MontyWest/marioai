package com.montywest.marioai.learning.ec.vector

import com.montywest.marioai.learning.ec.DynamicSpeciesParameters
import scala.annotation.tailrec
import ec.EvolutionState
import ec.vector.VectorSpecies
import ec.util.Parameter
import ec.vector.IntegerVectorSpecies

class DynamicParameterIntegerVectorSpecies extends IntegerVectorSpecies {  

  var dynamicParamsClassOpt: Option[DynamicSpeciesParameters] = None;
  
  override def setup(state: EvolutionState, base: Parameter): Unit = {
    val default = defaultBase
    
    if (dynamicParamsClassOpt.isEmpty && state.parameters.exists(base.push(DynamicParameterIntegerVectorSpecies.P_DYNAMIC_PARAMETER_CLASS), default.push(DynamicParameterIntegerVectorSpecies.P_DYNAMIC_PARAMETER_CLASS))) {
      val dynamicParamsClassOpt: Option[DynamicSpeciesParameters] =
        state.parameters.getInstanceForParameter(
              base.push(DynamicParameterIntegerVectorSpecies.P_DYNAMIC_PARAMETER_CLASS),
              default.push(DynamicParameterIntegerVectorSpecies.P_DYNAMIC_PARAMETER_CLASS),
              classOf[DynamicSpeciesParameters]) match {
        case obj: DynamicSpeciesParameters => Some(obj)
        case _ => state.output.fatal("Dynamical parameter class wasn't a subclass of DynamicSpeciesParameters",
                                      base.push(DynamicParameterIntegerVectorSpecies.P_DYNAMIC_PARAMETER_CLASS),
                                      default.push(DynamicParameterIntegerVectorSpecies.P_DYNAMIC_PARAMETER_CLASS))
                  None
        }
    }
    
    super.setup(state, base)
  }
  
  override def dynamicParameterOverride(state: EvolutionState, base: Parameter, default: Parameter): Unit = {
       
    if (dynamicParamsClassOpt.isDefined) {
      val dynamicParamsClass = dynamicParamsClassOpt.get
      
      if (dynamicParamsClass.minGene.isDefined) {
        fill(minGene, dynamicParamsClass.minGene.get)
      }
      if (dynamicParamsClass.maxGene.isDefined) {
        fill(minGene, dynamicParamsClass.maxGene.get)
      }
      if (dynamicParamsClass.mutationProb.isDefined) {
        fill(mutationProbability, dynamicParamsClass.mutationProb.get)
      }
      
      if (dynamicParamsClass.moduloNum.isDefined) {
        val moduloNum = dynamicParamsClass.moduloNum.get
        
        if (moduloNum == 0)
          state.output.fatal(
                      "[Dynamic Params] Modulo by zero is undefined. moduloNum return must be non-zero.");
        
        if ((genomeSize % moduloNum) != 0)
          state.output.fatal(
               "[Dynamic Params] moduloNum must divide genome size.");
        
        dynamicGenomeByModuloIndices(state, dynamicParamsClass, moduloNum);
      }
      
      if (dynamicParamsClass.numSegments.isDefined) {
        val numSegments = dynamicParamsClass.numSegments.get
        if(numSegments < 0)
              state.output.fatal(
                  "[Dynamic Params] Invalid number of genome segments: " + numSegments
                  + "\nIt must be a nonnegative value.");
        
        val segmentType = dynamicParamsClass.segmentType.getOrElse(VectorSpecies.P_SEGMENT_START)
        
        if(segmentType.equalsIgnoreCase(VectorSpecies.P_SEGMENT_START)) {
          this.dynamicGenomeSegmentsByStartIndices(state, dynamicParamsClass, numSegments)
        } else if(segmentType.equalsIgnoreCase(VectorSpecies.P_SEGMENT_END)) {
          this.dynamicGenomeSegmentsByEndIndices(state, dynamicParamsClass, numSegments)
        } else
            state.output.fatal(
                "[Dynamic Params] Invalid specification of genome segment type: " + segmentType
                + "\nThe segmentType must have the value of " + VectorSpecies.P_SEGMENT_START + " or " + VectorSpecies.P_SEGMENT_END);
        
      }

      for(i <- 0 until genomeSize) {
    	  if (dynamicParamsClass.minGene(i).isDefined) minGene(i) = dynamicParamsClass.minGene(i).get
        if (dynamicParamsClass.maxGene(i).isDefined) minGene(i) = dynamicParamsClass.maxGene(i).get
        if (dynamicParamsClass.mutationProb(i).isDefined) mutationProbability(i) = dynamicParamsClass.mutationProb(i).get
      }
    }
    
    super.dynamicParameterOverride(state, base, default)
  }
  
  private def dynamicGenomeByModuloIndices(state: EvolutionState, dynamicParamsClass: DynamicSpeciesParameters, moduloNum: Int): Unit = {
    for (i <- 0 until moduloNum) {
      val min = dynamicParamsClass.moduloMinGene(i)
      val max = dynamicParamsClass.moduloMaxGene(i)
      val prob = dynamicParamsClass.moduloMutationProb(i)
      if (min.isDefined || max.isDefined || prob.isDefined) {
        for (j <- i until genomeSize by moduloNum) {
          if (min.isDefined) minGene(j) = min.get
            if (max.isDefined) maxGene(j) = max.get
            if (prob.isDefined) mutationProbability(j) = prob.get
        }
      }
    }
  }
  
  private def dynamicGenomeSegmentsByStartIndices(state: EvolutionState, dynamicParamsClass: DynamicSpeciesParameters, numSegments: Int): Unit = {    
    @tailrec
    def inner(currentSeg: Int, previousSegEnd: Int): Unit = {
      if (currentSeg < 0) return
      val segEndOpt: Option[Int] = dynamicParamsClass.segmentStart(currentSeg); 
      if (segEndOpt.isDefined) {
        val segEnd: Int = segEndOpt.get
        
        //check if the start index is valid
        if(segEnd >= previousSegEnd || segEnd < 0)
                state.output.fatal(
                    "[Dynamic Params] Invalid start index value for segment " + currentSeg + ": " + segEnd 
                    +  "\nThe value must be smaller than " + previousSegEnd +
                    " and greater than or equal to  " + 0);
                        
        //check if the index of the first segment is equal to 0
        if(currentSeg == 0 && segEnd != 0)
            state.output.fatal(
                "[Dynamic Params] Invalid start index value for the first segment " + currentSeg + ": " + segEnd 
                +  "\nThe value must be equal to " + 0);
        
        val min = dynamicParamsClass.segmentMinGene(currentSeg)
        val max = dynamicParamsClass.segmentMaxGene(currentSeg)
        val prob = dynamicParamsClass.segmentMutationProb(currentSeg)
        if (min.isDefined || max.isDefined || prob.isDefined) {
          for(j <- segEnd until previousSegEnd) {
            if (min.isDefined) minGene(j) = min.get
            if (max.isDefined) maxGene(j) = max.get
            if (prob.isDefined) mutationProbability(j) = prob.get
          }
        }
        
        inner(currentSeg - 1, segEnd)
      } else {
        state.output.fatal("[Dynamic Params] Genome segment " + currentSeg + " has not been defined!" +
                    "\nYou must specify start indices for " + numSegments + " segment(s)")
      }
    } 
    inner(numSegments - 1, genomeSize)
  }

  private def dynamicGenomeSegmentsByEndIndices(state: EvolutionState, dynamicParamsClass: DynamicSpeciesParameters, numSegments: Int): Unit = {    
    @tailrec
    def inner(currentSeg: Int, previousSegEnd: Int): Unit = {
      if (currentSeg >= numSegments) return
      val segEndOpt: Option[Int] = dynamicParamsClass.segmentEnd(currentSeg); 
      if (segEndOpt.isDefined) {
        val segEnd: Int = segEndOpt.get
        
        //check if the end index is valid
        if(segEnd <= previousSegEnd || segEnd >= genomeSize)
                state.output.fatal(
                    "[Dynamic Params] Invalid end index value for segment " + currentSeg + ": " + segEnd 
                    +  "\nThe value must be greater than " + previousSegEnd +
                    " and smaller than " + genomeSize);
                        
        //check if the index of the final segment is equal to the genomeSize
        if(currentSeg == numSegments - 1 && segEnd != (genomeSize-1))
            state.output.fatal(
                "Invalid end index value for the last segment " + currentSeg + ": " + segEnd 
                +  "\nThe value must be equal to the index of the last gene in the genome:  " + (genomeSize-1));
                   
        
        val min = dynamicParamsClass.segmentMinGene(currentSeg)
        val max = dynamicParamsClass.segmentMaxGene(currentSeg)
        val prob = dynamicParamsClass.segmentMutationProb(currentSeg)
        if (min.isDefined || max.isDefined || prob.isDefined) {
          for(j <- (previousSegEnd+1) to segEnd) {
            if (min.isDefined) minGene(j) = min.get
            if (max.isDefined) maxGene(j) = max.get
            if (prob.isDefined) mutationProbability(j) = prob.get
          }
        }
        
        inner(currentSeg + 1, segEnd)
      } else {
        state.output.fatal("[Dynamic Params] Genome segment " + currentSeg + " has not been defined!" +
                    "\nYou must specify end indices for " + numSegments + " segment(s)")
      }
    } 
    inner(0, -1)
  }
}

object DynamicParameterIntegerVectorSpecies {
  val P_DYNAMIC_PARAMETER_CLASS: String = "dynamic-param-class";
}