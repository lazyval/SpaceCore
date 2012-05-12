package com.github.omnomnom.spacecore

import collection.mutable.ListBuffer
import org.lwjgl.util.vector.Vector3f
import org.lwjgl.opengl.GL11._

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

class Model(
             val v: ListBuffer[Vector3f] = new ListBuffer[Vector3f](),
             val f: ListBuffer[Face] = new ListBuffer[Face](),
             val n: ListBuffer[Vector3f] = new ListBuffer[Vector3f](),
             val pt: Vector3f = new Vector3f(),
             val yaw: Float = 0f
             ) {

  def Render() {
    glLineWidth(1);

    r(GL_FILL)
    r(GL_LINE)

    def r(mode: Int) {
      glPolygonMode(GL_FRONT_AND_BACK, mode);
      glBegin(GL_TRIANGLES);

      f.foreach {
        face: Face => {
          val v1 = v((face.vertex.x - 1).toInt);
          glVertex3f(v1.x, v1.y, v1.z);
          val v2 = v((face.vertex.y - 1).toInt);
          glVertex3f(v2.x, v2.y, v2.z);
          val v3 = v((face.vertex.z - 1).toInt);
          glVertex3f(v3.x, v3.y, v3.z);
        }
      }
      glEnd();
    }
  }

}