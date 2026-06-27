package sk.adamhagara.game.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import sk.adamhagara.game.annotations.GameMechanic;
import sk.adamhagara.game.patterns.visitor.CardVisitor;
import sk.adamhagara.game.patterns.composite.EffectComponent;

@GameMechanic(
    description = "Core card system for game mechanics",
    category = GameMechanic.Category.CARD_PLAY,
    points = 3
)
public abstract class Card {
    protected String name;
    protected Texture texture;

    public Card(String name, String textureFile) {
        this.name = name;
        if (Gdx.files != null) {
            this.texture = new Texture(Gdx.files.internal(textureFile));
        } else {
            this.texture = null;
        }
    }


    public Texture getTexture() {
        return texture;
    }

    public String getName() { return name; }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public abstract void applyEffect();

    /**
     * Apply effect with intensity - method overloading
     */
    public void applyEffect(float intensity) {
        applyEffect();
        // Default implementation - can be overridden
    }

    // Visitor pattern support
    public abstract void accept(CardVisitor visitor);

    // Composite pattern support - default implementation for simple cards
    public EffectComponent getEffectComponent() {
        return new sk.adamhagara.game.patterns.composite.SimpleEffect(
            name, 
            this::applyEffect
        );
    }
}
