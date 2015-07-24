package com.montywest.marioai.util

import scala.util.Random

object RandomUtils {

  private val r = new Random(System.currentTimeMillis)
  
  def randomIntEx(lowerIn: Int, higherEx: Int): Int = {
    r.nextInt(higherEx - lowerIn) + lowerIn
  }
  def randomIntIn(lowerIn: Int, higherIn: Int): Int = {
    r.nextInt(higherIn+1 - lowerIn) + lowerIn
  }
  
  def randomByteEx(lowerIn: Byte, higherEx: Byte): Byte = {
    (r.nextInt(higherEx.toInt - lowerIn.toInt) + lowerIn).toByte
  }
  def randomByteIn(lowerIn: Byte, higherIn: Byte): Byte = {
    (r.nextInt(higherIn.toInt+1 - lowerIn.toInt) + lowerIn).toByte
  }
  
  def randomIntTupleInclusive(lowerIn: (Int, Int), higherIn: (Int, Int)): (Int, Int) = {
    (lowerIn, higherIn) match {
      case ((x1, y1), (x2, y2)) if x1 <= x2 &&  y1 <= y2 =>
         (randomIntIn(x1, x2), randomIntIn(y1, y2))
      case _ => throw new IllegalArgumentException
    }
  }
}