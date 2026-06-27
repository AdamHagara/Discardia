package sk.adamhagara.game.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.ui.GameScreen;

/**
 * Shame emotion card implementation
 * Causes entity shame effect and camera lock
 */
public class ShameCard extends EmotionCard {
    private final GameScreen gameScreen;
    private final Sound shameSound;
    private float effectDuration = 3f;
    private float cameraLockDuration = 3f;


    public ShameCard(Entity entity, GameScreen gameScreen, float intensity) {
        super("Shame", "SHAME.png", entity, intensity);
        this.gameScreen = gameScreen;
        this.shameSound = createSound();
    }


    @Override
    public void applyEffect() {
        float adjustedDuration = effectDuration * effectIntensity;
        targetEntity.triggerShameEffect(adjustedDuration);

        if (shameSound != null) {
            shameSound.play(Math.min(1.0f, effectIntensity));
        }

        // Start camera lock effect based on intensity
        float lockDuration = cameraLockDuration * effectIntensity;
        gameScreen.startCameraLock(lockDuration);
    }

    /**
     * Create sound effect
     */
    private Sound createSound() {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("Shame_sound.mp3"));
            SoundManager.registerSound(sound);
            return sound;
        } catch (Exception ignored) {
            return null;
        }
    }
}

