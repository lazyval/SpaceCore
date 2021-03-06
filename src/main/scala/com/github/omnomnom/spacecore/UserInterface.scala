package com.github.omnomnom.spacecore

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 * @date 12.05.12
 */

import org.lwjgl.opengl.GL11._

object UserInterface {

  def Render(RealVelocity: Float, TargetVelocity: Float, MaxVelocity: Float) {
    glPushMatrix()
    glLoadIdentity()
    val RScale: Float = 1f - (RealVelocity / MaxVelocity)
    val TScale: Float = 1f - (TargetVelocity / MaxVelocity)
    glColor3f(1f / 255f, 36f / 255f, 59f / 255f)
    glBegin(GL_QUADS)
    glVertex2f(32f, 200f)
    glVertex2f(64f, 200f)
    glVertex2f(64f, 32f)
    glVertex2f(32f, 32f)
    glEnd()
    glColor3f(157f / 255f, 167f / 255f, 178f / 255f)
    glBegin(GL_QUADS)
    glVertex2f(37f, 195f)
    glVertex2f(50f, 195f)
    glVertex2f(50f, 37f + 158f * RScale)
    glVertex2f(37f, 37f + 158f * RScale)
    glEnd()
    glColor3f(197f / 255f, 197f / 255f, 197f / 255f)
    glBegin(GL_QUADS)
    glVertex2f(50f, 195f)
    glVertex2f(59f, 195f)
    glVertex2f(59f, 37f + 158f * TScale)
    glVertex2f(50f, 37f + 158f * TScale)
    glEnd()
    glPopMatrix()
  }
}

