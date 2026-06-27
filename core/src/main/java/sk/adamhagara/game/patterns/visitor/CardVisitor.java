package sk.adamhagara.game.patterns.visitor;

import sk.adamhagara.game.cards.Card;
import sk.adamhagara.game.cards.EmotionCard;
import sk.adamhagara.game.cards.special.SpecialCard;

/**
 * Visitor pattern interface
 * Allows adding new operations to Card hierarchy without modifying the classes
 */
public interface CardVisitor {
    // Visit methods for different card types
    void visit(Card card);
    void visit(EmotionCard emotionCard);
    void visit(sk.adamhagara.game.cards.special.SpecialCard specialCard);
    void visit(sk.adamhagara.game.cards.special.BlessingCard blessingCard);
    void visit(sk.adamhagara.game.cards.LonelinessCard lonelinessCard);
}
