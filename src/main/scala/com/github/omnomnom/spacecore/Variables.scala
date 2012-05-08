package com.github.omnomnom.spacecore

import org.lwjgl.util.vector.{Quaternion, Vector3f}


/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

trait Variables {
  protected var Forward = new Vector3f(0, 0, 1)
  protected var Up = new Vector3f(0, 1, 0)
  protected var Right = new Vector3f(-1, 0, 0)
  protected var Pitch: Double = _
  protected var Roll: Double = _

  protected var Position = new Vector3f(0, 0.1f, 0)

  protected var RealVelocity: Float = _
  protected var targetVelocity: Float = _

  protected var Bounced = false;
  protected var Crashed = false;


  protected val VEL_dMAX = 0.005f;
  protected val VEL_MAX = 0.15f;

  protected val QResult = new Quaternion()

}
