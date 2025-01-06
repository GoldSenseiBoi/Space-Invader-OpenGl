package com.spaceinvader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Main {

    private static float playerX = 0f; // Position horizontale initiale
    private static final float PLAYER_SPEED = 0.02f; // Vitesse de déplacement
    private static List<Projectile> projectiles = new ArrayList<>();
    private static List<Bonus> bonuses = new ArrayList<>();
    private static List<Enemy> enemies = new ArrayList<>();
    private static List<Barrier> barriers = new ArrayList<>(); // Barrières de protection
    private static int[] numberTextures = new int[10];
    private static List<EnemyProjectile> enemyProjectiles = new ArrayList<>();
    private static int backgroundTexture;
    private static int enemyProjectileTexture;
    private static boolean isIntro = true; // Le jeu commence sur la page d'intro
    private static int titleTexture;


    private static int pressEnterTexture;

    private static float backgroundScroll = 0.0f;
    private static int lifeBonusTexture;
    private static int scoreBonusTexture;
    private static int firepowerBonusTexture;
    private static int scoreLabelTexture;
    private static int barrierTexture;
    private static int score = 0;
    private static int bonusTimer = 0;
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

        // Charger les textures
        int playerTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\JoueurDefault2.png");
        int projectileTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\shot.png");
        int enemyTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\Alien.png");
        scoreLabelTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\score.png");
        backgroundTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\space.jpg");
        lifeBonusTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\life.png");
        scoreBonusTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\scoreBonus.png");
        firepowerBonusTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\fireshot.png");
        barrierTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\barrier.png");
        enemyProjectileTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\shot.png");
        titleTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\TitreIntro.png");
        pressEnterTexture = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\PressEnter.png");

        



       // Ajouter des barrières avec des positions prédéfinies
        barriers.add(new Barrier(-0.6f, -0.5f));
        barriers.add(new Barrier(0.0f, -0.5f));
        barriers.add(new Barrier(0.6f, -0.5f));

        for (int i = 0; i < 10; i++) {
            numberTextures[i] = TextureLoader.loadTexture("space-invader\\src\\main\\resources\\textures\\textures\\" + i + ".png");
        }
        // Générer des rangées d'ennemis
        for (int i = 0; i < 5; i++) { // 5 rangées
            for (int j = 0; j < 10; j++) { // 10 ennemis par rangée
                enemies.add(new Enemy(-0.8f + j * 0.15f, 0.8f - i * 0.15f, enemyTexture));
            }
        }
        GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f); // Fond gris foncé

        // Gestion des entrées clavier
        GLFW.glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
                if (key == GLFW.GLFW_KEY_LEFT) {
                    playerX -= PLAYER_SPEED; // Déplacer à gauche
                } else if (key == GLFW.GLFW_KEY_RIGHT) {
                    playerX += PLAYER_SPEED; // Déplacer à droite
                } else if (key == GLFW.GLFW_KEY_SPACE) {
                    projectiles.add(new Projectile(playerX, -0.75f, projectileTexture));
                    System.out.println("Projectile créé à la position X : " + playerX);
                }
            }
        });

        // Boucle principale
        while (!GLFW.glfwWindowShouldClose(window)) {

            if (isIntro) {
                renderIntro();
                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS) {
                    isIntro = false; // Quitter la page d'intro
                }
            } else {
                
            
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
            // Limiter les déplacements aux bords de l'écran
            if (playerX < -0.9f) {
                playerX = -0.9f; // Limite gauche
            } else if (playerX > 0.9f) {
                playerX = 0.9f; // Limite droite
            }
        
            // Afficher l’arrière-plan
            renderBackground();
            backgroundScroll += 0.003f;
            if (backgroundScroll >= 1.0f) {
                backgroundScroll = 0.0f; // Réinitialise le défilement
            }
        
            // Gérer les bonus
            bonusTimer++;
            if (bonusTimer >= 600) { // Toutes les 10 secondes (600 frames à 60 FPS)
                float randomX = (float) (Math.random() * 1.8f - 0.9f); // Position aléatoire en X
                int bonusType = (int) (Math.random() * 3); // Type de bonus aléatoire
        
                switch (bonusType) {
                    case 0:
                        bonuses.add(new Bonus(randomX, 1.0f, lifeBonusTexture, "life"));
                        break;
                    case 1:
                        bonuses.add(new Bonus(randomX, 1.0f, scoreBonusTexture, "score"));
                        break;
                    case 2:
                        bonuses.add(new Bonus(randomX, 1.0f, firepowerBonusTexture, "firepower"));
                        break;
                }
                bonusTimer = 0; // Réinitialise le compteur
            }
        
            // Mettre à jour et rendre les bonus
            Iterator<Bonus> bonusIterator = bonuses.iterator();
            while (bonusIterator.hasNext()) {
                Bonus bonus = bonusIterator.next();
                bonus.update();
                bonus.render();
        
                // Collision avec le joueur
                if (Math.abs(bonus.getX() - playerX) < 0.1f && bonus.getY() < -0.7f) {
                    switch (bonus.getType()) {
                        case "life":
                            System.out.println("Bonus : Vie supplémentaire !");
                            break;
                        case "score":
                            score += 100;
                            System.out.println("Bonus : Score augmenté !");
                            break;
                        case "firepower":
                            System.out.println("Bonus : Tirs améliorés !");
                            break;
                    }
                    bonusIterator.remove(); // Supprimer le bonus après collecte
                }
        
                // Supprimer les bonus hors de l'écran
                if (bonus.getY() < -1.0f) {
                    bonusIterator.remove();
                }
            }
        
            // Affichage du joueur
            GL11.glPushMatrix();
            GL11.glTranslatef(playerX, -0.8f, 0f); // Déplacer horizontalement et fixer la position verticale
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, playerTexture);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 1); GL11.glVertex2f(-0.1f, -0.1f);
            GL11.glTexCoord2f(1, 1); GL11.glVertex2f(0.1f, -0.1f);
            GL11.glTexCoord2f(1, 0); GL11.glVertex2f(0.1f, 0.1f);
            GL11.glTexCoord2f(0, 0); GL11.glVertex2f(-0.1f, 0.1f);
            GL11.glEnd();
            GL11.glPopMatrix();
        
            // Afficher les barrières
            for (Barrier barrier : barriers) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, barrierTexture);
                GL11.glPushMatrix();
                GL11.glTranslatef(barrier.getX(), barrier.getY(), 0f); // Position de la barrière
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(0, 1); GL11.glVertex2f(-0.1f, -0.05f);
                GL11.glTexCoord2f(1, 1); GL11.glVertex2f(0.1f, -0.05f);
                GL11.glTexCoord2f(1, 0); GL11.glVertex2f(0.1f, 0.05f);
                GL11.glTexCoord2f(0, 0); GL11.glVertex2f(-0.1f, 0.05f);
                GL11.glEnd();
                GL11.glPopMatrix();
            }
        
            // Gérer les projectiles du joueur
            Iterator<Projectile> projectileIterator = projectiles.iterator();
            while (projectileIterator.hasNext()) {
                Projectile projectile = projectileIterator.next();
                projectile.update();
                projectile.render();
        
                // Vérifier les collisions avec les ennemis
                Iterator<Enemy> enemyIterator = enemies.iterator();
                while (enemyIterator.hasNext()) {
                    Enemy enemy = enemyIterator.next();
                    if (enemy.isHit(projectile.getX(), projectile.getY())) {
                        enemyIterator.remove(); // Supprimer l'ennemi touché
                        projectileIterator.remove(); // Supprimer le projectile
                        System.out.println("Collision détectée !");
                        score += 10; // Augmenter le score uniquement en cas de collision
                        System.out.println("Score : " + score);
                        break; // Quitter la boucle des ennemis
                    }
                }

                
        
                // Supprimer les projectiles hors de l'écran
                if (projectile.getY() > 1.0f) {
                    projectileIterator.remove();
                }
            }
        
            // Afficher les ennemis
            for (Enemy enemy : enemies) {
                enemy.update();
                enemy.render();
        
                // Les ennemis peuvent tirer
                if (Math.random() < 0.001) { // Probabilité de tir
                    enemyProjectiles.add(new EnemyProjectile(enemy.getX(), enemy.getY() - 0.1f));
                }
            }
        
            // Gérer les tirs des ennemis
            
        Iterator<EnemyProjectile> enemyProjectileIterator = enemyProjectiles.iterator();
        while (enemyProjectileIterator.hasNext()) {
            EnemyProjectile enemyProjectile = enemyProjectileIterator.next();
            enemyProjectile.update();
            enemyProjectile.render();

            // Vérifier les collisions avec les barrières
            Iterator<Barrier> barrierIterator = barriers.iterator();
            while (barrierIterator.hasNext()) {
                Barrier barrier = barrierIterator.next();
                if (barrier.isHit(enemyProjectile.getX(), enemyProjectile.getY())) {
                    barrier.takeDamage(); // Réduire la durabilité de la barrière
                    enemyProjectileIterator.remove(); // Supprimer le projectile ennemi après l'impact
                    System.out.println("Barrière touchée !");
                    if (barrier.isDestroyed()) {
                        barrierIterator.remove(); // Supprimer la barrière si elle est détruite
                        System.out.println("Barrière détruite !");
                    }
                    break; // Quitter la boucle des barrières après une collision
                }
            }

            // Collision avec le joueur
            if (Math.abs(enemyProjectile.getX() - playerX) < 0.1f && enemyProjectile.getY() < -0.7f) {
                System.out.println("Le joueur a été touché !");
                enemyProjectileIterator.remove();
            }

            // Supprimer les projectiles ennemis hors écran
            if (enemyProjectile.getY() < -1.0f) {
                enemyProjectileIterator.remove();
            }
        }

        
            // Afficher le score
            renderScoreLabel(-0.9f, 0.9f, 1.0f); // Affiche "Score" en haut à gauche
            renderScore(score, -0.6f, 0.9f, 1.0f); // Affiche le score à côté
    }
            // Échanger les buffers et traiter les événements
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
        
        // Terminer GLFW
        GLFW.glfwTerminate();
        
    }

    private static void renderIntro() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); // Nettoyer l'écran
    
        // Afficher l'image de titre
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, titleTexture);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(-0.8f, 0.5f);  // Coin supérieur gauche
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(0.8f, 0.5f);   // Coin supérieur droit
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(0.8f, -0.2f);  // Coin inférieur droit
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(-0.8f, -0.2f); // Coin inférieur gauche
        GL11.glEnd();
    
        // Afficher "Press Enter to Start"
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pressEnterTexture);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(-0.4f, -0.6f);  // Coin supérieur gauche
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(0.4f, -0.6f);   // Coin supérieur droit
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(0.4f, -0.8f);   // Coin inférieur droit
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(-0.4f, -0.8f);  // Coin inférieur gauche
        GL11.glEnd();
    }
    
    
    
    
    
    
    
    private static void renderScore(int score, float x, float y, float scale) {
        String scoreStr = String.valueOf(score);
        float offset = 0.1f * scale; // Espace entre chaque chiffre
    
        for (int i = 0; i < scoreStr.length(); i++) {
            int digit = scoreStr.charAt(i) - '0'; // Convertir le caractère en chiffre
            renderDigit(digit, x + i * offset, y, scale);
        }
    }

    private static void renderDigit(int digit, float x, float y, float scale) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, numberTextures[digit]);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(-0.05f * scale, -0.05f * scale);
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(0.05f * scale, -0.05f * scale);
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(0.05f * scale, 0.05f * scale);
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(-0.05f * scale, 0.05f * scale);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private static void renderScoreLabel(float x, float y, float scale) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, scoreLabelTexture);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(-0.1f * scale, -0.05f * scale); // Réduit la largeur
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(0.1f * scale, -0.05f * scale);
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(0.1f * scale, 0.05f * scale); // Réduit la hauteur
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(-0.1f * scale, 0.05f * scale);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private static void renderBackground() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, backgroundScroll); GL11.glVertex2f(-1.0f, -1.0f); // Coin inférieur gauche
        GL11.glTexCoord2f(1, backgroundScroll); GL11.glVertex2f(1.0f, -1.0f);  // Coin inférieur droit
        GL11.glTexCoord2f(1, 1 + backgroundScroll); GL11.glVertex2f(1.0f, 1.0f);   // Coin supérieur droit
        GL11.glTexCoord2f(0, 1 + backgroundScroll); GL11.glVertex2f(-1.0f, 1.0f);  // Coin supérieur gauche
        GL11.glEnd();
    }
    
    public static int getEnemyProjectileTexture() {
        return enemyProjectileTexture;
    }
    
}
