package sk.adamhagara.game.generics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.adamhagara.game.gameobjects.Candle;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.gameobjects.StopWatch;

/**
 * Generic manager for all game objects
 * Uses GameComponentManager with different types for type-safe operations
 */
public class GameObjectManager {
    private static final Logger logger = LoggerFactory.getLogger(GameObjectManager.class);
    
    private final GameComponentManager<Candle> candleManager;
    private final GameComponentManager<Entity> entityManager;
    private final GameComponentManager<StopWatch> stopwatchManager;
    
    public GameObjectManager() {
        this.candleManager = new GameComponentManager<>(Candle.class);
        this.entityManager = new GameComponentManager<>(Entity.class);
        this.stopwatchManager = new GameComponentManager<>(StopWatch.class);
    }
    
    /**
     * Adds a candle component
     * @param name The candle name
     * @param candle The candle object
     */
    public void addCandle(String name, Candle candle) {
        candleManager.addComponent(name, candle);
    }
    
    /**
     * Adds an entity component
     * @param name The entity name
     * @param entity The entity object
     */
    public void addEntity(String name, Entity entity) {
        entityManager.addComponent(name, entity);
    }
    
    /**
     * Adds a stopwatch component
     * @param name The stopwatch name
     * @param stopwatch The stopwatch object
     */
    public void addStopwatch(String name, StopWatch stopwatch) {
        stopwatchManager.addComponent(name, stopwatch);
    }
    
    /**
     * Gets a candle component by name
     * @param name The candle name
     * @return The candle component or null if not found
     */
    public GameComponent<Candle> getCandleComponent(String name) {
        return candleManager.getComponent(name);
    }
    
    /**
     * Gets an entity component by name
     * @param name The entity name
     * @return The entity component or null if not found
     */
    public GameComponent<Entity> getEntityComponent(String name) {
        return entityManager.getComponent(name);
    }
    
    /**
     * Gets a stopwatch component by name
     * @param name The stopwatch name
     * @return The stopwatch component or null if not found
     */
    public GameComponent<StopWatch> getStopwatchComponent(String name) {
        return stopwatchManager.getComponent(name);
    }
    
    /**
     * Gets a candle by name
     * @param name The candle name
     * @return The candle or null if not found
     */
    public Candle getCandle(String name) {
        return candleManager.getGameObject(name);
    }
    
    /**
     * Gets an entity by name
     * @param name The entity name
     * @return The entity or null if not found
     */
    public Entity getEntity(String name) {
        return entityManager.getGameObject(name);
    }
    
    /**
     * Gets a stopwatch by name
     * @param name The stopwatch name
     * @return The stopwatch or null if not found
     */
    public StopWatch getStopwatch(String name) {
        return stopwatchManager.getGameObject(name);
    }
    
    /**
     * Gets all active candles
     * @return List of active candle components
     */
    public java.util.List<GameComponent<Candle>> getActiveCandles() {
        return candleManager.getActiveComponents();
    }
    
    /**
     * Gets all active entities
     * @return List of active entity components
     */
    public java.util.List<GameComponent<Entity>> getActiveEntities() {
        return entityManager.getActiveComponents();
    }
    
    /**
     * Gets all active stopwatches
     * @return List of active stopwatch components
     */
    public java.util.List<GameComponent<StopWatch>> getActiveStopwatches() {
        return stopwatchManager.getActiveComponents();
    }
    
    /**
     * Performs operation on all active candles
     * @param operation The operation to perform
     */
    public void performOnActiveCandles(java.util.function.Consumer<GameComponent<Candle>> operation) {
        candleManager.performOnActive(operation);
    }
    
    /**
     * Performs operation on all active entities
     * @param operation The operation to perform
     */
    public void performOnActiveEntities(java.util.function.Consumer<GameComponent<Entity>> operation) {
        entityManager.performOnActive(operation);
    }
    
    /**
     * Performs operation on all active stopwatches
     * @param operation The operation to perform
     */
    public void performOnActiveStopwatches(java.util.function.Consumer<GameComponent<StopWatch>> operation) {
        stopwatchManager.performOnActive(operation);
    }
    
    /**
     * Gets candles with low fuel (less than 30%)
     * @return List of candles with low fuel
     */
    public java.util.List<GameComponent<Candle>> getCandlesWithLowFuel() {
        return candleManager.getComponentsMatching(component -> {
            Candle candle = component.getGameObject();
            return candle.getFuelPercent() < 30f;
        });
    }
    
    /**
     * Gets entities with high anger (more than 80%)
     * @return List of entities with high anger
     */
    public java.util.List<GameComponent<Entity>> getEntitiesWithHighAnger() {
        return entityManager.getComponentsMatching(component -> {
            Entity entity = component.getGameObject();
            return entity.getAngerLevel() > 80f;
        });
    }
    
    /**
     * Gets stopwatches with critical time (less than 10 seconds)
     * @return List of stopwatches with critical time
     */
    public java.util.List<GameComponent<StopWatch>> getStopwatchesWithCriticalTime() {
        return stopwatchManager.getComponentsMatching(component -> {
            StopWatch stopwatch = component.getGameObject();
            return stopwatch.getTimeRemaining() < 10f;
        });
    }
    
    /**
     * Deactivates all game objects
     */
    public void deactivateAll() {
        candleManager.deactivateAll();
        entityManager.deactivateAll();
        stopwatchManager.deactivateAll();
    }
    
    /**
     * Gets total component count
     * @return Total number of components
     */
    public int getTotalComponentCount() {
        return candleManager.getComponentCount() + 
               entityManager.getComponentCount() + 
               stopwatchManager.getComponentCount();
    }
    
    /**
     * Validates game state using lambda expressions
     * @return true if game state is valid, false otherwise
     */
    public boolean validateGameState() {
        // Lambda expression to check if any candle has critical fuel level
        boolean hasCriticalCandle = getCandlesWithLowFuel().stream()
            .anyMatch(component -> component.getGameObject().getFuelPercent() < 10f);
        
        // Lambda expression to check if any entity has critical anger level
        boolean hasCriticalEntity = getEntitiesWithHighAnger().stream()
            .anyMatch(component -> component.getGameObject().getAngerLevel() > 95f);
        
        // Lambda expression to check if any stopwatch has critical time
        boolean hasCriticalStopwatch = getStopwatchesWithCriticalTime().stream()
            .anyMatch(component -> component.getGameObject().getTimeRemaining() < 5f);
        
        // Lambda expression to check overall game health
        java.util.function.Predicate<Boolean> isHealthy = condition -> !condition;
        
        // Return true if no critical conditions exist
        return isHealthy.test(hasCriticalCandle) && 
               isHealthy.test(hasCriticalEntity) && 
               isHealthy.test(hasCriticalStopwatch);
    }
    
    /**
     * Applies emergency actions using lambda expressions
     */
    public void applyEmergencyActions() {
        // Lambda expression for emergency candle relight
        java.util.function.Consumer<GameComponent<Candle>> emergencyRelight = candle -> {
            if (candle != null && candle.isActive() && candle.getGameObject().getFuelPercent() < 10f) {
                candle.getGameObject().relight(50f);
                logger.info("Emergency relight applied to candle: {}", candle.getComponentName());
            }
        };
        
        // Lambda expression for emergency entity pacification
        java.util.function.Consumer<GameComponent<Entity>> emergencyPacify = entity -> {
            if (entity != null && entity.isActive() && entity.getGameObject().getAngerLevel() > 95f) {
                entity.getGameObject().pacify(10f);
                logger.info("Emergency pacification applied to entity: {}", entity.getComponentName());
            }
        };
        
        // Lambda expression for emergency time addition
        java.util.function.Consumer<GameComponent<StopWatch>> emergencyTime = stopwatch -> {
            if (stopwatch != null && stopwatch.isActive() && stopwatch.getGameObject().getTimeRemaining() < 5f) {
                stopwatch.getGameObject().addTime(30f);
                logger.info("Emergency time added to stopwatch: {}", stopwatch.getComponentName());
            }
        };
        
        // Apply emergency actions using lambda expressions
        getActiveCandles().forEach(emergencyRelight);
        getActiveEntities().forEach(emergencyPacify);
        getActiveStopwatches().forEach(emergencyTime);
    }
}
