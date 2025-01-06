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
        y -= SPEED; // Les projectiles descendent
    }

    public void render() {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - 0.02f, y - 0.05f);
        GL11.glVertex2f(x + 0.02f, y - 0.05f);
        GL11.glVertex2f(x + 0.02f, y + 0.05f);
        GL11.glVertex2f(x - 0.02f, y + 0.05f);
        GL11.glEnd();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
