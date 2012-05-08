package com.github.omnomnom.spacecore

import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.{Vector4f, Quaternion, Vector3f}

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

trait State {
  self: Variables =>

  def Update() {
    if (Crashed) {
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

    if (targetVelocity > RealVelocity) RealVelocity += VEL_dMAX * 0.5f
    else if (targetVelocity < RealVelocity) RealVelocity -= VEL_dMAX * 0.5f
    Pitch += dPitch
    Roll += dRoll
    Forward.scale(math.cos(dPitch).asInstanceOf[Float])
    Up.scale(math.sin(dPitch).asInstanceOf[Float])
    Forward = Vector3f.add(Forward, Up, null)
    Up = Vector3f.cross(Right, Forward, null)
    Forward.normalise()
    Up.normalise()
    Right.scale(Math.cos(dRoll).asInstanceOf[Float])
    Up.scale(Math.sin(dRoll).asInstanceOf[Float])
    Right = Vector3f.add(Right, Up, null)
    Up = Vector3f.cross(Right, Forward, null)
    Right.normalise()
    Up.normalise()
    val ForwardCopy: Vector3f = new Vector3f(Forward)
    ForwardCopy.scale(RealVelocity)
    var Gravity: Float = 0.05f
    val NVelocity: Float = math.min((RealVelocity / VEL_MAX) * 3, 1)
    val TotalUp: Vector3f = new Vector3f(Up)
    TotalUp.scale(NVelocity * Gravity)
    TotalUp.y -= Gravity
    Vector3f.add(Position, ForwardCopy, Position)
    val QRoll: Quaternion = new Quaternion
    QRoll.setFromAxisAngle(new Vector4f(Forward.x, Forward.y, Forward.z, dRoll.toFloat))
    val QPitch: Quaternion = new Quaternion
    QPitch.setFromAxisAngle(new Vector4f(Right.x, Right.y, Right.z, -dPitch.toFloat))
    Quaternion.mul(QResult, QRoll, QResult)
    Quaternion.mul(QResult, QPitch, QResult)
    QResult.normalise()
  }
}
