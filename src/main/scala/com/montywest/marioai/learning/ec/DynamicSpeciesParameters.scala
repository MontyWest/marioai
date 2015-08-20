package com.montywest.marioai.learning.ec

abstract class DynamicSpeciesParameters {

  def minGene: Option[Long] = None
  def maxGene: Option[Long] = None
  def mutationProb: Option[Double] = None
  
  def numSegments: Option[Int] = None
	def segmentType: Option[String] = None
  
  def segmentStart(segment: Int): Option[Int] = None
  def segmentEnd(segment: Int): Option[Int] = None
  
  def segmentMinGene(segment: Int): Option[Int] = None
  def segmentMaxGene(segment: Int): Option[Int] = None
  def segmentMutationProb(segment: Int): Option[Int] = None
  
  def moduloNum: Option[Int] = None
  def moduloMinGene(congruence: Int): Option[Int] = None
  def moduloMaxGene(congruence: Int): Option[Int] = None
  def moduloMutationProb(congruence: Int): Option[Int] = None
  
  def minGene(index: Int): Option[Int] = None
  def maxGene(index: Int): Option[Int] = None
  def mutationProb(index: Int): Option[Double] = None
}