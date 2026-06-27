package sk.adamhagara.game.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.ui.GameScreen;

/**
 * Fear emotion card implementation
 * Causes entity fear effect and screen flashing
 */
public class FearCard extends EmotionCard {
    private final GameScreen gameScreen;
    private final Sound suspenseSound;
    private float effectDuration = 3f;
    private float screenFlashDuration = 5f;

    public FearCard(Entity entity, GameScreen gameScreen) {
        super("Fear", "FEAR.png", entity);
        this.gameScreen = gameScreen;
        this.suspenseSound = createSound();
    }


    @Override
    public void applyEffect() {
        float adjustedDuration = effectDuration * effectIntensity;
        targetEntity.triggerFearEffect(adjustedDuration);

        if (suspenseSound != null) {
            suspenseSound.play(Math.min(1.0f, effectIntensity));
        }

        // Start screen flashing effect based on intensity
        float flashDuration = screenFlashDuration * effectIntensity;
        gameScreen.startScreenFlash(flashDuration);
    }

    /**
     * Create sound effect
     */
    private Sound createSound() {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("Suspense_audio.mp3"));
            SoundManager.registerSound(sound);
            return sound;
        } catch (Exception ignored) {
            return null;
        }
    }
}

