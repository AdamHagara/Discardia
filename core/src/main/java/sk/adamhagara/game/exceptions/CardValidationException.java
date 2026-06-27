package sk.adamhagara.game.exceptions;

/**
 * Exception thrown when card validation fails
 * Used to handle invalid card states and configurations
 */
public class CardValidationException extends GameException {
    
    private final String cardName;
    private final String validationError;
    
    public CardValidationException(String cardName, String validationError) {
        super(String.format("Card '%s' validation failed: %s", cardName, validationError));
        this.cardName = cardName;
        this.validationError = validationError;
    }
    
    public CardValidationException(String cardName, String validationError, Throwable cause) {
        super(String.format("Card '%s' validation failed: %s", cardName, validationError), cause);
        this.cardName = cardName;
        this.validationError = validationError;
    }
    
    public CardValidationException(String cardName, Throwable cause) {
        super(cause);  // Uses GameException(Throwable cause) constructor
        this.cardName = cardName;
        this.validationError = "Unexpected validation error";
    }
    
    public String getCardName() {
        return cardName;
    }
    
    public String getValidationError() {
        return validationError;
    }
}
