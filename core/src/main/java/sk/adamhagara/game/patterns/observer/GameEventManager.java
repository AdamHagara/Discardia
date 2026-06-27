package sk.adamhagara.game.patterns.observer;

/**
 * Singleton event manager for Observer pattern
 * Central point for managing game state notifications
 */
public class GameEventManager implements GameSubject {
    private static GameEventManager instance;
    private final GameSubjectImpl subject;
    
    private GameEventManager() {
        this.subject = new GameSubjectImpl();
    }
    
    public static GameEventManager getInstance() {
        if (instance == null) {
            instance = new GameEventManager();
        }
        return instance;
    }
    
    @Override
    public void addObserver(GameObserver observer) {
        subject.addObserver(observer);
    }
    
    @Override
    public void removeObserver(GameObserver observer) {
        subject.removeObserver(observer);
    }
    
    @Override
    public void notifyObservers(GameObserver.GameEventType eventType, Object data) {
        subject.notifyObservers(eventType, data);
    }
    
    // Convenience methods for common events
    public void notifyCandleExtinguished() {
        notifyObservers(GameObserver.GameEventType.CANDLE_EXTINGUISHED, null);
    }
    
    public void notifyCandleRelit() {
        notifyObservers(GameObserver.GameEventType.CANDLE_RELIT, null);
    }
    
    public void notifyCandleFuelLow(float fuelPercent) {
        notifyObservers(GameObserver.GameEventType.CANDLE_FUEL_LOW, fuelPercent);
    }
    
    public void notifyCandleFuelCritical(float fuelPercent) {
        notifyObservers(GameObserver.GameEventType.CANDLE_FUEL_CRITICAL, fuelPercent);
    }
    
    public void notifyEntityAngerChanged(float angerLevel) {
        notifyObservers(GameObserver.GameEventType.ENTITY_ANGER_CHANGED, angerLevel);
    }
    
    public void notifyEntityAngerHigh(float angerLevel) {
        notifyObservers(GameObserver.GameEventType.ENTITY_ANGER_HIGH, angerLevel);
    }
    
    public void notifyEntityAngerCritical(float angerLevel) {
        notifyObservers(GameObserver.GameEventType.ENTITY_ANGER_CRITICAL, angerLevel);
    }
    
    public void notifyTimeWarning(float timeRemaining) {
        notifyObservers(GameObserver.GameEventType.TIME_WARNING, timeRemaining);
    }
    
    public void notifyTimeCritical(float timeRemaining) {
        notifyObservers(GameObserver.GameEventType.TIME_CRITICAL, timeRemaining);
    }
    
    public void notifyCardPlayed(String cardName) {
        notifyObservers(GameObserver.GameEventType.CARD_PLAYED, cardName);
    }
}
