package net.sabio.purgatory.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.sabio.purgatory.Purgatory;
import net.sabio.purgatory.mod.world.PurgatoryNoiseTracker;

import java.util.Random;

public class EyeHudRenderer {
    private static final Random RANDOM = new Random();

    private static final Identifier[] PHASE_TEXTURES = new Identifier[]{
            eyeTexture("phase_0_unnoticed"),
            eyeTexture("phase_1_suspicious"),
            eyeTexture("phase_2_alert"),
            eyeTexture("phase_3_stalking"),
            eyeTexture("phase_4_hunting")
    };

    private static volatile int currentPhase = 0;

    private static Identifier eyeTexture(String name) {
        return Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, "textures/gui/eye/" + name + ".png");
    }

    public static void setPhase(int phase) {
        if (phase < 0) {
            currentPhase = PurgatoryNoiseTracker.PHASE_HIDDEN;
            return;
        }
        currentPhase = Math.clamp(phase, 0, PHASE_TEXTURES.length - 1);
    }

    public static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (currentPhase < 0 || currentPhase >= PHASE_TEXTURES.length) {
            return;
        }

        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        int x = (screenWidth - 64) / 2;
        int y = screenHeight - 80;

        if (currentPhase == 4) {
            x += RANDOM.nextInt(7) - 3;
            y += RANDOM.nextInt(7) - 3;
        }

        Identifier texture = PHASE_TEXTURES[currentPhase];
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, 64, 32, 64, 32);
    }

    public static void reset() {
        currentPhase = PurgatoryNoiseTracker.PHASE_HIDDEN;
    }
}
