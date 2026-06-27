package sk.adamhagara.game.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.ui.GameScreen;

/**
 * Guilt emotion card implementation
 * Causes entity guilt effect and visual whitewash
 */
public class GuiltCard extends EmotionCard {
    private final GameScreen gameScreen;
    private final Sound guiltSound;
    private float effectDuration = 3f;
    private float whitewashDuration = 5f;

    public GuiltCard(Entity entity, GameScreen gameScreen, float intensity) {
        super("Guilt", "GUILT.png", entity, intensity);
        this.gameScreen = gameScreen;
        this.guiltSound = createSound();
    }

    public GuiltCard(Entity entity, GameScreen gameScreen, float intensity, float duration) {
        this(entity, gameScreen, intensity);
        this.effectDuration = duration;
    }

    @Override
    public void applyEffect() {
        float adjustedDuration = effectDuration * effectIntensity;
        targetEntity.triggerGuiltEffect(adjustedDuration);

        if (guiltSound != null) {
            guiltSound.play(Math.min(1.0f, effectIntensity));
        }

        // Start whitewash effect based on intensity
        float washDuration = whitewashDuration * effectIntensity;
        gameScreen.startGuiltWhitewash(washDuration);
    }

    /**
     * Create sound effect
     */
    private Sound createSound() {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("Guilt_sound.mp3"));
            SoundManager.registerSound(sound);
            return sound;
        } catch (Exception ignored) {
            return null;
        }
    }
}

