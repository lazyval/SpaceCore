package com.github.omnomnom.spacecore

import collision.{BoundingBoxCreator, BoundingBox}
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Vector3f
import org.lwjgl.BufferUtils
import particle.ParticlePool
import java.util.Random

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

class PlayerShip(modelFile: String) extends OBJModel(modelFile)
with ShipUtilities
with Variables
with State
with BoundingBoxCreator {
  protected val p = new ParticlePool

  position = new Vector3f(0, 0.1f, 0)

  override def Render() {
    GL11.glPushMatrix
    GL11.glTranslatef(position.x, position.y, position.z)
    val QMatrix = new Array[Float](16)
    createMatrix(QMatrix, qResult)
    val Buffer = BufferUtils.createFloatBuffer(16)
    Buffer.put(QMatrix)
    Buffer.position(0)
    GL11.glMultMatrix(Buffer)
    GL11.glLineWidth(2.0f)
    GL11.glBegin(GL11.GL_LINES)
    GL11.glColor3f(1, 0.5f, 0.5f)
    GL11.glVertex3f(0, 0, 0)
    GL11.glVertex3f(1, 0, 0)
    GL11.glColor3f(0.5f, 1, 0.5f)
    GL11.glVertex3f(0, 0, 0)
    GL11.glVertex3f(0, 1, 0)
    GL11.glColor3f(0.5f, 0.5f, 1)
    GL11.glVertex3f(0, 0, 0)
    GL11.glVertex3f(0, 0, 1)
    GL11.glEnd

    //    p.Render

    GL11.glLineWidth(1)

    // TODO: remake

    for (i <- 0 to 2) {
      if (i == 0) GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL)
      else GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)
      val SurfaceRand: Random = new Random(123456)
      GL11.glBegin(GL11.GL_TRIANGLES)
      for (face <- f) {
        if (i == 0) GL11.glColor3f(0.8f, 0.8f, 0.5f + 0.5f * (SurfaceRand.nextFloat))
        else GL11.glColor3f(0.4f, 0.4f, 0.2f + 0.2f * (SurfaceRand.nextFloat))
        val v1 = v(face.vertex.x.asInstanceOf[Int] - 1)
        GL11.glVertex3f(v1.x, v1.y, v1.z)
        val v2 = v(face.vertex.y.asInstanceOf[Int] - 1)
        GL11.glVertex3f(v2.x, v2.y, v2.z)
        val v3 = v(face.vertex.z.asInstanceOf[Int] - 1)
        GL11.glVertex3f(v3.x, v3.y, v3.z)
      }
      GL11.glEnd
    }

    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL)
    GL11.glPopMatrix
    GL11.glPushMatrix
    GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL)
    GL11.glPolygonOffset(-1.0f, -1.0f)
    GL11.glTranslatef(0, 0.001f, 0)
    renderShadow(position)
    GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL)
    GL11.glPopMatrix
  }

  def GetCameraVectors(cameraPos: Vector3f, cameraTarget: Vector3f, cameraUp: Vector3f) {
    cameraPos.set(position.x, position.y, position.z)
    cameraTarget.set(forward.x + position.x, forward.y + position.y, forward.z + position.z)
    cameraUp.set(up.x, up.y, up.z)
  }

  protected def renderShadow(Translation: Vector3f) {

    val vertices = v.map {
      v =>
        var vt = new Vector3f(v)
        vt = ApplyQuatToPoint(qResult, vt)
        Vector3f.add(vt, Translation, vt)
    }

    val maxD = (vertices.map(_.y).filter(_ < 0) ++ Seq(0f)).min

    position.y += math.abs(maxD)
    bounced = true

    val lightPos = new Vector3f(0, 1000, 0)

    GL11.glBegin(GL11.GL_TRIANGLES)

    f.foreach {
      face =>
        GL11.glColor3f(0.4f, 0.4f, 0.4f)
        val v1 = getPlaneIntersect(vertices((face.vertex.x - 1).toInt), lightPos)
        GL11.glVertex3f(v1.x, v1.y, v1.z)
        val v2 = getPlaneIntersect(vertices((face.vertex.y - 1).toInt), lightPos)
        GL11.glVertex3f(v2.x, v2.y, v2.z)
        val v3 = getPlaneIntersect(vertices((face.vertex.z - 1).toInt), lightPos)
        GL11.glVertex3f(v3.x, v3.y, v3.z)
    }
    GL11.glEnd
  }

  def GetYaw: Float = {
    val FFlat = new Vector3f(forward.x, 0f, forward.z)
    val RFlat = new Vector3f(1f, 0f, 0f)
    val ang = Vector3f.angle(RFlat, FFlat)
    if (Vector3f.cross(RFlat, FFlat, null).y < 0)
      (math.Pi * 2.0).toFloat - ang
    else
      ang
  }


  override def bb = {
    def x = v.map(u => {
      if (position == null)
        position = new Vector3f(0, 0, 0)
      Vector3f.add(position, u, null)
    }
    )

    val bbMax = new Vector3f(
      x.map(u => u.x).max,
      x.map(u => u.y).max,
      x.map(u => u.z).max
    )

    val bbMin = new Vector3f(
      x.map(u => u.x).min,
      x.map(u => u.y).min,
      x.map(u => u.z).min
    )

    BoundingBox(bbMin, bbMax)
  }

}
