package com.github.omnomnom.spacecore

import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.{Vector4f, Quaternion, Vector3f}

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

trait State {
  self: Variables =>

  def Update() {
    import Constants._
    if (crashed) {
      System.out.println("Crashed!")
    }
    var dPitch: Double = 0
    var dRoll: Double = 0
    if (Keyboard.isKeyDown(Keyboard.KEY_W)) dPitch -= 0.03
    if (Keyboard.isKeyDown(Keyboard.KEY_S)) dPitch += 0.03
    if (Keyboard.isKeyDown(Keyboard.KEY_A)) dRoll += 0.05
    if (Keyboard.isKeyDown(Keyboard.KEY_D)) dRoll -= 0.05
    if (Keyboard.isKeyDown(Keyboard.KEY_R)) targetVelocity += VEL_dMAX
    if (Keyboard.isKeyDown(Keyboard.KEY_F)) targetVelocity -= VEL_dMAX
    targetVelocity = math.max(math.min(targetVelocity, VEL_MAX), 0.0f)

    if (targetVelocity > realVelocity) realVelocity += VEL_dMAX * 0.5f
    else if (targetVelocity < realVelocity) realVelocity -= VEL_dMAX * 0.5f
    pitch += dPitch
    roll += dRoll
    forward.scale(math.cos(dPitch).toFloat)
    up.scale(math.sin(dPitch).toFloat)
    forward = Vector3f.add(forward, up, null)
    up = Vector3f.cross(right, forward, null)
    forward.normalise()
    up.normalise()
    right.scale(math.cos(dRoll).toFloat)
    up.scale(math.sin(dRoll).toFloat)
    right = Vector3f.add(right, up, null)
    up = Vector3f.cross(right, forward, null)
    right.normalise()
    up.normalise()
    val ForwardCopy: Vector3f = new Vector3f(forward)
    ForwardCopy.scale(realVelocity)
    var Gravity: Float = 0.05f
    val NVelocity: Float = math.min((realVelocity / VEL_MAX) * 3, 1)
    val TotalUp: Vector3f = new Vector3f(up)
    TotalUp.scale(NVelocity * Gravity)
    TotalUp.y -= Gravity
    Vector3f.add(position, ForwardCopy, position)
    val QRoll: Quaternion = new Quaternion
    QRoll.setFromAxisAngle(new Vector4f(forward.x, forward.y, forward.z, dRoll.toFloat))
    val QPitch: Quaternion = new Quaternion
    QPitch.setFromAxisAngle(new Vector4f(right.x, right.y, right.z, -dPitch.toFloat))
    Quaternion.mul(qResult, QRoll, qResult)
    Quaternion.mul(qResult, QPitch, qResult)
    qResult.normalise()
  }
}
