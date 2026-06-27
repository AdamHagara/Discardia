package sk.adamhagara.game.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.ui.GameScreen;

/**
 * Depression emotion card implementation
 * Causes entity depression effect and card draw cooldown
 */
public class DepressionCard extends EmotionCard {
    private final GameScreen gameScreen;
    private final Sound depressionSound;
    private float effectDuration = 3f;
    private float cardCooldownDuration = 5f;

    public DepressionCard(Entity entity, GameScreen gameScreen) {
        super("Depression", "DEPRESSION.png", entity);
        this.gameScreen = gameScreen;
        this.depressionSound = createSound();
    }


    @Override
    public void applyEffect() {
        float adjustedDuration = effectDuration * effectIntensity;
        targetEntity.triggerDepressionEffect(adjustedDuration);

        if (depressionSound != null) {
            depressionSound.play(Math.min(1.0f, effectIntensity));
        }

        // Add cooldown to card drawing based on intensity
        float cooldown = cardCooldownDuration * effectIntensity;
        gameScreen.addCardDrawCooldown(cooldown);
    }

    /**
     * Create sound effect
     */
    private Sound createSound() {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("Depression_audio.mp3"));
            SoundManager.registerSound(sound);
            return sound;
        } catch (Exception ignored) {
            return null;
        }
    }
}

