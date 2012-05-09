package com.github.omnomnom.spacecore;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.Random;

/**
 * 08.05.12
 *
 * @author Golikov Konstantine kostya-kostya@yandex-team.ru
 * @subject:
 */

public class PlayerShip extends ScalaPlayerShip {


    public void Render() {

        // Translate to position
        GL11.glPushMatrix();
        GL11.glTranslatef(position().x, position().y, position().z);

        // Why isn't this a built-in feature of LWJGL
        float[] QMatrix = new float[16];
        createMatrix(QMatrix, qResult());

        FloatBuffer Buffer = BufferUtils.createFloatBuffer(16);
        Buffer.put(QMatrix);
        Buffer.position(0);

        GL11.glMultMatrix(Buffer);


        // normals
        GL11.glLineWidth(2.0f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(1, 0.5f, 0.5f);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(1, 0, 0);

        GL11.glColor3f(0.5f, 1, 0.5f);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(0, 1, 0);

        GL11.glColor3f(0.5f, 0.5f, 1);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(0, 0, 1);
        GL11.glEnd();

        // emit particles
        p().Render();

        // Set width to a single line
        GL11.glLineWidth(1);

        // Change rendermode
        for (int i = 0; i < 2; i++) {
            if (i == 0)
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            else
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

            // Randomize surface color a bit
            Random SurfaceRand = new Random(123456);

            GL11.glBegin(GL11.GL_TRIANGLES);

            for (Face face : jf()) {

                // Always make black when in line mode)
                if (i == 0)
                    GL11.glColor3f(0.8f, 0.8f, 0.5f + 0.5f * (SurfaceRand.nextFloat()));
                else
                    GL11.glColor3f(0.4f, 0.4f, 0.2f + 0.2f * (SurfaceRand.nextFloat()));

                // Randomize the color a tiny bit
                Vector3f v1 = jv().get((int) face.vertex().x - 1);
                GL11.glVertex3f(v1.x, v1.y, v1.z);
                Vector3f v2 = jv().get((int) face.vertex().y - 1);
                GL11.glVertex3f(v2.x, v2.y, v2.z);
                Vector3f v3 = jv().get((int) face.vertex().z - 1);
                GL11.glVertex3f(v3.x, v3.y, v3.z);
            }
            GL11.glEnd();
        }

        // Reset back to regular faces
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

        // Done
        GL11.glPopMatrix();

        // Render the shadow (view-volume)
        // Note: we render the shadow independent of the model's translation and rotation
        // THOUGH NOTE: we do translate the shadow up a tiny bit off the ground so it doesn't z-fight
        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-1.0f, -1.0f);

        GL11.glTranslatef(0, 0.001f, 0);
        renderShadow(position());

        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);

        GL11.glPopMatrix();
    }
}
