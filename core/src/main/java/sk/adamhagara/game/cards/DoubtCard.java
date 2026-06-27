package sk.adamhagara.game.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;

/**
 * Doubt emotion card implementation
 * Causes entity doubt effect and makes entity invisible
 */
public class DoubtCard extends EmotionCard {
    private final Sound doubtSound;
    private float effectDuration = 3f;
    private float invisibilityDuration = 10f;

    public DoubtCard(Entity entity) {
        super("Doubt", "DOUBT.png", entity);
        this.doubtSound = createSound();
    }



    @Override
    public void applyEffect() {
        float adjustedDuration = effectDuration * effectIntensity;
        float adjustedInvisibilityDuration = invisibilityDuration * effectIntensity;
        
        targetEntity.triggerDoubtEffect(adjustedInvisibilityDuration);

        if (doubtSound != null) {
            doubtSound.play(Math.min(1.0f, effectIntensity));
        }

        // Make entity sprite invisible based on intensity
        targetEntity.setVisible(false);
        // Note: Entity visibility will be restored by timer logic in Entity class
    }

    /**
     * Create sound effect
     */
    private Sound createSound() {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("Doubt_sound.mp3"));
            SoundManager.registerSound(sound);
            return sound;
        } catch (Exception ignored) {
            return null;
        }
    }
}

