package sk.adamhagara.game.cards;

import sk.adamhagara.game.annotations.GameMechanic;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.patterns.visitor.CardVisitor;

/**
 * Abstract base class for emotion cards
 * Provides common functionality for all emotion-based cards
 */
@GameMechanic(
    description = "Emotion-based card system affecting entity behavior",
    category = GameMechanic.Category.ENTITY_BEHAVIOR,
    points = 2
)
public abstract class EmotionCard extends Card {
    protected final Entity targetEntity;
    protected float effectIntensity = 1.0f;

    public EmotionCard(String name, String textureFile, Entity targetEntity) {
        super(name, textureFile);
        this.targetEntity = targetEntity;
    }

    public EmotionCard(String name, String textureFile, Entity targetEntity, float intensity) {
        this(name, textureFile, targetEntity);
        this.effectIntensity = intensity;
    }

    /**
     * Override applyEffect with intensity - method overriding
     */
    @Override
    public void applyEffect(float intensity) {
        this.effectIntensity = Math.max(0.1f, Math.min(2.0f, intensity));
        applyEffect();
    }

    // Visitor pattern support
    @Override
    public void accept(CardVisitor visitor) {
        visitor.visit(this);
    }

    // Getters for validation and statistics
    public Entity getTargetEntity() { return targetEntity; }
    public float getEffectIntensity() { return effectIntensity; }
}

