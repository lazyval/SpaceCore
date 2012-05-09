package com.github.omnomnom.spacecore.particle

import util.Random
import org.lwjgl.util.vector.Vector3f

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 * @date 09.05.12
 *
 *       for details look this tutorial http://www.naturewizard.com/tutorial08.html
 */


abstract class Particle() {
  private val decay = 0.01
  private var lifetime = 0.0

  val pos = new Vector3f()
  val color: Vector3f
  val velocity = new Vector3f()

  def velos(): Vector3f

  reset()

  def reset() {
    lifetime = 1 + Random.nextInt(5).toDouble / 10
    pos.set(0, 0, 0)
    velocity.set(velos)
  }

  def evolve() {
    lifetime -= decay
    if (lifetime < 0 || pos.y < 0) reset()
    else {
      Vector3f.add(pos, velocity, pos)
      velocity.setY(velocity.y - 0.00007f)
    }
  }
}