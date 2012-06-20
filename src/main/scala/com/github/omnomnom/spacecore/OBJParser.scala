package com.github.omnomnom.spacecore

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

import org.lwjgl.util.vector.Vector3f
import io.Source
import org.clapper.avsl.Logger

trait OBJParser {
  container: Model =>

  val log = Logger(classOf[World])

  val RESOURCES = "src/main/resources/"

  def loadModel(path: String) {
    val filename = RESOURCES + path

    try {
      Source.fromFile(filename).getLines().foreach {
        line =>
          line.split(' ').toList match {
            case "v" :: rest =>
              val List(x, y, z) = rest.map(_.toFloat)
              // v defined in container: Model
              v += new Vector3f(x, y, z)
            case "f" :: rest =>
              val List(x, y, z) = rest.map(_.toFloat)
              // f defined in container: Model
              f += new Face(new Vector3f(x, y, z))
            case x => log.error("Unknown token" + x)
          }
      }
    } catch {
      case e: java.io.IOException => log.error("Problems with file %s IO".format(filename))
    }
    log.debug("Model " + path + " parsed succsessfully")
  }
}

