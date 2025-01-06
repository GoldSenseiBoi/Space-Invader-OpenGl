package com.spaceinvader;

import org.lwjgl.opengl.GL11;

public class Bonus {
    private float x, y;
    private int texture;
    private String type; // Type du bonus (e.g., "life", "score", "firepower")

    public Bonus(float x, float y, int texture, String type) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.type = type;
    }

    public void update() {
        y -= 0.01f; // Le bonus descend vers le joueur
    }

    public void render() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x - 0.05f, y - 0.05f);
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(x + 0.05f, y - 0.05f);
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + 0.05f, y + 0.05f);
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x - 0.05f, y + 0.05f);
        GL11.glEnd();
    }

    public String getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
