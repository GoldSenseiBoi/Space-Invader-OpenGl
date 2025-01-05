package com.spaceinvader;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;


public class Main {

    private static float playerX = 0f; // Position horizontale initiale
    private static final float PLAYER_SPEED = 0.02f; // Vitesse de déplacement
    
    public static void main(String[] args) {
        // Initialisation GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW!");
        }
    
        long window = GLFW.glfwCreateWindow(800, 600, "Space Invader", 0, 0);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    
        // Charger la texture
        int playerTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\JoueurDefault2.png");
    
        GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f); // Fond gris foncé
    
        // Gestion des entrées clavier
        GLFW.glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
                if (key == GLFW.GLFW_KEY_LEFT) {
                    playerX -= PLAYER_SPEED; // Déplacer à gauche
                } else if (key == GLFW.GLFW_KEY_RIGHT) {
                    playerX += PLAYER_SPEED; // Déplacer à droite
                }
            }
        });
    
        // Boucle principale
        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    
            // Limiter les déplacements aux bords de l'écran
            if (playerX < -0.9f) {
                playerX = -0.9f; // Limite gauche
            } else if (playerX > 0.9f) {
                playerX = 0.9f; // Limite droite
            }
    
            // Affichage du joueur avec position mise à jour
            GL11.glPushMatrix();
            GL11.glTranslatef(playerX, -0.8f, 0f); // Déplacer horizontalement et fixer la position verticale
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, playerTexture);
            GL11.glBegin(GL11.GL_QUADS);
            // Coordonnées modifiées pour inverser l'image verticalement
            GL11.glTexCoord2f(0, 1); GL11.glVertex2f(-0.1f, -0.1f);
            GL11.glTexCoord2f(1, 1); GL11.glVertex2f(0.1f, -0.1f);
            GL11.glTexCoord2f(1, 0); GL11.glVertex2f(0.1f, 0.1f);
            GL11.glTexCoord2f(0, 0); GL11.glVertex2f(-0.1f, 0.1f);
            GL11.glEnd();
            GL11.glPopMatrix();
    
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    
        GLFW.glfwTerminate();
    }
}
