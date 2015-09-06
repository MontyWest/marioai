package com.montywest.marioai.learning.ec.eval

import scala.annotation.tailrec

import com.montywest.marioai.agents.MWRulesetAgent
import com.montywest.marioai.agents.MWRulesetAgent
import com.montywest.marioai.agents.MWRulesetAgent
import com.montywest.marioai.agents.MWRulesetAgent
import com.montywest.marioai.agents.MWRulesetAgent
import com.montywest.marioai.learning.ec.params.EvaluationParamsUtil
import com.montywest.marioai.rules.KeyRight
import com.montywest.marioai.rules.KeySpeed
import com.montywest.marioai.rules.MWAction
import com.montywest.marioai.rules.Ruleset
import com.montywest.marioai.task.EvaluationTask
import com.montywest.marioai.task.MWEvaluationMultipliers
import com.montywest.marioai.task.MWEvaluationTask
import com.montywest.marioai.task.MWLevelOptions

import ch.idsia.agents.Agent
import ec.EvolutionState
import ec.Individual
import ec.Problem
import ec.simple.SimpleFitness
import ec.simple.SimpleProblemForm
import ec.util.Parameter
import ec.vector.ByteVectorIndividual

class AgentRulesetEvaluator extends Problem with SimpleProblemForm {

  private var _taskSeeds: Vector[Int] = Vector(0);
  def taskSeeds = _taskSeeds
  
  private var _numberOfLevels: Int = 0
  def numberOfLevels = _numberOfLevels
  
  private var _evalMults: MWEvaluationMultipliers = MWEvaluationMultipliers.zeroEvaluationMultipliers
  def evalMults = _evalMults
  
  private var _baseLevelOptions: MWLevelOptions = MWLevelOptions.defaultOptions
  def baseLevelOptions = _baseLevelOptions
  private var _updateOptionsFunc: (Int, MWLevelOptions) => MWLevelOptions = MWLevelOptions.noUpdate
  def updateOptionsFunc = _updateOptionsFunc
  
  private var _fallbackAction: MWAction = MWAction()
  def fallbackAction = _fallbackAction
  
  private var task: Option[EvaluationTask] = None
  
  override def prepareToEvaluate(state: EvolutionState, thread: Int): Unit = {
    if (task.isEmpty) {
      task = Some(
          MWEvaluationTask(numberOfLevels, evalMults, baseLevelOptions, updateOptionsFunc, false)
            .withLevelSeed(_taskSeeds(state.generation))
      )
    }
  }
  
  private def buildIndAgentName(state: EvolutionState, individual: Individual, subpop: Int, thread: Int): String = {
    return "IND-thread:" +  thread + "-gen:" + state.generation
  }
  
  override def evaluate(state: EvolutionState, individual: Individual, subpop: Int, thread: Int): Unit = {
    try {
      individual match {
        case ind: ByteVectorIndividual => {
          if (task.isDefined) {
            val evalTask = task.get
            val name = this.buildIndAgentName(state, individual, subpop, thread)
            val ruleset: Ruleset = Ruleset.buildFromArray(ind.genome, fallbackAction)
            val agent: Agent = MWRulesetAgent(name, ruleset)
            
            val iFitness = evalTask.withAgent(agent)
                                   .withLevelSeed(_taskSeeds(state.generation))
                                   .evaluate
            
//            state.output.message("Evalled: " + iFitness)
            ind.fitness match {
              case _: SimpleFitness => {
                ind.fitness.asInstanceOf[SimpleFitness].setFitness(state, iFitness.toDouble, false)
                ind.evaluated = true
              }
              case _ => {
                state.output.fatal("This evaluator (EvolvedAgentRulesetEvaluator) requires a individuals to have SimpleFitness")
              }
            }
          } else {
            state.output.fatal("Task was not defined when evaluating individual, implying prepareToEvaluate was not run on this instance.")
          }
        }
        case _ => {
          state.output.fatal("This evaluator (EvolvedAgentRulesetEvaluator) requires a ByteVectorIndividual")
        }
      }
    } catch {
      case e: Exception => {
    	  e.printStackTrace()
        state.output.fatal("Exception thrown in evaluator: " + e + " " + e.getMessage)
      }
    }
    
  }
  
  override def setup(state: EvolutionState, base: Parameter): Unit = {
    import AgentRulesetEvaluator._
    
    val default = defaultBase
    
    // SEEDS
    if (!state.parameters.exists(base.push(P_SEED).push(P_SEED_START), default.push(P_SEED).push(P_SEED_START))) {
      state.output.fatal(P_SEED + P_SEED_START + " must be defined in problem.",
          base.push(P_SEED).push(P_SEED_START), default.push(P_SEED).push(P_SEED_START))
    }
    
    val levelSeeds = EvaluationParamsUtil.getLevelSeeds(state.parameters, base.push(P_SEED), 0, 0, 1)
    
    val seedStart = levelSeeds._1
    val seedAdd = levelSeeds._2
    val seedMult = levelSeeds._3

    var prevSeed = seedStart; 
    def memmedTS(g: Int): Int = {
      val ns = prevSeed + seedAdd + (g*seedMult)
      prevSeed = ns
      ns
    }
    	
    _taskSeeds = Vector.tabulate(state.numGenerations)(memmedTS)
    
    
    //NumOfLevels
    _numberOfLevels = EvaluationParamsUtil.getNumberOfLevels(state.parameters, base.push(P_LEVEL)) match {
      case None => state.output.fatal(EvaluationParamsUtil.P_NUM_OF_LEVELS + " must be defined in params file", base.push(P_LEVEL)); 0
      case Some(x) => x
    }  
    
    //EvalMults
    _evalMults = EvaluationParamsUtil.getEvaluationMutlipliers(state.parameters, base.push(P_MULTS))
            
    //LevelOptions
    _baseLevelOptions  = EvaluationParamsUtil.getBaseLevelOptions(state.parameters, base.push(P_LEVEL))
    _updateOptionsFunc = EvaluationParamsUtil.getUpdateLevelOptionsFunction(state.parameters, base.push(P_LEVEL), numberOfLevels)

    
    //Fallback
    _fallbackAction = {
      if (state.parameters.exists(base.push(P_FALLBACK), default.push(P_FALLBACK))) {
        MWAction.build(
          state.parameters.getBoolean(base.push(P_FALLBACK).push(P_ACTION_LEFT), default.push(P_FALLBACK).push(P_ACTION_LEFT), false),
          state.parameters.getBoolean(base.push(P_FALLBACK).push(P_ACTION_RIGHT), default.push(P_FALLBACK).push(P_ACTION_RIGHT), false),
          state.parameters.getBoolean(base.push(P_FALLBACK).push(P_ACTION_JUMP), default.push(P_FALLBACK).push(P_ACTION_JUMP), false),
          state.parameters.getBoolean(base.push(P_FALLBACK).push(P_ACTION_SPEED), default.push(P_FALLBACK).push(P_ACTION_SPEED), false)
        )
      } else {
        defaultFallbackAction
      }
    }

    super.setup(state, base)
  }
  
  /***
   * Should suffice to share, as all fields are internally immutable.
   */
  override def clone: Object = { 
    val prob = super.clone().asInstanceOf[AgentRulesetEvaluator]
    prob._taskSeeds = this.taskSeeds
    prob._numberOfLevels = this.numberOfLevels
    prob._baseLevelOptions = this.baseLevelOptions.clone
    prob._updateOptionsFunc = this.updateOptionsFunc
    prob._evalMults = this.evalMults.clone
    
    prob
  }

  
}

object AgentRulesetEvaluator {
  
  val P_LEVEL = "level"
  val P_SEED = "seed"
  val P_SEED_START = "start"
  val P_SEED_ADD = "add"
  val P_SEED_MULT = "mult"
  val P_MULTS = "mults"
  val P_FALLBACK = "fallback-action"
  val P_ACTION_LEFT = "left"
  val P_ACTION_RIGHT = "right"
  val P_ACTION_JUMP = "jump"
  val P_ACTION_SPEED = "speed"
  
  private val defaultFallbackAction = MWAction(KeyRight, KeySpeed)
  
}