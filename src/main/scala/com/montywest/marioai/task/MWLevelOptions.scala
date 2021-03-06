package com.montywest.marioai.task

import ch.idsia.tools.MarioAIOptions

/***
 * Options that control level generation
 */
class MWLevelOptions(
    val blocks: Boolean,   // Blocks appear
    val cannons: Boolean,  // Cannons appear
    val coins: Boolean,    // Coins appear
    val deadEnds: Boolean, // Dead ends appear in terrain forcing Mario to turn back
    val enemies: Boolean,  // Enemies/Creatures appear
    val flatLevel: Boolean, // Level is flat, no change in elevation
    val frozenCreatures: Boolean, // All creatures are frozen and don't move
    val pits: Boolean,     // Pits appear
    val hiddenBlocks: Boolean, // Hidden blocks appear
    val tubes: Boolean,    // Tubes/Pipes appear (occasionally with piranha plant enemies
    val ladders: Boolean,  // Ladders appear
    val levelDifficulty: Int, // Difficulty of level, effective range 0-25, 0 easiest
    val levelLength: Int,  // Length of level in blocks
    val levelType: Int,    // Type of level, 0-Outside 1-Cave, 2-Castle
    val startingMarioMode: Int, // Mode Mario starts as 0-small, 1-big, 2-fire
    val timeLimit: Int     // Number of Mario seconds allowed to complete level
) {
  
  override def clone: MWLevelOptions = {
    new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withBlocks(blocks: Boolean): MWLevelOptions = {
    return new MWLevelOptions(blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withCannons(cannons: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withCoins(coins: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withDeadEnds(deadEnds: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withEnemies(enemies: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withFlatLevel(flatLevel: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withFrozenCreatures(frozenCreatures: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withPits(pits: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withHiddenBlocks(hiddenBlocks: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withTubes(tubes: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withLadders(ladders: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withLevelDifficulty(levelDifficulty: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withLevelLength(_levelLength: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, _levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withLevelType(levelType: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withStartingMarioMode(startingMarioMode: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, startingMarioMode, this.timeLimit)
  }
  
  def withTimeLimit(_timeLimit: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.pits, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, _timeLimit)
  }
  
  override def toString: String = {
     "Level Options :- \n" +
     "  blocks: " + blocks + "\n" +
     "  cannons: " + cannons + "\n" +
     "  coins: " + coins + "\n" +
     "  deadEnds: " + deadEnds + "\n" +
     "  enemies: " + enemies + "\n" +
     "  flatLevel: " + flatLevel + "\n" +
     "  frozenCreatures: " + frozenCreatures + "\n" +
     "  gaps: " + pits + "\n" +
     "  hiddenBlocks: " + hiddenBlocks + "\n" +
     "  tubes: " + tubes + "\n" +
     "  ladders: " + ladders + "\n" +
     "  levelDifficulty: " + levelDifficulty + "\n" +
     "  levelLength: " + levelLength + "\n" +
     "  levelType: " + levelType + "\n" +
     "  startingMarioMode: " + startingMarioMode + "\n" +
     "  timeLimit: " + timeLimit + "\n"
  }
  
  override def equals(obj: Any): Boolean = {
    obj match {
      case other: MWLevelOptions => {
        if (other == null) false
        (blocks == other.blocks &&
        cannons == other.cannons &&
        coins == other.coins &&
        deadEnds == other.deadEnds &&
        enemies == other.enemies &&
        flatLevel == other.flatLevel &&
        frozenCreatures == other.frozenCreatures &&
        pits == other.pits &&
        hiddenBlocks == other.hiddenBlocks &&
        tubes == other.tubes &&
        ladders == other.ladders &&
        levelDifficulty == other.levelDifficulty &&
        levelLength == other.levelLength &&
        levelType == other.levelType &&
        startingMarioMode ==  other.startingMarioMode &&
        timeLimit == other.timeLimit)
      }
      case _ => false
    }
  }
}


object MWLevelOptions {
  
  def updateMarioAIOptions(marioAIOptions: MarioAIOptions, levelOptions: MWLevelOptions): MarioAIOptions = {
    marioAIOptions.setBlocksCount(levelOptions.blocks)
    marioAIOptions.setCannonsCount(levelOptions.cannons)
    marioAIOptions.setCoinsCount(levelOptions.coins)
    marioAIOptions.setDeadEndsCount(levelOptions.deadEnds)
    marioAIOptions.setEnemies(if(levelOptions.enemies) "" else "off")
    marioAIOptions.setFlatLevel(levelOptions.flatLevel)
    marioAIOptions.setFrozenCreatures(levelOptions.frozenCreatures)
    marioAIOptions.setGapsCount(levelOptions.pits)
    marioAIOptions.setHiddenBlocksCount(levelOptions.hiddenBlocks)
    marioAIOptions.setTubesCount(levelOptions.tubes)
    marioAIOptions.setLevelLadder(levelOptions.ladders)
    marioAIOptions.setLevelDifficulty(levelOptions.levelDifficulty)
    marioAIOptions.setLevelLength(levelOptions.levelLength)
    marioAIOptions.setLevelType(levelOptions.levelType)
    marioAIOptions.setMarioMode(levelOptions.startingMarioMode)
    marioAIOptions.setTimeLimit(levelOptions.timeLimit)
    
    marioAIOptions
  }
  
  val defaultOptions: MWLevelOptions = 
		  new MWLevelOptions(true,  //blocks
                         true,  //cannons
                         true,  //coins
                         false, //deadEnds
                         true,  //enemies
                         false, //flatLevel
                         false, //frozenCreatures
                         true,  //gaps
                         false, //hiddenBlocks
                         false, //tubes
                         false, //ladders
                         0,     //levelDifficulty
                         256,   //levelLength
                         0,     //levelType
                         2,     //startingMarioMode
                         200    //timeLimit
                         )
  
  val noUpdate: (Int, MWLevelOptions) => MWLevelOptions = (i: Int, options: MWLevelOptions) => options
  
  val compNumberOfLevels = 512;
  val compOptions = defaultOptions
  def compUpdate(levelSeed: Int): (Int, MWLevelOptions) => MWLevelOptions = (i: Int, options: MWLevelOptions) => {
//    println("Prev ll; " + options.levelLength)
    options.withLevelLength(((((i+levelSeed) * 431) % (501+levelSeed) ) % 462) + 50)
           .withTimeLimit((options.levelLength * 0.7).toInt)
           .withLevelType(i % 3)
           .withLevelDifficulty((compNumberOfLevels - i)/32)
           .withPits(i % 4 != 2)
           .withCannons(i % 6 == 2)
           .withTubes(i % 5 == 1)
           .withCoins(i % 5 != 0)
           .withBlocks(i % 6 != 2)
//           .withHiddenBlocks(i % 6 != 0)
           .withDeadEnds(false)
           .withLadders(i % 10 == 2)
           .withFrozenCreatures(i % 3 == 1)
           .withEnemies(!(i % 4 == 1))
           .withStartingMarioMode(
               if (i % 7 == 5 || i % 7 == 1) {
                 if (i % 2 == 0) 0 
                 else 1
               } else 
                 2)
  }
}