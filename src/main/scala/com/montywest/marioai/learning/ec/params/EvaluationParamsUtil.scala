package com.montywest.marioai.learning.ec.params

import com.montywest.marioai.task.MWLevelOptions
import ec.util.ParameterDatabaseInf
import ec.util.Parameter
import java.util.regex.Pattern
import com.montywest.marioai.task.MWEvaluationMultipliers

object EvaluationParamsUtil {
  
  val P_EVAL_BASE     = "eval.problem" 
  
  val P_NUM_OF_LEVELS = "num-levels"
  
  //LEVEL OPTIONS
  val P_LEVEL_BASE   = "level"
  
  val P_BASE_OPTIONS = "base"
  
  val P_BLOCKS           = "blocks"
  val P_CANNONS          = "cannons"
  val P_COINS            = "coins"
  val P_DEAD_ENDS        = "dead-ends"
  val P_ENEMIES          = "enemies"
  val P_FLAT             = "flat"
  val P_FROZEN_CREATURES = "frozen-enemies"
  val P_PITS             = "pits"
  val P_HIDDEN_BLOCKS    = "hidden-blocks"
  val P_TUBES            = "tubes"
  val P_DIFFICULTY       = "difficulty-num"
  val P_LENGTH           = "length-num"
  val P_TYPE             = "type-num"
  val P_START_MODE       = "start-mode-num"
  val P_TIME_LIMIT       = "time-limit"
  
  val blocksFn = (b: Boolean, opt: MWLevelOptions) => opt.withBlocks(b)
  val cannonsFn = (b: Boolean, opt: MWLevelOptions) => opt.withCannons(b)
  val coinsFn = (b: Boolean, opt: MWLevelOptions) => opt.withCoins(b)
  val deadEndsFn = (b: Boolean, opt: MWLevelOptions) => opt.withDeadEnds(b)
  val enemiesFn = (b: Boolean, opt: MWLevelOptions) => opt.withEnemies(b)
  val flatLevelFn = (b: Boolean, opt: MWLevelOptions) => opt.withFlatLevel(b)
  val frozenFn = (b: Boolean, opt: MWLevelOptions) => opt.withFrozenCreatures(b)
  val pitsFn = (b: Boolean, opt: MWLevelOptions) => opt.withPits(b)
  val hiddenBlocksFn = (b: Boolean, opt: MWLevelOptions) => opt.withHiddenBlocks(b)
  val tubesFn = (b: Boolean, opt: MWLevelOptions) => opt.withTubes(b)
  
  val difficultyFn = (i: Int, opt: MWLevelOptions) => opt.withLevelDifficulty(i)
  val lengthFn = (i: Int, opt: MWLevelOptions) => opt.withLevelLength(i)
  val typeFn = (i: Int, opt: MWLevelOptions) => opt.withLevelType(i)
  val startModeFn = (i: Int, opt: MWLevelOptions) => opt.withStartingMarioMode(i)
  val timeLimitFn = (i: Int, opt: MWLevelOptions) => opt.withTimeLimit(i)
  
  
  //EVAL MULTIPLIER
  
  val P_MULTS_BASE = "mults"
  
  val P_DISTANCE = "distance"
  val P_WIN = "win"
  val P_MODE = "mode"
  val P_FLOWER_FIRE = "flower-fire"
  val P_KILLS = "kills"
  val P_KILLED_BY_FIRE = "kills-fire"
  val P_KILLED_BY_SHELL = "kills-shell"
  val P_KILLED_BY_STOMP = "kills-stomp"
  val P_MUSHROOM = "mushroom"
  val P_TIME_LEFT = "time-left"
  val P_HIDDEN_BLOCK = "hidden-block"
  val P_GREEN_MUSHROOM = "green-mushroom"
  val P_STOMP = "stomp"
  
  
  //SEEDS 
  val P_SEED = "seed"
  val P_SEED_START = "start"
  val P_SEED_ADD = "add"
  val P_SEED_MULT = "mult"
  
  
  def getLevelParamsBase(pd: ParameterDatabaseInf): Option[Parameter] = {
    getParamsBase(pd, P_LEVEL_BASE)
  }
  
  def getMultsParamsBase(pd: ParameterDatabaseInf): Option[Parameter] = {
    getParamsBase(pd, P_MULTS_BASE)
  }
  
  def getSeedParamBase(pd: ParameterDatabaseInf): Option[Parameter] = {
    val bp = getParamsBase(pd, P_SEED)
    bp
  }
  
  private def getParamsBase(pd: ParameterDatabaseInf, postfix: String): Option[Parameter] = {
    val evalBase = new Parameter("eval").push("problem").push(postfix)
    if (pd.exists(evalBase, null)) 
      Some(evalBase)
    else {
      val probBase = new Parameter("problem").push(postfix)
      if (pd.exists(probBase, null))
        Some(probBase)
      else {
        val plainBase = new Parameter(postfix)
        if (pd.exists(plainBase, null))
          Some(plainBase)
        else 
          None
      }
    }
  }
  
  def getLevelSeeds(pd: ParameterDatabaseInf, base: Parameter, dStart: Int, dAdd: Int, dMult: Int): (Int, Int, Int) = {
    val seedStart = pd.getIntWithDefault(base.push(P_SEED_START), null, dStart)
    val seedAdd = pd.getIntWithDefault(base.push(P_SEED_ADD), null, dAdd)
    val seedMult = pd.getIntWithDefault(base.push(P_SEED_MULT), null, dMult)
    (seedStart, seedAdd, seedMult)
  }
  
  def getNumberOfLevels(pd: ParameterDatabaseInf, base: Parameter): Option[Int] = {
    if (pd.exists(base.push(P_NUM_OF_LEVELS), null))
    	Some(pd.getInt(base.push(P_NUM_OF_LEVELS), null, 1))
    else
    	None
  }
  
  def getEvaluationMutlipliers(pd: ParameterDatabaseInf, base: Parameter): MWEvaluationMultipliers = {
    def innerLoop(opt: MWEvaluationMultipliers, vec: Seq[(String, (Int, MWEvaluationMultipliers) => MWEvaluationMultipliers)]): MWEvaluationMultipliers = vec match  {
      case Nil => opt
      case (key: String, fn: ((Int, MWEvaluationMultipliers) => MWEvaluationMultipliers)) +: tl => {        
        innerLoop(getIntOption(pd, base, key) match { case Some(i) =>{fn(i, opt)} case None => opt }, tl)
      }
    }
    
    innerLoop(
      MWEvaluationMultipliers.zeroEvaluationMultipliers,
      Seq((P_DISTANCE, (i: Int, eval: MWEvaluationMultipliers) => eval.withDistance(i)),
          (P_WIN, (i: Int, eval: MWEvaluationMultipliers) => eval.withWin(i)),
          (P_MODE, (i: Int, eval: MWEvaluationMultipliers) => eval.withMode(i)),
          (P_FLOWER_FIRE, (i: Int, eval: MWEvaluationMultipliers) => eval.withFlowerFire(i)),
          (P_COINS, (i: Int, eval: MWEvaluationMultipliers) => eval.withCoins(i)),
          (P_KILLS, (i: Int, eval: MWEvaluationMultipliers) => eval.withKills(i)),
          (P_KILLED_BY_FIRE, (i: Int, eval: MWEvaluationMultipliers) => eval.withKilledByFire(i)),
          (P_KILLED_BY_SHELL, (i: Int, eval: MWEvaluationMultipliers) => eval.withKilledByShell(i)),
          (P_KILLED_BY_STOMP, (i: Int, eval: MWEvaluationMultipliers) => eval.withKilledByStomp(i)),
          (P_MUSHROOM, (i: Int, eval: MWEvaluationMultipliers) => eval.withMushroom(i)),
          (P_TIME_LEFT, (i: Int, eval: MWEvaluationMultipliers) => eval.withTimeLeft(i)),
          (P_HIDDEN_BLOCK, (i: Int, eval: MWEvaluationMultipliers) => eval.withHiddenBlock(i)),
          (P_GREEN_MUSHROOM, (i: Int, eval: MWEvaluationMultipliers) => eval.withGreenMushroom(i)),
          (P_STOMP, (i: Int, eval: MWEvaluationMultipliers) => eval.withStomp(i)))
    )
  }
  

  def getBaseLevelOptions(pd: ParameterDatabaseInf, preBase: Parameter): MWLevelOptions = {
    val base = preBase.push(P_BASE_OPTIONS);
    
    def innerBoolLoop(opt: MWLevelOptions, vec: Seq[(String, (Boolean, MWLevelOptions) => MWLevelOptions)]): MWLevelOptions = vec match  {
      case Nil => opt
      case (key: String, fn: ((Boolean, MWLevelOptions) => MWLevelOptions)) +: tl => 
        innerBoolLoop(getBooleanOption(pd, base, key) match { case Some(b) => fn(b, opt); case None => opt }, tl)
    }
    
    def innerIntLoop(opt: MWLevelOptions, vec: Seq[(String, (Int, MWLevelOptions) => MWLevelOptions)]): MWLevelOptions = vec match  {
      case Nil => opt
      case (key: String, fn: ((Int, MWLevelOptions) => MWLevelOptions)) +: tl => 
        innerIntLoop(getIntOption(pd, base, key) match { case Some(i) => fn(i, opt); case None => opt }, tl)
    }
    
    val afterBools = 
      innerBoolLoop(
          MWLevelOptions.defaultOptions,
          Seq((P_BLOCKS, blocksFn),
              (P_CANNONS, cannonsFn),
              (P_COINS, coinsFn),
              (P_DEAD_ENDS, deadEndsFn),
              (P_ENEMIES, enemiesFn),
              (P_FLAT, flatLevelFn),
              (P_FROZEN_CREATURES, frozenFn),
              (P_PITS, pitsFn),
              (P_HIDDEN_BLOCKS, hiddenBlocksFn),
              (P_TUBES, tubesFn))
      )

    innerIntLoop(
          afterBools,
          Seq((P_DIFFICULTY, difficultyFn),
              (P_LENGTH, lengthFn),
              (P_TYPE, typeFn),
              (P_START_MODE, startModeFn),
              (P_TIME_LIMIT, timeLimitFn))
    )
  }
  
  def getUpdateLevelOptionsFunction(pd: ParameterDatabaseInf, base: Parameter, numberOfLevels: Int): (Int, MWLevelOptions) => MWLevelOptions = {
    
    def innerBool(mapOpt: Option[Map[Int,Boolean]], key: String, i: Int) = {
      getBooleanOption(pd, base.push(""+i), key) match {
          case None => mapOpt
          case Some(b: Boolean) => mapOpt match {
            case None => Some(Map(i->b))
            case Some(map) => Some(map + (i->b))
          }
        }
    } 
    
    def innerInt(mapOpt: Option[Map[Int,Int]], key: String, i: Int) = {
      getIntOption(pd, base.push(""+i), key) match {
          case None => mapOpt
          case Some(n: Int) => mapOpt match {
            case None => Some(Map(i->n))
            case Some(map) => Some(map + (i->n))
          }
        }
    }
    
    var blocks: Option[Map[Int,Boolean]] = None
    var cannons: Option[Map[Int,Boolean]] = None
    var coins: Option[Map[Int,Boolean]] = None
    var deadEnds: Option[Map[Int,Boolean]] = None
    var enemies: Option[Map[Int,Boolean]] = None
    var flatLevel: Option[Map[Int,Boolean]] = None
    var frozenCreatues: Option[Map[Int,Boolean]] = None
    var pits: Option[Map[Int,Boolean]] = None
    var hiddenBlocks: Option[Map[Int,Boolean]] = None
    var tubes: Option[Map[Int,Boolean]] = None
    var levelDifficulty: Option[Map[Int,Int]] = None
    var levelLength: Option[Map[Int,Int]] = None
    var levelType: Option[Map[Int,Int]] = None
    var startMode: Option[Map[Int,Int]] = None
    var timeLimit: Option[Map[Int,Int]] = None
 
    
    for (i <- 0 until numberOfLevels) {
//      if (pd.exists(base.push(""+i), null)) {
        blocks = innerBool(blocks, P_BLOCKS, i)
        cannons = innerBool(cannons, P_CANNONS, i)
        coins = innerBool(coins, P_COINS, i)
        deadEnds = innerBool(deadEnds, P_DEAD_ENDS, i)
        enemies = innerBool(enemies, P_ENEMIES, i)
        flatLevel = innerBool(flatLevel, P_FLAT, i)
        frozenCreatues = innerBool(frozenCreatues, P_FROZEN_CREATURES, i)
        pits = innerBool(pits, P_PITS, i)
        hiddenBlocks = innerBool(hiddenBlocks, P_HIDDEN_BLOCKS, i)
        tubes = innerBool(tubes, P_TUBES, i)
        levelDifficulty = innerInt(levelDifficulty, P_DIFFICULTY, i)
        levelLength = innerInt(levelLength, P_LENGTH, i)
        levelType = innerInt(levelType, P_TYPE, i)
        startMode = innerInt(startMode, P_START_MODE, i)
        timeLimit = innerInt(timeLimit, P_TIME_LIMIT, i)
//      }
    }

    buildUpdateFunction(
      Seq((blocks, blocksFn),
          (cannons, cannonsFn),
          (coins, coinsFn),
          (deadEnds, deadEndsFn),
          (enemies, enemiesFn),
          (flatLevel, flatLevelFn),
          (frozenCreatues, frozenFn),
          (pits, pitsFn),
          (hiddenBlocks, hiddenBlocksFn),
          (tubes, tubesFn)),
      Seq((levelDifficulty, difficultyFn),
          (levelLength, lengthFn),
          (levelType, typeFn),
          (startMode, startModeFn),
          (timeLimit, timeLimitFn))
    )
  }
  
  private def getBooleanOption(pd: ParameterDatabaseInf, base: Parameter, key: String): Option[Boolean] = {
    if (pd.exists(base.push(key), null))
      Some(pd.getBoolean(base.push(key), null, false))
    else
      None
  }
  
  private def getIntOption(pd: ParameterDatabaseInf, base: Parameter, key: String): Option[Int] = {        
    if (pd.exists(base.push(key), null)) {
      Some(pd.getIntWithDefault(base.push(key), null, 0))
    } else {
      None
    }
  }
  
  def buildUpdateFunction(
      boolSeq: Seq[(Option[Map[Int,Boolean]], (Boolean, MWLevelOptions) => MWLevelOptions )],
      intSeq: Seq[(Option[Map[Int,Int]], (Int, MWLevelOptions) => MWLevelOptions )]
    ): (Int, MWLevelOptions) => MWLevelOptions = {
      def innerBoolLoop(i: Int, opt: MWLevelOptions, boolSeq:
        Seq[(Option[Map[Int,Boolean]], (Boolean, MWLevelOptions) => MWLevelOptions )]): MWLevelOptions = boolSeq match {
        case Nil => opt
        case (mapOpt, fn) +: tl => mapOpt match {
          case Some(map) => map.get(i) match {
            case Some(b: Boolean) => innerBoolLoop(i, fn(b, opt), tl)
            case None => innerBoolLoop(i, opt, tl)
          }
          case None => innerBoolLoop(i, opt, tl)
        }
      }
      
      def innerIntLoop(i: Int, opt: MWLevelOptions, intSeq:
        Seq[(Option[Map[Int,Int]], (Int, MWLevelOptions) => MWLevelOptions )]): MWLevelOptions = intSeq match {
        case Nil => opt
        case (mapOpt, fn) +: tl => mapOpt match {
          case Some(map) => map.get(i) match {
            case Some(x: Int) => innerIntLoop(i, fn(x, opt), tl)
            case None => innerIntLoop(i, opt, tl)
          }
          case None => innerIntLoop(i, opt, tl)
        }
      }
      
      
      (i: Int, current: MWLevelOptions) => {
        val afterBool = innerBoolLoop(i, current, boolSeq)
        innerIntLoop(i, afterBool, intSeq)
      }
      
  }
}