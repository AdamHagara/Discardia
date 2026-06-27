package sk.adamhagara.game.patterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject interface for Observer pattern
 * Manages observer registration and notifications
 */
public interface GameSubject {
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void notifyObservers(GameObserver.GameEventType eventType, Object data);
}

/**
 * Concrete implementation of GameSubject
 * Manages the list of observers and notification logic
 */
class GameSubjectImpl implements GameSubject {
    private final List<GameObserver> observers = new ArrayList<>();
    
    @Override
    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(GameObserver.GameEventType eventType, Object data) {
        for (GameObserver observer : observers) {
            observer.onGameStateChanged(eventType, data);
        }
    }
}
