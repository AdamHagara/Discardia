package sk.adamhagara.game.cards.special;

import sk.adamhagara.game.annotations.GameMechanic;
import sk.adamhagara.game.cards.Card;
import sk.adamhagara.game.patterns.visitor.CardVisitor;

@GameMechanic(
    description = "Special cards with unique game effects",
    category = GameMechanic.Category.EFFECT,
    points = 2
)
public abstract class SpecialCard extends Card {
    public SpecialCard(String name, String textureFile) {
        super(name, textureFile);
    }

    // Visitor pattern support
    @Override
    public void accept(CardVisitor visitor) {
        visitor.visit((sk.adamhagara.game.cards.special.SpecialCard) this);
    }
}

