package com.github.omnomnom.spacecore.particle

import org.lwjgl.opengl.GL11._

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 * @date 09.05.12
 *
 */

class ParticlePool(val N: Int = 12000) {

  var particles = Seq.fill(N / 2)(new FogParticle()) ++ Seq.fill(N / 2)(new FlameParticle())

  def Render() {

    particles.foreach(p => {
      glColor3f(p.color.x, p.color.y, p.color.z)
      glBegin(GL_TRIANGLE_STRIP)
      // top right
      glTexCoord2d(0.0, 1.0)
      glVertex3f(p.pos.x + 0.002f, p.pos.y + 0.002f, p.pos.z)
      // top left
      glTexCoord2d(0.0, 0.0)
      glVertex3f(p.pos.x - 0.002f, p.pos.y + 0.002f, p.pos.z)
      // bottom right
      glTexCoord2d(1.0, 0.0)
      glVertex3f(p.pos.x + 0.002f, p.pos.y - 0.002f, p.pos.z)
      // bottom left
      glTexCoord2d(1.0, 1.0)
      glVertex3f(p.pos.x - 0.002f, p.pos.y + 0.002f, p.pos.z)
      glEnd()
      p.evolve()
    }
    )
  }

  // to get flame
  //  velocity.set(
  //  )


}