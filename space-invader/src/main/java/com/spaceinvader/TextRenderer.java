package com.spaceinvader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;

import java.nio.ByteBuffer;
import java.nio.file.Files;

import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.system.MemoryStack;

public class TextRenderer {
    private static final int BITMAP_W = 512;
    private static final int BITMAP_H = 512;
    private final int textureID;
    private final STBTTBakedChar.Buffer charData;

    public TextRenderer(String fontPath, int fontSize) throws Exception {
        ByteBuffer fontBuffer = ByteBuffer.wrap(Files.readAllBytes(java.nio.file.Paths.get(fontPath)));
        ByteBuffer bitmap = ByteBuffer.allocateDirect(BITMAP_W * BITMAP_H);
        charData = STBTTBakedChar.malloc(96); // 96 caractères ASCII imprimables

        stbtt_BakeFontBitmap(fontBuffer, fontSize, bitmap, BITMAP_W, BITMAP_H, 32, charData);
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, BITMAP_W, BITMAP_H, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public void renderText(String text, float x, float y, float scale) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureID);
        glColor3f(1.0f, 1.0f, 0.0f); // Couleur jaune

        try (MemoryStack stack = MemoryStack.stackPush()) {
            STBTTAlignedQuad quad = STBTTAlignedQuad.malloc(stack);
            float[] xpos = {x};
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c < 32 || c >= 128) continue; // Ignorer les caractères non supportés
                stbtt_GetBakedQuad(charData, BITMAP_W, BITMAP_H, c - 32, xpos, new float[]{y}, quad, true);

                glBegin(GL_QUADS);
                glTexCoord2f(quad.s0(), quad.t0());
                glVertex2f(quad.x0() * scale, quad.y0() * scale);
                glTexCoord2f(quad.s1(), quad.t0());
                glVertex2f(quad.x1() * scale, quad.y0() * scale);
                glTexCoord2f(quad.s1(), quad.t1());
                glVertex2f(quad.x1() * scale, quad.y1() * scale);
                glTexCoord2f(quad.s0(), quad.t1());
                glVertex2f(quad.x0() * scale, quad.y1() * scale);
                glEnd();
            }
        }
        glDisable(GL_TEXTURE_2D);
    }
}
