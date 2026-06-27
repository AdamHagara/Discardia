package sk.adamhagara.game.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Candle;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;

/**
 * Anxiety emotion card implementation
 * Affects candle behavior and plays anxiety sound
 */
public class AnxietyCard extends EmotionCard {
    private final Candle candle;
    private final Sound anxietySound;

    public AnxietyCard(Entity entity, Candle candle) {
        super("Anxiety", "ANXIETY.png", entity);
        this.candle = candle;
        this.anxietySound = createSound();
    }


    @Override
    public void applyEffect() {
        // Anxiety card should only affect the candle, not the entity
        candle.applyAnxiety(999f);
        if (anxietySound != null) {
            anxietySound.play(Math.min(1.0f, effectIntensity));
        }
    }

    /**
     * Create sound effect
     */
    private Sound createSound() {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("Anxiety_sound.mp3"));
            SoundManager.registerSound(sound);
            return sound;
        } catch (Exception ignored) {
            return null;
        }
    }
}

