package sk.adamhagara.game.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.ui.GameScreen;
import sk.adamhagara.game.patterns.visitor.CardVisitor;

/**
 * Loneliness emotion card implementation
 * Causes entity to hide and affects goal visibility
 */
public class LonelinessCard extends EmotionCard {
    private final GameScreen gameScreen;
    private final Sound lonelinessSound;
    private float effectDuration = 3f;


    public LonelinessCard(Entity entity, GameScreen gameScreen, float intensity) {
        super("Loneliness", "LONELINESS.png", entity, intensity);
        this.gameScreen = gameScreen;
        this.lonelinessSound = createSound();
    }


    @Override
    public void applyEffect() {
        float adjustedDuration = effectDuration * effectIntensity;
        targetEntity.triggerLonelinessEffect(adjustedDuration);

        if (lonelinessSound != null) {
            lonelinessSound.play(Math.min(1.0f, effectIntensity));
        }

        // Start goal hidden effect based on intensity
        float goalHiddenDuration = 10f * effectIntensity;
        gameScreen.startGoalHidden(goalHiddenDuration);
    }

    /**
     * Create sound effect
     */
    private Sound createSound() {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("Loneliness_sound.mp3"));
            SoundManager.registerSound(sound);
            return sound;
        } catch (Exception ignored) {
            return null;
        }
    }

    // Visitor pattern support
    @Override
    public void accept(CardVisitor visitor) {
        visitor.visit(this);
    }

    // Getter for validation
    public GameScreen getGameScreen() { return gameScreen; }
}

