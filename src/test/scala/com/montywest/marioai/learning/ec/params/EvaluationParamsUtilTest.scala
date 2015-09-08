package com.montywest.marioai.learning.ec.params

import org.scalatest.BeforeAndAfterAll
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers
import org.scalatest.FlatSpec
import ec.util.ParameterDatabaseInf
import ec.util.Parameter
import org.scalamock.matchers.MockParameter
import com.montywest.marioai.task.MWEvaluationMultipliers
import com.montywest.marioai.task.MWLevelOptions

class EvaluationParamsUtilTest extends FlatSpec with Matchers with MockFactory with BeforeAndAfterAll {

  private var blankParam = new Parameter("")
  var pdStub = stub[ParameterDatabaseInf]
  
  override def beforeAll {
    pdStub = stub[ParameterDatabaseInf]
  }
  
  
  "getLevelSeeds" should "return defaults for blank database" in {
	  val base = blankParam.push(EvaluationParamsUtil.P_LEVEL_BASE)
    (pdStub.exists _) when(*, *) returns(false)
    (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_SEED_START), *, 5) returns(5)
	  (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_SEED_ADD), *, 7) returns(7)
	  (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_SEED_MULT), *, 9) returns(9)
    assertResult((5, 7, 9)) {
      EvaluationParamsUtil.getLevelSeeds(pdStub, base, 5, 7, 9)
    }
  }
  
  "getLevelSeeds" should "return start, add and mult params from db" in {
    val base = blankParam.push(EvaluationParamsUtil.P_LEVEL_BASE)
    (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_SEED_START), *, *) returns(2)
    (pdStub.exists _) when(base.push(EvaluationParamsUtil.P_SEED_START), *) returns(true)
    (pdStub.getInt _) when(base.push(EvaluationParamsUtil.P_SEED_START), *, *) returns(2)
    (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_SEED_ADD), *, *) returns(3)
    (pdStub.exists _) when(base.push(EvaluationParamsUtil.P_SEED_ADD), *) returns(true)
    (pdStub.getInt _) when(base.push(EvaluationParamsUtil.P_SEED_ADD), *, *) returns(3)
    (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_SEED_MULT), *, *) returns(4)
    (pdStub.exists _) when(base.push(EvaluationParamsUtil.P_SEED_MULT), *) returns(true)
    (pdStub.getInt _) when(base.push(EvaluationParamsUtil.P_SEED_MULT), *, *) returns(4)
    
    assertResult((2, 3, 4)) {
      EvaluationParamsUtil.getLevelSeeds(pdStub, base, 5, 7, 9)
    }
  }
  
  "getNumberOfLevels" should "return None for blank database" in {
    val base = blankParam.push(EvaluationParamsUtil.P_LEVEL_BASE)
    (pdStub.exists _) when(*, *) returns(false)
    
    assertResult(None) {
      EvaluationParamsUtil.getNumberOfLevels(pdStub, base)
    }
  }
  
  "getNumberOfLevels" should "return Some(val) for with val from db" in {
    val base = blankParam.push(EvaluationParamsUtil.P_LEVEL_BASE)
    (pdStub.exists _) when(base.push(EvaluationParamsUtil.P_NUM_OF_LEVELS), *) returns(true)
    (pdStub.getInt _) when(base.push(EvaluationParamsUtil.P_NUM_OF_LEVELS), *, *) returns(10)
    (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_NUM_OF_LEVELS), *, *) returns(10)
    
    assertResult(Some(10)) {
      EvaluationParamsUtil.getNumberOfLevels(pdStub, base)
    }
  }
  
  "getEvaluationMutlipliers" should "return zero multiplier for blank db" in {
    val base = blankParam.push(EvaluationParamsUtil.P_EVAL_BASE)
    (pdStub.exists _) when(*, *) returns(false)
    
    assertResult(MWEvaluationMultipliers.zeroEvaluationMultipliers) {
      EvaluationParamsUtil.getEvaluationMutlipliers(pdStub, base)
    }
  }
  
  "getEvaluationMutlipliers" should "return eval mults from databade" in {
    val base = blankParam.push(EvaluationParamsUtil.P_EVAL_BASE)
    (pdStub.exists _) when(base, *) returns(true)
    
    (pdStub.exists _) when(base.push(EvaluationParamsUtil.P_COINS), *) returns(true)
    (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_COINS), *, *) returns(10)
    
    (pdStub.exists _) when(base.push(EvaluationParamsUtil.P_KILLED_BY_SHELL), *) returns(true)
    (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_KILLED_BY_SHELL), *, *) returns(200)
    
    (pdStub.exists _) when(base.push(EvaluationParamsUtil.P_DISTANCE), *) returns(true)
    (pdStub.getIntWithDefault _) when(base.push(EvaluationParamsUtil.P_DISTANCE), *, *) returns(1)
    
    val evalMults = EvaluationParamsUtil.getEvaluationMutlipliers(pdStub, base);
    
    assert(evalMults.coins == 10)
    assert(evalMults.killedByShell == 200)
    assert(evalMults.distance == 1)
    assert(evalMults.win == 0)
    assert(evalMults.kills == 0)
  }
  
  "getBaseLevelOptions" should "return defaultOptions for blank db" in {
    val base = blankParam.push(EvaluationParamsUtil.P_LEVEL_BASE)
    (pdStub.exists _) when(*, *) returns(false)
    
    assertResult(MWLevelOptions.defaultOptions) {
      EvaluationParamsUtil.getBaseLevelOptions(pdStub, base)
    }
  }
  
  "getBaseLevelOptions" should "return params from db" in {
    val base = blankParam.push(EvaluationParamsUtil.P_LEVEL_BASE)
    val optBase = base.push(EvaluationParamsUtil.P_BASE_OPTIONS)
    (pdStub.exists _) when(base, *) returns(true)
    
    (pdStub.exists _) when(optBase.push(EvaluationParamsUtil.P_BLOCKS), *) returns(true)
    (pdStub.getBoolean _) when(optBase.push(EvaluationParamsUtil.P_BLOCKS), *, *) returns(false)
    
    (pdStub.exists _) when(optBase.push(EvaluationParamsUtil.P_LENGTH), *) returns(true)
    (pdStub.getIntWithDefault _) when(optBase.push(EvaluationParamsUtil.P_LENGTH), *, *) returns(100)
    
    (pdStub.exists _) when(optBase.push(EvaluationParamsUtil.P_PITS), *) returns(true)
    (pdStub.getBoolean _) when(optBase.push(EvaluationParamsUtil.P_PITS), *, *) returns(false)
    
    val baseOpt = EvaluationParamsUtil.getBaseLevelOptions(pdStub, base);
    val defaultOpt = MWLevelOptions.defaultOptions

    
    assert(baseOpt.blocks == false)
    assert(baseOpt.levelLength == 100)
    assert(baseOpt.pits == false)
    assert(baseOpt.tubes == defaultOpt.tubes)
    assert(baseOpt.startingMarioMode == defaultOpt.startingMarioMode)
    assert(baseOpt.timeLimit == defaultOpt.timeLimit)
  }
  
  "getUpdateLevelOptionsFunction" should "return trivial lambda for blank database" in {
    val base = blankParam.push(EvaluationParamsUtil.P_LEVEL_BASE)
    (pdStub.exists _) when(*, *) returns(false)
    
    val updateOpt = EvaluationParamsUtil.getUpdateLevelOptionsFunction(pdStub, base, 3)
    val defaultOpt = MWLevelOptions.defaultOptions
    
    assert(updateOpt(0, defaultOpt) == defaultOpt)
    assert(updateOpt(1, defaultOpt) == defaultOpt)
    assert(updateOpt(2, defaultOpt) == defaultOpt)
  }
  
  "getUpdateLevelOptionsFunction" should "return param lambda from database" in {
    val base = blankParam.push(EvaluationParamsUtil.P_LEVEL_BASE)
    (pdStub.exists _) when(base, *) returns(true)
    
    (pdStub.exists _) when(base.push(""+0).push(EvaluationParamsUtil.P_BLOCKS), *) returns(true)
    (pdStub.getBoolean _) when(base.push(""+0).push(EvaluationParamsUtil.P_BLOCKS), *, *) returns(false)
    
    (pdStub.exists _) when(base.push(""+1).push(EvaluationParamsUtil.P_LENGTH), *) returns(true)
    (pdStub.getIntWithDefault _) when(base.push(""+1).push(EvaluationParamsUtil.P_LENGTH), *, *) returns(100)
    
    (pdStub.exists _) when(base.push(""+2).push(EvaluationParamsUtil.P_PITS), *) returns(true)
    (pdStub.getBoolean _) when(base.push(""+2).push(EvaluationParamsUtil.P_PITS), *, *) returns(false)
    
    val updateOpt = EvaluationParamsUtil.getUpdateLevelOptionsFunction(pdStub, base, 3)
    val defaultOpt = MWLevelOptions.defaultOptions
    
    assert(updateOpt(0, defaultOpt).blocks == false)
    assert(updateOpt(1, defaultOpt).levelLength == 100)
    assert(updateOpt(2, defaultOpt).pits == false)
    assert(updateOpt(3, defaultOpt) == defaultOpt)
  }
}