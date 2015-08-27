package com.montywest.marioai.task

import ch.idsia.benchmark.tasks.BasicTask
import ch.idsia.benchmark.tasks.Task
import ch.idsia.tools.MarioAIOptions
import ch.idsia.tools.EvaluationInfo
import java.text.DecimalFormat
import scala.annotation.tailrec
import ch.idsia.agents.Agent

abstract class MWBasicTask(val name: String, val baseMarioAIOptions: MarioAIOptions) extends BasicTask(baseMarioAIOptions) with Task {

  val localEvaluationInfo = new EvaluationInfo();
  var disqualifications = 0;
  localEvaluationInfo.setTaskName(name);
  this.resetLocalEvaluationInfo
  
  def getFitness: Int = localEvaluationInfo.computeWeightedFitness()
  
  def numberOfLevels: Int
  
  protected def baseLevelOptions: MWLevelOptions
  
  protected def updateOptions(episode: Int, options: MWLevelOptions): MWLevelOptions
    
  protected def nextLevelSeed(episode: Int, currentSeed: Int): Int
  
  protected def getBaseLevelSeed: Int
  
  override def doEpisodes(amount: Int, verbose: Boolean, repetitionsOfSingleEpisode: Int): Unit = {
    @tailrec
    def runSingle(iteration: Int, prevOptions: MWLevelOptions, disqualifications: Int): Int = {
      if (iteration == amount) { 
        disqualifications
      } else {
        val newOptions = updateOptions(iteration, prevOptions)
        
        val marioAIOptions = MWLevelOptions.updateMarioAIOptions(options, newOptions)
        marioAIOptions.setLevelRandSeed(nextLevelSeed(iteration, marioAIOptions.getLevelRandSeed))
        
        super.setOptionsAndReset(marioAIOptions)
        val disqualified: Int = if (!runSingleEpisode(repetitionsOfSingleEpisode)) 1 else 0
        
        updateLocalEvaluationInfo(super.getEvaluationInfo)
        runSingle(iteration+1, newOptions, disqualifications + disqualified)
      }
    }
    
    super.setOptionsAndReset(MWLevelOptions.updateMarioAIOptions(options, baseLevelOptions))
    disqualifications = runSingle(0, baseLevelOptions, 0)
  }
  
  def doEpisodes: Unit = {
    doEpisodes(numberOfLevels, false, 1)
  }
  
  protected def injectAgent(agent: Agent, resetEval: Boolean): Unit = {
    options.setAgent(agent);
    reset
    if (resetEval) this.resetLocalEvaluationInfo
  }
  
  protected def injectLevelSeed(seed: Int, resetEval: Boolean): Unit = {
    options.setLevelRandSeed(seed)
    reset
    if (resetEval) this.resetLocalEvaluationInfo
  }
  
  def getStatistics(): String = {
    val df: DecimalFormat = new DecimalFormat("#.##")
    return  "\n[MarioAI] ~ Evaluation Results for Task - " + localEvaluationInfo.getTaskName() + "\n" +
            "\n                     Seed : " + this.getBaseLevelSeed +
            "\n                    Agent : " + options.getAgent.getName +
            "\n         Weighted Fitness : " + getFitness +
            "\n         Levels Completed : " + localEvaluationInfo.marioStatus + " of " + numberOfLevels +
            "\n               Mario Mode : " + localEvaluationInfo.marioMode +
            "\nCollisions with creatures : " + localEvaluationInfo.collisionsWithCreatures +
            "\n     Passed (Cells, Phys) : " + localEvaluationInfo.distancePassedCells + " of " + localEvaluationInfo.levelLength + ", " + df.format(localEvaluationInfo.distancePassedPhys) + " of " + df.format(localEvaluationInfo.levelLength * 16) + " (" + localEvaluationInfo.distancePassedCells * 100 / localEvaluationInfo.levelLength + "% passed)" +
            "\n Time Spent(marioseconds) : " + localEvaluationInfo.timeSpent +
            "\n  Time Left(marioseconds) : " + localEvaluationInfo.timeLeft +
            "\n             Coins Gained : " + localEvaluationInfo.coinsGained + " of " + localEvaluationInfo.totalNumberOfCoins + " (" + localEvaluationInfo.coinsGained * 100 / (if(localEvaluationInfo.totalNumberOfCoins == 0) 1 else localEvaluationInfo.totalNumberOfCoins) + "% collected)" +
            "\n      Hidden Blocks Found : " + localEvaluationInfo.hiddenBlocksFound + " of " + localEvaluationInfo.totalNumberOfHiddenBlocks + " (" + localEvaluationInfo.hiddenBlocksFound * 100 / (if(localEvaluationInfo.totalNumberOfHiddenBlocks == 0) 1 else localEvaluationInfo.totalNumberOfHiddenBlocks) + "% found)" +
            "\n       Mushrooms Devoured : " + localEvaluationInfo.mushroomsDevoured + " of " + localEvaluationInfo.totalNumberOfMushrooms + " found (" + localEvaluationInfo.mushroomsDevoured * 100 / (if(localEvaluationInfo.totalNumberOfMushrooms == 0) 1 else localEvaluationInfo.totalNumberOfMushrooms) + "% collected)" +
            "\n         Flowers Devoured : " + localEvaluationInfo.flowersDevoured + " of " + localEvaluationInfo.totalNumberOfFlowers + " found (" + localEvaluationInfo.flowersDevoured * 100 / (if(localEvaluationInfo.totalNumberOfFlowers == 0) 1 else localEvaluationInfo.totalNumberOfFlowers) + "% collected)" +
            "\n              Kills Total : " + localEvaluationInfo.killsTotal + " of " + localEvaluationInfo.totalNumberOfCreatures + " found (" + localEvaluationInfo.killsTotal * 100 / (if(localEvaluationInfo.totalNumberOfCreatures == 0) 1 else localEvaluationInfo.totalNumberOfCreatures) + "%)" +
            "\n            Kills By Fire : " + localEvaluationInfo.killsByFire +
            "\n           Kills By Shell : " + localEvaluationInfo.killsByShell +
            "\n           Kills By Stomp : " + localEvaluationInfo.killsByStomp +
            "\n        Disqualifications : " + disqualifications
  }
  
  protected def updateLocalEvaluationInfo(evInfo: EvaluationInfo) = {
    localEvaluationInfo.distancePassedCells += evInfo.distancePassedCells;
    localEvaluationInfo.distancePassedPhys += evInfo.distancePassedPhys;
    localEvaluationInfo.flowersDevoured += evInfo.flowersDevoured;
    localEvaluationInfo.killsTotal += evInfo.killsTotal;
    localEvaluationInfo.killsByFire += evInfo.killsByFire;
    localEvaluationInfo.killsByShell += evInfo.killsByShell;
    localEvaluationInfo.killsByStomp += evInfo.killsByStomp;
    localEvaluationInfo.marioMode += evInfo.marioMode;
    localEvaluationInfo.marioStatus += evInfo.marioStatus;
    localEvaluationInfo.mushroomsDevoured += evInfo.mushroomsDevoured;
    localEvaluationInfo.coinsGained += evInfo.coinsGained;
    localEvaluationInfo.timeLeft += evInfo.timeLeft;
    localEvaluationInfo.timeSpent += evInfo.timeSpent;
    localEvaluationInfo.hiddenBlocksFound += evInfo.hiddenBlocksFound;
    localEvaluationInfo.totalNumberOfCoins += evInfo.totalNumberOfCoins;
    localEvaluationInfo.totalNumberOfCreatures += evInfo.totalNumberOfCreatures;
    localEvaluationInfo.totalNumberOfFlowers += evInfo.totalNumberOfFlowers;
    localEvaluationInfo.totalNumberOfMushrooms += evInfo.totalNumberOfMushrooms;
    localEvaluationInfo.totalNumberOfHiddenBlocks += evInfo.totalNumberOfHiddenBlocks;
    localEvaluationInfo.collisionsWithCreatures += evInfo.collisionsWithCreatures;
    localEvaluationInfo.levelLength += evInfo.levelLength;
  }
  
  protected def resetLocalEvaluationInfo = {
    localEvaluationInfo.distancePassedCells = 0;
    localEvaluationInfo.distancePassedPhys = 0;
    localEvaluationInfo.flowersDevoured = 0;
    localEvaluationInfo.killsTotal = 0;
    localEvaluationInfo.killsByFire = 0;
    localEvaluationInfo.killsByShell = 0;
    localEvaluationInfo.killsByStomp = 0;
    localEvaluationInfo.marioMode = 0;
    localEvaluationInfo.marioStatus = 0;
    localEvaluationInfo.mushroomsDevoured = 0;
    localEvaluationInfo.coinsGained = 0;
    localEvaluationInfo.timeLeft = 0;
    localEvaluationInfo.timeSpent = 0;
    localEvaluationInfo.hiddenBlocksFound = 0;
    localEvaluationInfo.totalNumberOfCoins = 0;
    localEvaluationInfo.totalNumberOfCreatures = 0;
    localEvaluationInfo.totalNumberOfFlowers = 0;
    localEvaluationInfo.totalNumberOfMushrooms = 0;
    localEvaluationInfo.totalNumberOfHiddenBlocks = 0;
    localEvaluationInfo.collisionsWithCreatures = 0;
    localEvaluationInfo.levelLength = 0;
  }
  
}