package sk.adamhagara.game.patterns.visitor;

import sk.adamhagara.game.cards.*;
import sk.adamhagara.game.cards.special.*;
import sk.adamhagara.game.exceptions.CardValidationException;

/**
 * Concrete visitor for card validation
 * Validates card properties and effects without modifying card classes
 */
public class CardValidationVisitor implements CardVisitor {
    private boolean isValid = true;
    private String validationMessage = "";

    @Override
    public void visit(Card card) {
        // Basic card validation
        if (card.getName() == null || card.getName().isEmpty()) {
            isValid = false;
            validationMessage += "Card name is null or empty. ";
        }
        if (card.getTexture() == null) {
            validationMessage += "Card texture is null. ";
        }
    }

    @Override
    public void visit(EmotionCard emotionCard) {
        visit((Card) emotionCard);

        // Emotion card specific validation
        if (emotionCard.getTargetEntity() == null) {
            isValid = false;
            validationMessage += "Emotion card has no target entity. ";
        }

        if (emotionCard.getEffectIntensity() < 0.1f || emotionCard.getEffectIntensity() > 2.0f) {
            isValid = false;
            validationMessage += "Emotion card intensity out of range [0.1, 2.0]. ";
        }
    }

    @Override
    public void visit(sk.adamhagara.game.cards.special.SpecialCard specialCard) {
        visit((Card) specialCard);
        // Basic validation for special cards
    }

    @Override
    public void visit(BlessingCard blessingCard) {
        visit((Card) blessingCard);

        if (blessingCard.getCandle() == null) {
            isValid = false;
            validationMessage += "Blessing card has no candle target. ";
        }
        if (blessingCard.getEntity() == null) {
            isValid = false;
            validationMessage += "Blessing card has no entity target. ";
        }
    }

    @Override
    public void visit(LonelinessCard lonelinessCard) {
        visit((EmotionCard) lonelinessCard);
        if (lonelinessCard.getGameScreen() == null) {
            isValid = false;
            validationMessage += "Loneliness card has no game screen target. ";
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public String getValidationMessage() {
        return validationMessage.isEmpty() ? "Card is valid" : validationMessage;
    }
    
    /**
     * Validates the card and throws exception if invalid
     * @throws CardValidationException if card validation fails
     */
    public void validateAndThrow(Card card) throws CardValidationException {
        // Reset validation state
        isValid = true;
        validationMessage = "";
        
        try {
            // Run validation
            card.accept(this);
            
            // Throw exception if invalid
            if (!isValid) {
                throw new CardValidationException(card.getName(), validationMessage);
            }
        } catch (RuntimeException e) {
            // Handle unexpected errors during validation
            if (e.getMessage() == null) {
                // Use the GameException(Throwable cause) constructor
                throw new CardValidationException(card.getName(), e);
            } else {
                throw new CardValidationException(card.getName(), 
                                               "Unexpected validation error: " + e.getMessage(), 
                                               e);
            }
        }
    }
}
