package com.montywest.marioai.task

import ch.idsia.tools.MarioAIOptions

class MWLevelOptions(
    val blocks: Boolean,
    val cannons: Boolean,
    val coins: Boolean,
    val deadEnds: Boolean,
    val enemies: Boolean,
    val flatLevel: Boolean,
    val frozenCreatures: Boolean,
    val gaps: Boolean,
    val hiddenBlocks: Boolean,
    val tubes: Boolean,
    val ladders: Boolean,
    val levelDifficulty: Int,
    val levelLength: Int,
    val levelType: Int,
    val startingMarioMode: Int,
    val timeLimit: Int
) {
  
  override def clone: MWLevelOptions = {
    new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withBlocks(blocks: Boolean): MWLevelOptions = {
    return new MWLevelOptions(blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withCannons(cannons: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withCoins(coins: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withDeadEnds(deadEnds: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withEnemies(enemies: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withFlatLevel(flatLevel: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withFrozenCreatures(frozenCreatures: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withGaps(gaps: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withHiddenBlocks(hiddenBlocks: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withTubes(tubes: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withLadders(ladders: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withLevelDifficulty(levelDifficulty: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withLevelLength(levelLength: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, levelLength, this.levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withLevelType(levelType: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, levelType, this.startingMarioMode, this.timeLimit)
  }
  
  def withStartingMarioMode(startingMarioMode: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, startingMarioMode, this.timeLimit)
  }
  
  def withTimeLimit(timeLimit: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.ladders, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode, timeLimit)
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
     "  gaps: " + gaps + "\n" +
     "  hiddenBlocks: " + hiddenBlocks + "\n" +
     "  tubes: " + tubes + "\n" +
     "  ladders: " + ladders + "\n" +
     "  levelDifficulty: " + levelDifficulty + "\n" +
     "  levelLength: " + levelLength + "\n" +
     "  levelType: " + levelType + "\n" +
     "  startingMarioMode: " + startingMarioMode + "\n" +
     "  timeLimit: " + timeLimit + "\n"
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
    marioAIOptions.setGapsCount(levelOptions.gaps)
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
  
  val compOptions = defaultOptions
  val noUpdate: (Int, MWLevelOptions) => MWLevelOptions = (i: Int, options: MWLevelOptions) => options
  
  def compUpdate(levelSeed: Int): (Int, MWLevelOptions) => MWLevelOptions = (i: Int, options: MWLevelOptions) => {
    options.withLevelLength(Math.max(((200 + (i * 12) + ((levelSeed+i-1) % (i + 1))) % 512), 50))
           .withLevelType(i % 3)
           .withLevelDifficulty(i/20)
           .withGaps(i % 3 == 0)
           .withCannons(i % 3 != 1)
           .withCoins(i % 5 != 0)
           .withBlocks(i % 4 != 0)
           .withHiddenBlocks(i % 6 != 0)
           .withDeadEnds(i % 10 == 0)
           .withLadders(i % 10 == 2)
           .withFrozenCreatures(i % 3 == 1)
           .withEnemies(!(i % 4 == 1))
  }
}