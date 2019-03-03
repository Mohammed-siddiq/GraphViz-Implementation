package com.uic.cs

import java.io.File

import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.parse.Parser
import info.leadinglight.jdot._
import info.leadinglight.jdot.enums._

import scala.io.Source

object VisualizeCSAuthors {


  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  //Generate dot file from the output files
  def readOutputFile(directory: String) = {

    var totalWeights = 0
    var relationsMap = scala.collection.mutable.Map[String, Int]()
    var authorSet = scala.collection.mutable.Map[String, Int]()
    val files = getListOfFiles(dir = directory)
    for (file <- files) {
      for (line <- Source.fromFile(file).getLines) {

        val authorAndWeight = line.split("\t")

        if (authorAndWeight(0).contains(",")) {
          //line describing the relation -> add to map
          if (relationsMap.contains(authorAndWeight(0))) // if already exists then add the value
            relationsMap(authorAndWeight(0)) = relationsMap(authorAndWeight(0)) + authorAndWeight(1).toInt
          else {
            relationsMap += (authorAndWeight(0) -> authorAndWeight(1).toInt)

          }

        }
        else {
          // if individual author add to the author set
          if (authorSet.contains(authorAndWeight(0)))
            authorSet(authorAndWeight(0)) = authorSet(authorAndWeight(0)) + authorAndWeight(0).toInt
          else
            authorSet += (authorAndWeight(0) -> authorAndWeight(1).toInt)
          totalWeights += authorAndWeight(1).toInt
        }
      }
    }

    val g = new Graph("MySample")

    for ((author, weight) <- authorSet) {
      g.addNode(new Node(author).setLabel(author + " : " + weight.toString).setShape(Shape.circle))
    }
    for ((authorsMapping, weight) <- relationsMap) {
      g.addEdge(
        new Edge().addNode(authorsMapping.split(",")(0)).addNode(authorsMapping.split(",")(1)).setLabel(weight.toString).setArrowHead(ArrowType.none))
    }

    //Generating graph DOT file :
    g.toDot
  }

  def main(args: Array[String]): Unit = {

    //    val inputFile = args(1)
    //    val outputFile = args(2)
    val dot = readOutputFile("/Users/mohammedsiddiq/Downloads/Output")
    val g: MutableGraph = Parser.read(dot)

    Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("myoutput"))
    //    print()


  }


}
