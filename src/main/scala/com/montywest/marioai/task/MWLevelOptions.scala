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
    val levelDifficulty: Int,
    val levelLength: Int,
    val levelType: Int,
    val startingMarioMode: Int
) {
  
  def withBlocks(blocks: Boolean): MWLevelOptions = {
    return new MWLevelOptions(blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withCannons(cannons: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withCoins(coins: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withDeadEnds(deadEnds: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withEnemies(enemies: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withFlatLevel(flatLevel: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withFrozenCreatures(frozenCreatures: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withGaps(gaps: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withHiddenBlocks(hiddenBlocks: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withTubes(tubes: Boolean): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, tubes, this.levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withLevelDifficulty(levelDifficulty: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, levelDifficulty, this.levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withLevelLength(levelLength: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, levelLength, this.levelType, this.startingMarioMode)
  }
  
  def withLevelType(levelType: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, levelType, this.startingMarioMode)
  }
  
  def withStartingMarioMode(startingMarioMode: Int): MWLevelOptions = {
    return new MWLevelOptions(this.blocks, this.cannons, this.coins, this.deadEnds, this.enemies, this.flatLevel, this.frozenCreatures, this.gaps, this.hiddenBlocks, this.tubes, this.levelDifficulty, this.levelLength, this.levelType, startingMarioMode)
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
    marioAIOptions.setLevelDifficulty(levelOptions.levelDifficulty)
    marioAIOptions.setLevelLength(levelOptions.levelLength)
    marioAIOptions.setLevelType(levelOptions.levelType)
    marioAIOptions.setMarioMode(levelOptions.startingMarioMode)
    
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
                         0,     //levelDifficulty
                         256,   //levelLength
                         0,     //levelType
                         2      //startingMarioMode
                         )
}