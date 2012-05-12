package com.github.omnomnom.spacecore

import collision.BoundingBoxCreator
import util.Random

import org.lwjgl.util.vector.Vector3f
import org.lwjgl.opengl.GL11._
import org.clapper.avsl.Logger

/**
 * @author Golikov Konstantine <kostya-kostya@yandex-team.ru>
 */

class World {

  private val log = Logger(classOf[World])

  log.debug("New world created")

  // Box size
  val SkyboxSize = 32.0f;
  val WorldSize = 1024.0f;
  private val stars = Seq.fill(1000)(new StarPoint(SkyboxSize))

  val Ship: ScalaPlayerShip = new ScalaPlayerShip

  private val models: Seq[OBJModel] = {
    val road = new OBJModel("Road.obj") with BoundingBoxCreator

    def randomPlaceOnSurface = new Vector3f(
      (Random.nextDouble() * 2.0 - 1.0).toFloat * SkyboxSize,
      0f,
      (Random.nextDouble() * 2.0 - 1.0).toFloat * SkyboxSize
    )

    val rocks = {
      val N = 4
      for (i <- 1 to 100) yield {
        new OBJModel(
          path = "Rock" + (1 + Random.nextInt(N)) + ".obj",
          yaw = (Random.nextDouble() * 2.0 * math.Pi).toFloat,
          // randomly place rocks in the world
          pt = randomPlaceOnSurface
        )
      }
    }

    val hangar = new OBJModel(
      path = "m/open_box.obj",
      pt = randomPlaceOnSurface
    )

    val inAir = randomPlaceOnSurface
    //    inAir.setY(0.2f * SkyboxSize)

    val bunnies = Seq.fill(10)(
      new OBJModel(
        path = "m/bunny_200.obj",
        pt = inAir
      ) with BoundingBoxCreator
    )

    Seq(road, hangar) ++
    //      rocks ++
    bunnies;

  }

  log.debug("All models are loaded")

  def Render(pos: Vector3f, yaw: Float) {
    log.debug("Rendering")
    // Rotate (yaw) as needed so the player always faces non-corners
    glPushMatrix();

    // Rotate and translate
    glTranslatef(pos.x, pos.y, pos.z);
    glRotatef(yaw, 0f, 1f, 0f);

    // Render the skybox and stars
    renderSkybox();

    // Be done
    glPopMatrix();

    // Render out the stars
    glPushMatrix();

    // Show stars
    glTranslatef(pos.x, pos.y * 0.99f, pos.z);
    renderStars();

    // Be done
    glPopMatrix();

    // Draw stars
    glPushMatrix();

    // Render ground and right below
    glTranslatef(pos.x, 0, pos.z);

    val color = new Vector3f(236.0f / 255.0f, 200.0f / 255.0f, 122.0f / 255.0f);
    renderGround(WorldSize, color);

    glPopMatrix();

    models.foreach {
      case model: OBJModel with BoundingBoxCreator =>
        glPushMatrix();
        // Render ground and right below
        glTranslatef(model.pt.x, model.pt.y, model.pt.z);
        glRotatef(java.lang.Math.toDegrees(model.yaw).toFloat, 0, 1, 0);
        if (model.bb.intersects(Ship.bb)) {
          glColor3f(0.5f, 0, 0)
        }
        else
          glColor3f(1f, 1f, 1f)
        model.Render()
        glPopMatrix();
      case model =>
        glPushMatrix();
        // Render ground and right below
        glTranslatef(model.pt.x, model.pt.y, model.pt.z);
        glRotatef(java.lang.Math.toDegrees(model.yaw).toFloat, 0, 1, 0);
        model.Render()
        glPopMatrix();
    }

    Ship.Render()
  }

  private def renderSkybox() {
    // Define the top and bottom color
    val topColor = new Vector3f(204f / 255f, 255f / 255f, 255f / 255f);
    val bottomColor = new Vector3f(207f / 255f, 179f / 255f, 52f / 255f);

    // Save matrix
    glPushMatrix();

    // Draw out top side
    glBegin(GL_QUADS);

    // Polygon & texture map
    // Top has one constant color
    glColor3f(topColor.x, topColor.y, topColor.z);
    glVertex3f(-SkyboxSize, SkyboxSize, -SkyboxSize);
    glVertex3f(SkyboxSize, SkyboxSize, -SkyboxSize);
    glVertex3f(SkyboxSize, SkyboxSize, SkyboxSize);
    glVertex3f(-SkyboxSize, SkyboxSize, SkyboxSize);

    glEnd();

    // Draw out the left side
    glBegin(GL_QUADS);

    // Polygon & texture map
    glColor3f(topColor.x, topColor.y, topColor.z);
    glVertex3f(SkyboxSize, SkyboxSize, -SkyboxSize);
    glColor3f(bottomColor.x, bottomColor.y, bottomColor.z);
    glVertex3f(SkyboxSize, -SkyboxSize, -SkyboxSize);
    glVertex3f(SkyboxSize, -SkyboxSize, SkyboxSize);
    glColor3f(topColor.x, topColor.y, topColor.z);
    glVertex3f(SkyboxSize, SkyboxSize, SkyboxSize);

    glEnd();

    // Draw out the right side
    glBegin(GL_QUADS);

    // Polygon & texture map
    glColor3f(topColor.x, topColor.y, topColor.z);
    glVertex3f(-SkyboxSize, SkyboxSize, SkyboxSize);
    glColor3f(bottomColor.x, bottomColor.y, bottomColor.z);
    glVertex3f(-SkyboxSize, -SkyboxSize, SkyboxSize);
    glVertex3f(-SkyboxSize, -SkyboxSize, -SkyboxSize);
    glColor3f(topColor.x, topColor.y, topColor.z);
    glVertex3f(-SkyboxSize, SkyboxSize, -SkyboxSize);

    glEnd();

    // Drow out the front side
    glBegin(GL_QUADS);

    // Polygon & texture map
    glColor3f(topColor.x, topColor.y, topColor.z);
    glVertex3f(SkyboxSize, SkyboxSize, SkyboxSize);
    glColor3f(bottomColor.x, bottomColor.y, bottomColor.z);
    glVertex3f(SkyboxSize, -SkyboxSize, SkyboxSize);
    glVertex3f(-SkyboxSize, -SkyboxSize, SkyboxSize);
    glColor3f(topColor.x, topColor.y, topColor.z);
    glVertex3f(-SkyboxSize, SkyboxSize, SkyboxSize);

    glEnd();

    // Drow out the back side
    glBegin(GL_QUADS);

    // Polygon & texture map
    glColor3f(topColor.x, topColor.y, topColor.z);
    glVertex3f(-SkyboxSize, SkyboxSize, -SkyboxSize);
    glColor3f(bottomColor.x, bottomColor.y, bottomColor.z);
    glVertex3f(-SkyboxSize, -SkyboxSize, -SkyboxSize);
    glVertex3f(SkyboxSize, -SkyboxSize, -SkyboxSize);
    glColor3f(topColor.x, topColor.y, topColor.z);
    glVertex3f(SkyboxSize, SkyboxSize, -SkyboxSize);

    glEnd();

    // Place back matrix
    glPopMatrix();
  }

  // Draw the bottom level
  private def renderGround(WorldLength: Float, Color: Vector3f) {
    // Translate to position
    glPushMatrix();
    // Set the ship color to red for now
    glColor3f(Color.x, Color.y, Color.z);
    glBegin(GL_QUADS);
    glVertex3f(-WorldLength, 0, -WorldLength);
    glVertex3f(WorldLength, 0, -WorldLength);
    glVertex3f(WorldLength, 0, WorldLength);
    glVertex3f(-WorldLength, 0, WorldLength);
    glEnd();
    // Done
    glPopMatrix();
  }

  private def renderStars() {
    stars.foreach {
      star =>
        glPointSize(star.scale);
        glColor3f(star.color.x, star.color.y, star.color.z);
        glBegin(GL_POINTS);
        glVertex3f(star.pt.x, star.pt.y, star.pt.z);
        glEnd();
    }
  }

}

