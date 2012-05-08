package com.github.omnomnom.spacecore

import org.lwjgl.util.vector.Vector3f

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

class OBJModel(
                path: String,
                pt: Vector3f = new Vector3f(),
                yaw: Float = 0f
                ) extends Model(pt = pt, yaw = yaw) with OBJParser {
  loadModel(path)
}
