package sk.adamhagara.game.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Generic manager for game components of a specific type
 * Provides thread-safe operations for managing game components
 * @param <T> The type of game object this manager handles
 */
public class GameComponentManager<T> {
    private final Map<String, GameComponent<T>> components;
    private final Class<T> gameObjectType;
    
    public GameComponentManager(Class<T> gameObjectType) {
        this.components = new ConcurrentHashMap<>();
        this.gameObjectType = gameObjectType;
    }
    
    /**
     * Adds a component with the given name
     * @param name The component name
     * @param gameObject The game object to wrap
     */
    public void addComponent(String name, T gameObject) {
        GameComponent<T> component = new GameComponent<>(gameObject, name);
        components.put(name, component);
    }
    
    /**
     * Gets a component by name
     * @param name The component name
     * @return The component or null if not found
     */
    public GameComponent<T> getComponent(String name) {
        return components.get(name);
    }
    
    /**
     * Gets a game object by name
     * @param name The component name
     * @return The game object or null if not found
     */
    public T getGameObject(String name) {
        GameComponent<T> component = components.get(name);
        return component != null ? component.getGameObject() : null;
    }
    
    /**
     * Gets all active components
     * @return List of active components
     */
    public List<GameComponent<T>> getActiveComponents() {
        List<GameComponent<T>> activeComponents = new ArrayList<>();
        for (GameComponent<T> component : components.values()) {
            if (component.isActive()) {
                activeComponents.add(component);
            }
        }
        return activeComponents;
    }
    
    /**
     * Performs operation on all active components
     * @param operation The operation to perform
     */
    public void performOnActive(java.util.function.Consumer<GameComponent<T>> operation) {
        for (GameComponent<T> component : components.values()) {
            if (component.isActive()) {
                operation.accept(component);
            }
        }
    }
    
    /**
     * Gets components matching a predicate
     * @param predicate The predicate to test components
     * @return List of matching components
     */
    public List<GameComponent<T>> getComponentsMatching(java.util.function.Predicate<GameComponent<T>> predicate) {
        List<GameComponent<T>> matchingComponents = new ArrayList<>();
        for (GameComponent<T> component : components.values()) {
            if (predicate.test(component)) {
                matchingComponents.add(component);
            }
        }
        return matchingComponents;
    }
    
    /**
     * Deactivates all components
     */
    public void deactivateAll() {
        for (GameComponent<T> component : components.values()) {
            component.setActive(false);
        }
    }
    
    /**
     * Gets the total component count
     * @return Number of components
     */
    public int getComponentCount() {
        return components.size();
    }
    
}
