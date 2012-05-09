package com.github.omnomnom.spacecore.particle

import org.lwjgl.util.vector.Vector3f
import util.Random

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 * @date 09.05.12
 * @subject:
 */

class FogParticle extends Particle {
  def velos() = new Vector3f(
    0.005f - Random.nextFloat() / 1000f,
    0.01f - Random.nextFloat() / 1000f,
    0.0005f - Random.nextInt(100).toFloat
  )

  val color = new Vector3f(0.5f, 0.5f, 0.5f)
}