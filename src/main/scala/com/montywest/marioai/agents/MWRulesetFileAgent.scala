package com.montywest.marioai.agents

import ch.idsia.agents.Agent
import java.io.FileNotFoundException
import com.montywest.marioai.rules.Ruleset
import com.montywest.marioai.rules.Ruleset
import scala.io.BufferedSource
import com.montywest.marioai.rules.Ruleset
import com.montywest.marioai.rules.Ruleset
import java.util.regex.Pattern
import java.io.FileWriter
import java.io.IOException
import com.montywest.marioai.rules.Rule
import com.montywest.marioai.rules.Conditions
import java.io.File


object MWRulesetFileAgent {
  
  private val postfix = ".agent"
  private val writeFolder = "agents/written/"
  val CSV_HEADER = "cMM,cJA,cOG,cEL,cEUR,cELR,cOA,cPA,cPB,cMX,cMY,aL,aR,aJ,aS"
  
  def fromFile(filename: String, lineStart: Int = 0): MWRulesetAgent = {
    var bufferedSource: Option[BufferedSource] = None
    try {
      bufferedSource = Some(io.Source.fromFile(filename))
      val lines = bufferedSource.get.getLines.toVector.drop(lineStart)
      val letters = "[a-zA-Z]".r
      val linesDrop = 
        if (lines.headOption.isDefined && letters.findFirstIn(lines.head).isDefined) {
          lines.drop(1)
        } else 
          lines
        
      val vec: Vector[Byte] = linesDrop.toStream.takeWhile { (s: String) => !s.trim.isEmpty() }.flatMap {
        (s: String) => s.split(",").map { _.trim.toByte }
      } toVector;
      
      val filenameArr = filename.split(Pattern.quote("/"))
      new MWRulesetAgent( filenameArr.last.replace(".agent", ""), Ruleset.build(vec))
    } catch {
      case e: FileNotFoundException => throw new IllegalArgumentException("Could not find agent file.", e)
      case e: NumberFormatException => throw new IllegalArgumentException("Non-number found in agent file (after header).", e)
    } finally {
      if (bufferedSource.isDefined) bufferedSource.get.close
    }
  }
  
  def toFile(filename: String, agent: MWRulesetAgent, header: Boolean): Unit = {
    val filenameFull = if(!filename.contains(".")) {
      filename + postfix
    } else filename

    val rules = agent.ruleset.rules
    val defaultA = agent.ruleset.defaultAction
    
    var writerOpt: Option[FileWriter] = None;
    try {
      val dir: File  = new File(writeFolder);
      if (!dir.exists()) { dir.mkdirs() }
      
      writerOpt = Some(new FileWriter(writeFolder + filenameFull))
      
      val writer = writerOpt.get
      if(header) writer.append(CSV_HEADER + "\n")
      rules.foreach { (r: Rule) => {
        writer.append(r.getVectorRep.mkString(", "))
        writer.append("\n")
      }}
      writer.append((Vector.fill(Conditions.LENGTH)(-1:Byte) ++ defaultA).mkString(", "))
      writer.append("\n")

      writer.append("\n\n")
      writer.flush()
    } catch {
      case e: IOException => throw new IllegalArgumentException("File inaccessible or is a folder, or error on flush", e)
    } finally {
      if (writerOpt.isDefined) writerOpt.get.close
    }

  }
  
}