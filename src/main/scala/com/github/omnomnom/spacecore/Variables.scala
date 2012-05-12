package com.github.omnomnom.spacecore

import org.lwjgl.util.vector.{Quaternion, Vector3f}


/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

trait Variables {
  protected var forward = new Vector3f(0, 0, 1)
  protected var up = new Vector3f(0, 1, 0)
  protected var right = new Vector3f(-1, 0, 0)
  protected var pitch: Double = _
  protected var roll: Double = _
  protected var position = new Vector3f(0, 0.1f, 0)
  var realVelocity: Float = _
  var targetVelocity: Float = _
  protected var bounced = false;
  protected var crashed = false;

  protected val qResult = new Quaternion()
}
