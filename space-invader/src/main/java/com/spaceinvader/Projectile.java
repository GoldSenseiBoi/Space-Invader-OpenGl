package com.spaceinvader;

import org.lwjgl.opengl.GL11;

public class Projectile {
    private float x, y;
    private static final float SPEED = 0.05f;
    private int texture;

    public Projectile(float x, float y, int texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    public void update() {
        y += SPEED; // Le projectile monte vers le haut
    }

    public void render() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture); // Lier la texture
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x - 0.02f, y - 0.05f); // Coin inférieur gauche
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(x + 0.02f, y - 0.05f); // Coin inférieur droit
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + 0.02f, y + 0.05f); // Coin supérieur droit
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x - 0.02f, y + 0.05f); // Coin supérieur gauche
        GL11.glEnd();
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }
}
