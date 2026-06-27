package sk.adamhagara.game.patterns.observer;

/**
 * Observer pattern interface
 * Allows objects to be notified of game state changes
 */
public interface GameObserver {
    void onGameStateChanged(GameEventType eventType, Object data);
    
    enum GameEventType {
        CANDLE_EXTINGUISHED,
        CANDLE_RELIT,
        CANDLE_FUEL_LOW,
        CANDLE_FUEL_CRITICAL,
        ENTITY_ANGER_CHANGED,
        ENTITY_ANGER_HIGH,
        ENTITY_ANGER_CRITICAL,
        TIME_WARNING,
        TIME_CRITICAL,
        CARD_PLAYED
    }
}
