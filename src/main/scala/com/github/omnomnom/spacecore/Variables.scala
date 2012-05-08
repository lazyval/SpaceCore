package com.github.omnomnom.spacecore

import org.lwjgl.util.vector.Vector3f

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

trait Variables {
  protected var Forward = new Vector3f(0, 0, 1)
  protected var Up = new Vector3f(0, 1, 0)
  protected var Right = new Vector3f(-1, 0, 0)
}
