package com.github.omnomnom.spacecore

import org.lwjgl.util.vector.Vector3f
import org.lwjgl.opengl.{DisplayMode, Display}
import org.lwjgl.input.{Mouse, Keyboard}
import org.lwjgl.opengl.GL11._
import org.lwjgl.util.glu.GLU._
import org.lwjgl.util.glu.GLU

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

object Main extends App {
  final val DISPLAY_HEIGHT = 900
  final val DISPLAY_WIDTH = 1400

  val world = new World
  val CameraPos, CameraTarget, CameraUp = new Vector3f
  var tailPlaneCamera = false
  var Time: Float = _

  Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT))
  Display.setVSyncEnabled(true)
  Display.setFullscreen(false)
  Display.setTitle("SpaceCore lab. Dialog systems.")
  Display.create
  Keyboard.create
  Mouse.setGrabbed(false)
  Mouse.create

  initGL
  resizeGL
  run()

  def initGL {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    glDisable(GL_DEPTH_TEST)
  }


  def destroy {
    Mouse.destroy
    Keyboard.destroy
    Display.destroy
  }

  def update {
    if (Keyboard.isKeyDown(Keyboard.KEY_Q)) tailPlaneCamera = !tailPlaneCamera
    world.Ship.Update
  }

  def resizeGL2D() {
    glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    gluOrtho2D(0.0f, DISPLAY_WIDTH.toFloat, DISPLAY_HEIGHT.toFloat, 0.0f)
    glMatrixMode(GL_MODELVIEW)
    glDisable(GL_DEPTH_TEST)
  }

  def resizeGL() {
    glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    gluPerspective(45.0f, (DISPLAY_WIDTH.toFloat / DISPLAY_HEIGHT.toFloat), 0.1f, 100.0f)
    glMatrixMode(GL_MODELVIEW)
    glClearDepth(1.0f)
    glEnable(GL_DEPTH_TEST)
    glDepthFunc(GL_LEQUAL)
  }

  def run() {
    while (!Display.isCloseRequested && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
      if (Display.isVisible) {
        update
        render
      }
      else {
        if (Display.isDirty) {
          render
        }
        try {
          Thread.sleep(100)
        }
        catch {
          case ex: InterruptedException => {
          }
        }
      }
      Display.update
      Display.sync(60)
    }
  }

  def render {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    glLoadIdentity
    resizeGL
    Time += 0.001f
    val CDist = 6f
    world.Ship.GetCameraVectors(CameraPos, CameraTarget, CameraUp)
    if (tailPlaneCamera) {
      val Dir: Vector3f = new Vector3f
      Vector3f.sub(CameraPos, CameraTarget, Dir)
      Dir.normalise()
      Dir.scale(4)
      Dir.y += 0.1f
      Vector3f.add(CameraPos, Dir, CameraPos)
      CameraPos.y += 1
      if (CameraPos.y < 0.01f) CameraPos.y = 0.01f
      GLU.gluLookAt(CameraPos.x, CameraPos.y, CameraPos.z, CameraTarget.x, CameraTarget.y, CameraTarget.z, CameraUp.x, CameraUp.y, CameraUp.z)
    }
    else {
      GLU.gluLookAt(CDist * math.cos(Time).toFloat, CDist, CDist * math.sin(Time).toFloat, CameraPos.x, CameraPos.y, CameraPos.z, 0, 1, 0)
    }
    val Yaw = java.lang.Math.toDegrees(world.Ship.GetYaw).toFloat
    world.Render(CameraPos, Yaw)
    resizeGL2D
    UserInterface.Render(world.Ship.realVelocity, world.Ship.targetVelocity, Constants.VEL_MAX)
  }
}