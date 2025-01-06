package com.spaceinvader;

import org.lwjgl.opengl.GL11;

public class Barrier {
    private float x, y;
    private int health;

    public Barrier(float x, float y) {
        this.x = x;
        this.y = y;
        this.health = 5; // Chaque barrière a 5 points de vie
    }

    public void render() {
        GL11.glColor3f(0.0f, 1.0f, 0.0f); // Vert
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - 0.1f, y - 0.05f);
        GL11.glVertex2f(x + 0.1f, y - 0.05f);
        GL11.glVertex2f(x + 0.1f, y + 0.05f);
        GL11.glVertex2f(x - 0.1f, y + 0.05f);
        GL11.glEnd();
    }

    public boolean isHit(float projectileX, float projectileY) {
        return Math.abs(projectileX - x) < 0.1f && Math.abs(projectileY - y) < 0.05f;
    }

    public void takeDamage() {
        health--;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }
}
