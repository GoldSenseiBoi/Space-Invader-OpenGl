package com.spaceinvader;

import org.lwjgl.opengl.GL11;

public class Enemy {
    private float x, y;
    private int texture;
    private static final float SPEED = 0.01f;
    private boolean movingRight = true;

    public Enemy(float x, float y, int texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    public void update() {
        // Mouvement horizontal
        if (movingRight) {
            x += SPEED;
            if (x > 0.9f) {
                movingRight = false;
                y -= 0.05f; // Descente lorsqu'on atteint le bord
            }
        } else {
            x -= SPEED;
            if (x < -0.9f) {
                movingRight = true;
                y -= 0.05f; // Descente de l'autre côté
            }
        }
    }

    public void render() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x - 0.05f, y - 0.05f); // Coin inférieur gauche
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(x + 0.05f, y - 0.05f); // Coin inférieur droit
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + 0.05f, y + 0.05f); // Coin supérieur droit
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x - 0.05f, y + 0.05f); // Coin supérieur gauche
        GL11.glEnd();
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public boolean isHit(float projectileX, float projectileY) {
        float enemyWidth = 0.1f; // Largeur approximative de l'ennemi
        float enemyHeight = 0.1f; // Hauteur approximative de l'ennemi
        return projectileX > (x - enemyWidth / 2) && projectileX < (x + enemyWidth / 2) &&
               projectileY > (y - enemyHeight / 2) && projectileY < (y + enemyHeight / 2);
    }
    
}
