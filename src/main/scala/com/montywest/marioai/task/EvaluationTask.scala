package com.montywest.marioai.task

import ch.idsia.agents.Agent

trait EvaluationTask {

  def evaluate: Int;
  
  def withAgent(agent: Agent): EvaluationTask;
  
  def withLevelSeed(seed: Int): EvaluationTask;
  
  def getLevelSeed: Int;
  
  def getStatistics: String;
  
}