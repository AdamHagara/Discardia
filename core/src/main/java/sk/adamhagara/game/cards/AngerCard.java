package sk.adamhagara.game.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;

/**
 * Anger emotion card implementation
 * Causes entity anger effect with glass smash sound
 */
public class AngerCard extends EmotionCard {
    private final Sound glassSmashSound;
    private float angerSpike = 10f;

    public AngerCard(Entity entity) {
        super("Anger", "ANGER.png", entity);
        this.glassSmashSound = createSound();
    }


    @Override
    public void applyEffect() {
        float adjustedSpike = angerSpike * effectIntensity;
        targetEntity.triggerAngerEffect(adjustedSpike);

        if (glassSmashSound != null) {
            glassSmashSound.play(Math.min(1.0f, effectIntensity));
        }
    }

    /**
     * Create sound effect
     */
    private Sound createSound() {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("Glass_smash.mp3"));
            SoundManager.registerSound(sound);
            return sound;
        } catch (Exception ignored) {
            return null;
        }
    }
}

