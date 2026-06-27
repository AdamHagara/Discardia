package sk.adamhagara.game.generics;

import sk.adamhagara.game.gameobjects.Candle;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.gameobjects.StopWatch;

/**
 * Generic game component that can hold any game object type
 * Provides type-safe access to game objects with common operations
 * @param <T> The type of game object this component holds
 */
public class GameComponent<T> {
    private final T gameObject;
    private final String componentName;
    private boolean isActive;
    
    public GameComponent(T gameObject, String componentName) {
        this.gameObject = gameObject;
        this.componentName = componentName;
        this.isActive = true;
    }
    
    /**
     * Gets the game object
     * @return The game object of type T
     */
    public T getGameObject() {
        return gameObject;
    }
    
    /**
     * Gets the component name
     * @return The component name
     */
    public String getComponentName() {
        return componentName;
    }
    
    /**
     * Checks if the component is active
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Sets the component active state
     * @param active The active state
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    /**
     * Performs type-specific operations based on the game object type
     * @return Operation result as string
     */
    public String performTypeSpecificOperation() {
        if (!isActive) {
            return "Component is inactive";
        }
        
        if (gameObject instanceof Candle) {
            Candle candle = (Candle) gameObject;
            return String.format("Candle component: fuel=%d%%, isLit=%s", 
                               candle.getFuelPercent(), candle.isLit());
        } else if (gameObject instanceof Entity) {
            Entity entity = (Entity) gameObject;
            return String.format("Entity component: anger=%.1f%%, gameOver=%s", 
                               entity.getAngerLevel(), entity.isGameOver());
        } else if (gameObject instanceof StopWatch) {
            StopWatch stopwatch = (StopWatch) gameObject;
            return String.format("StopWatch component: time=%.1fs, timeRemaining=%s", 
                               stopwatch.getTimeRemaining(), stopwatch.getTimeRemaining());
        } else {
            return String.format("Unknown component: %s", gameObject.getClass().getSimpleName());
        }
    }
    
    @Override
    public String toString() {
        return String.format("GameComponent<%s>[name=%s, active=%s]", 
                           gameObject.getClass().getSimpleName(), 
                           componentName, 
                           isActive);
    }
}
