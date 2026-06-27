package sk.adamhagara.game.patterns.composite;

/**
 * Leaf node in Composite pattern
 * Represents a single, atomic effect
 */
public class SimpleEffect extends EffectComponent {
    private final Runnable effectAction;
    
    public SimpleEffect(String name, Runnable effectAction) {
        super(name);
        this.effectAction = effectAction;
    }
    
    @Override
    public void apply() {
        effectAction.run();
    }
}
