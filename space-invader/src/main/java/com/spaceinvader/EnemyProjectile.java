package com.spaceinvader;

import org.lwjgl.opengl.GL11;

public class EnemyProjectile {
    private float x, y;
    private static final float SPEED = 0.01f; // Vitesse du projectile

    public EnemyProjectile(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= SPEED; // Descend vers le bas
    }

    public void render() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Main.getEnemyProjectileTexture());
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x - 0.02f, y - 0.05f);
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(x + 0.02f, y - 0.05f);
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + 0.02f, y + 0.05f);
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x - 0.02f, y + 0.05f);
        GL11.glEnd();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
