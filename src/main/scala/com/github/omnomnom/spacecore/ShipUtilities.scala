package com.github.omnomnom.spacecore

import org.lwjgl.util.vector.{Quaternion, Vector3f}


/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

trait ShipUtilities {
  vars: Variables =>

  // Returns the intersection point of the vector (described as two points)
  // onto the y=0 plane (or simply the XZ plane)
  def getPlaneIntersect(vf: Vector3f, vi: Vector3f): Vector3f = {
    val lineDir: Vector3f = Vector3f.sub(vf, vi, null)
    lineDir.normalise()
    val planeNormal = new Vector3f(0, 1, 0)
    val neg_Vi = new Vector3f(-vi.x, -vi.y, -vi.z)
    val d = Vector3f.dot(neg_Vi, planeNormal) / Vector3f.dot(lineDir, planeNormal)
    val pt = new Vector3f(lineDir)
    pt.scale(d)
    Vector3f.add(pt, vi, pt)
    pt
  }

  def ApplyQuatToPoint(Q: Quaternion, vt: Vector3f): Vector3f = {
    val QMatrix: Array[Float] = new Array[Float](16)
    createMatrix(QMatrix, QResult)
    new Vector3f(
      QMatrix(0) * vt.x + QMatrix(4) * vt.y + QMatrix(8) * vt.z,
      QMatrix(1) * vt.x + QMatrix(5) * vt.y + QMatrix(9) * vt.z,
      QMatrix(2) * vt.x + QMatrix(6) * vt.y + QMatrix(10) * vt.z
    )
  }


  def createMatrix(pMatrix: Array[Float], q: Quaternion): Unit = {

    pMatrix(0) = 1.0f - 2.0f * (q.y * q.y + q.z * q.z)
    pMatrix(1) = 2.0f * (q.x * q.y - q.w * q.z)
    pMatrix(2) = 2.0f * (q.x * q.z + q.w * q.y)
    pMatrix(3) = 0.0f
    pMatrix(4) = 2.0f * (q.x * q.y + q.w * q.z)
    pMatrix(5) = 1.0f - 2.0f * (q.x * q.x + q.z * q.z)
    pMatrix(6) = 2.0f * (q.y * q.z - q.w * q.x)
    pMatrix(7) = 0.0f
    pMatrix(8) = 2.0f * (q.x * q.z - q.w * q.y)
    pMatrix(9) = 2.0f * (q.y * q.z + q.w * q.x)
    pMatrix(10) = 1.0f - 2.0f * (q.x * q.x + q.y * q.y)
    pMatrix(11) = 0.0f
    pMatrix(12) = 0
    pMatrix(13) = 0
    pMatrix(14) = 0
    pMatrix(15) = 1.0f
  }


}
