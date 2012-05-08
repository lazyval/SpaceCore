package com.github.omnomnom.spacecore

import util.Random
import org.lwjgl.util.vector.Vector3f

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

class StarPoint(skyboxSize: Float) {

  import math._

  val u = 2f * Random.nextDouble() - 1f
  val v = Random.nextDouble() * 2 * Pi

  val x = (sqrt(1f - u * u) * cos(v)).toFloat
  val y = (sqrt(1f - u * u) * sin(v)).toFloat
  val z = abs(u).toFloat

  val pt = new Vector3f(x, y, z)

  pt.scale(skyboxSize / 2)

  val scale = 3f * Random.nextFloat()

  private val gray: Float = 0.5f + 0.5f * Random.nextFloat()

  val color = new Vector3f(gray, gray, gray)
}