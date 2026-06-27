package sk.adamhagara.game.patterns.composite;

/**
 * Composite pattern for card effects
 * Allows both simple and composite effects to be treated uniformly
 */
public abstract class EffectComponent {
    protected String name;

    public EffectComponent(String name) {
        this.name = name;
    }

    // Abstract operations
    public abstract void apply();

}
