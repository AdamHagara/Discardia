package sk.adamhagara.game.exceptions;

/**
 * Custom exception for game-specific errors
 * Used for meaningful error handling in game logic
 */
public class GameException extends Exception {
    
    public GameException(String message) {
        super(message);
    }
    
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public GameException(Throwable cause) {
        super(cause);
    }
}
