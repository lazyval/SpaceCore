package com.github.omnomnom.spacecore

import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Vector3f
import org.lwjgl.BufferUtils

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

class ScalaPlayerShip extends OBJModel("Sample.obj")
with ShipUtilities
with Variables
with State {

  protected val VEL_dMAX = 0.005f;
  protected val VEL_MAX = 0.15f;

  var Pitch: Double = _
  var Roll: Double = _

  protected var Position = new Vector3f(0, 0.1f, 0)

  var RealVelocity: Float = _
  protected var targetVelocity: Float = _

  protected var Bounced = false;
  protected var Crashed = false;

  override def Render() {
    // Translate to position
    GL11.glPushMatrix();
    GL11.glTranslatef(Position.x, Position.y, Position.z);

    // Why isn't this a built-in feature of LWJGL
    val QMatrix = new Array[Float](16);
    createMatrix(QMatrix, QResult);

    val Buffer = BufferUtils.createFloatBuffer(16);
    //    Buffer.put(QMatrix);
    //    Buffer.position(0);

    GL11.glMultMatrix(Buffer);

    GL11.glLineWidth(2.0f);
    GL11.glBegin(GL11.GL_LINES);
    GL11.glColor3f(1, 0.5f, 0.5f);
    GL11.glVertex3f(0, 0, 0);
    GL11.glVertex3f(1, 0, 0);

    GL11.glColor3f(0.5f, 1, 0.5f);
    GL11.glVertex3f(0, 0, 0);
    GL11.glVertex3f(0, 1, 0);

    GL11.glColor3f(0.5f, 0.5f, 1);
    GL11.glVertex3f(0, 0, 0);
    GL11.glVertex3f(0, 0, 1);
    GL11.glEnd();

    // Set width to a single line
    GL11.glLineWidth(1);

    // Reset back to regular faces
    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

    // Done
    GL11.glPopMatrix();

    // Render the shadow (view-volume)
    // Note: we render the shadow independant of the model's translation and rotation
    // THOUGH NOTE: we do translate the shadow up a tiny bit off the ground so it doesnt z-fight
    GL11.glPushMatrix();

    GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
    GL11.glPolygonOffset(-1.0f, -1.0f);

    GL11.glTranslatef(0, 0.001f, 0);
    renderShadow(Position);

    GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);

    GL11.glPopMatrix();
  }

  def GetCameraVectors(cameraPos: Vector3f, cameraTarget: Vector3f, cameraUp: Vector3f) {
    cameraPos.set(Position.x, Position.y, Position.z)
    cameraTarget.set(Forward.x + Position.x, Forward.y + Position.y, Forward.z + Position.z)
    cameraUp.set(Up.x, Up.y, Up.z)
  }

  def renderShadow(Translation: Vector3f) {

    // TODO: there is bug when trying to draw a shadow. Fix latter
    //    val vertices = model.v.map {
    //      v =>
    //        var vt: Vector3f = new Vector3f(v)
    //        vt = ApplyQuatToPoint(QResult, vt)
    //        Vector3f.add(vt, Translation, vt)
    //    }
    //
    //    val maxD = vertices.map(_.y).max
    //
    //    Position.y += math.abs(maxD)
    //    Bounced = true
    //
    //    val LightPos: Vector3f = new Vector3f(0, 1000, 0)
    //
    //    GL11.glBegin(GL11.GL_TRIANGLES)
    //
    //
    //    var ff: Face = null
    //    try {
    //    model.f.foreach {
    //      face =>
    //        ff = face
    //        GL11.glColor3f(0.4f, 0.4f, 0.4f)
    //        val v1: Vector3f = getPlaneIntersect(v((face.vertex.x - 1).toInt), LightPos)
    //        GL11.glVertex3f(v1.x, v1.y, v1.z)
    //        val v2: Vector3f = getPlaneIntersect(v((face.vertex.y - 1).toInt), LightPos)
    //        GL11.glVertex3f(v2.x, v2.y, v2.z)
    //        val v3: Vector3f = getPlaneIntersect(v((face.vertex.z - 1).toInt), LightPos)
    //        GL11.glVertex3f(v3.x, v3.y, v3.z)
    //    }
    //    GL11.glEnd
    //    } catch {
    //      case e: IndexOutOfBoundsException => println(
    //      """
    //      model.f: %s
    //      ff: %s
    //      """.format(model.f, ff.vertex)
    //      )
    //      throw e
    //    }
  }

  def GetYaw: Float = {
    val FFlat = new Vector3f(Forward.x, 0f, Forward.z)
    val RFlat = new Vector3f(1f, 0f, 0f)
    val ang = Vector3f.angle(RFlat, FFlat)
    if (Vector3f.cross(RFlat, FFlat, null).y < 0)
      (math.Pi * 2.0).toFloat - ang
    else
      ang
  }

}
