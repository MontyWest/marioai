package com.montywest.marioai.fixtures

object EnvironmentFixtures {

  val mario = (9, 9)
  val sceneSize = (19, 19)
  
  def baseLevelScene(groundLevel: Int): Array[Array[Byte]] = Array.fill(sceneSize._1, sceneSize._2)(0: Byte)
                                                .updated(groundLevel+mario._1 + 1, Array.fill(sceneSize._2)(1: Byte))
                                                .updated(groundLevel+mario._1, Array.fill(sceneSize._2)(1: Byte));
  
  val baseEnemies: Array[Array[Byte]] = Array.fill(sceneSize._1, sceneSize._2)(0: Byte);
                                                
  def getEnemyScene(enemy: (Int, Int)): Array[Array[Byte]] = {
    Array.tabulate(sceneSize._1, sceneSize._2)((x: Int, y: Int) => {
      if (enemy == (x-mario._1, y-mario._2)) 1 else 0
    })
  }
  
  def getEnemyScene(enemies: Set[(Int, Int)]): Array[Array[Byte]] = {
    Array.tabulate(sceneSize._1, sceneSize._2)((x: Int, y: Int) => if (enemies.contains(x-mario._1, y-mario._2)) 1 else 0)
  }
  
  def getLevelSceneWithPit(groundLevel: Int, pitCols: Set[Int]): Array[Array[Byte]] = {
    Array.tabulate(sceneSize._1, sceneSize._2)(
        (x: Int, y: Int) => {
        	if (pitCols.contains(y-mario._2)) 0
        	else if (x-mario._1 == groundLevel) 1
          else 0
        })
  }
  
  def getCustomLevelScene(filledRows: Set[Int], removals: Set[(Int,Int)], additions: Set[(Int, Int)]): Array[Array[Byte]] = {
    Array.tabulate(sceneSize._1, sceneSize._2)(
        (x: Int, y: Int) => {
          if (removals.contains((x-mario._1, y-mario._2))) 0
          else if (additions.contains((x-mario._1, y-mario._2))) 1
          else if (filledRows.contains(x-mario._1)) 1
          else 0
        })
  }
  
}