package com.github.omnomnom.spacecore.collision

import org.lwjgl.util.vector.Vector3f
import com.github.omnomnom.spacecore.Model

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 * @date 12.05.12
 * @subject:
 */

case class BoundingBox(min: Vector3f, max: Vector3f) {
  def intersects(other: BoundingBox): Boolean = {
    (min.x < other.max.x) && (max.x > other.min.x) &&
      (min.y < other.max.y) && (max.y > other.min.y) &&
      (min.z < other.max.z) && (max.z > other.min.z);
  }
}

trait BoundingBoxCreator {
  model: Model =>
  def x = v.map(u =>
    Vector3f.add(pt, u, null)
  )

  // TODO: not optimal -- remake

  private val bbMax = new Vector3f(
    x.map(u => u.x).max,
    x.map(u => u.y).max,
    x.map(u => u.z).max
  )

  private val bbMin = new Vector3f(
    x.map(u => u.x).min,
    x.map(u => u.y).min,
    x.map(u => u.z).min
  )

  def bb = BoundingBox(bbMin, bbMax)
}