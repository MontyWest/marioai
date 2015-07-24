package com.montywest.marioai.util

object PrintUtils {
  
  def sceneToString(scene: Array[Array[Byte]], markCenter: Boolean = false): String = {
    if (markCenter) scene(scene.length/2)(scene(0).length/2) = 8
    scene.map {a: Array[Byte] => a.mkString(" ") } mkString("\n")
  }
  
}