package sk.adamhagara.game.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.adamhagara.game.gameobjects.Candle;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.gameobjects.StopWatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe game state manager
 * Manages background updates for game objects with proper synchronization
 */
public class GameStateManager {
    private static final Logger logger = LoggerFactory.getLogger(GameStateManager.class);
    
    private final ScheduledExecutorService scheduler;
    private final ExecutorService taskExecutor;
    private final AtomicBoolean isRunning;
    private final AtomicBoolean gameActive;
    private final ReentrantLock stateLock;
    
    // Game objects
    private Candle candle;
    private Entity entity;
    private StopWatch stopwatch;
    
    public GameStateManager() {
        // Create daemon thread factories
        ThreadFactory daemonThreadFactory = r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);  // Mark as daemon thread
            thread.setName("GameStateManager-" + thread.getId());
            return thread;
        };
        
        this.scheduler = Executors.newScheduledThreadPool(2, daemonThreadFactory);
        this.taskExecutor = Executors.newFixedThreadPool(3, daemonThreadFactory);
        this.isRunning = new AtomicBoolean(false);
        this.gameActive = new AtomicBoolean(true);
        this.stateLock = new ReentrantLock();
        
        logger.info("GameStateManager initialized with daemon thread pools");
    }
    
    /**
     * Starts background game state updates
     */
    public void start() {
        if (isRunning.compareAndSet(false, true)) {
            logger.info("Starting background game state updates");
            
            // Only schedule async tasks, no periodic updates
            scheduler.scheduleAtFixedRate(this::processAsyncTasks, 100, 200, TimeUnit.MILLISECONDS);
        }
    }
    
    /**
     * Deactivates game updates (called when game is closing)
     */
    public void gameDeactivate() {
        gameActive.set(false);
        logger.info("Game deactivated - background updates will stop");
        
        // Force immediate shutdown
        forceShutdown();
    }
    
    /**
     * Force immediate shutdown of all threads
     */
    private void forceShutdown() {
        logger.info("Force shutting down all threads");
        
        try {
            // Cancel all scheduled tasks
            scheduler.shutdownNow();
            taskExecutor.shutdownNow();
            
            // Wait minimal time
            Thread.sleep(50);
            
            logger.info("All threads force shutdown completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Force shutdown interrupted");
        }
    }
    
    /**
     * Stops background updates
     */
    public void stop() {
        gameActive.set(false); // Ensure game is deactivated
        if (isRunning.compareAndSet(true, false)) {
            logger.info("Stopping background game state updates");
            forceShutdown();
        }
    }
    
    
    /**
     * Process async tasks like sound loading, calculations, etc.
     */
    private void processAsyncTasks() {
        if (!isRunning.get()) return;
        
        taskExecutor.submit(() -> {
            try {
                // Simulate async game calculations
                Thread.sleep(10); // Simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    /**
     * Thread-safe setter for game objects
     */
    public void setGameObjects(Candle candle, Entity entity, StopWatch stopwatch) {
        stateLock.lock();
        try {
            this.candle = candle;
            this.entity = entity;
            this.stopwatch = stopwatch;
            logger.debug("Game objects updated in thread-safe manner");
        } finally {
            stateLock.unlock();
        }
    }
    
    /**
     * Thread-safe getter for candle
     */
    public Candle getCandle() {
        stateLock.lock();
        try {
            return candle;
        } finally {
            stateLock.unlock();
        }
    }
    
    /**
     * Thread-safe getter for entity
     */
    public Entity getEntity() {
        stateLock.lock();
        try {
            return entity;
        } finally {
            stateLock.unlock();
        }
    }
    
    /**
     * Thread-safe getter for stopwatch
     */
    public StopWatch getStopWatch() {
        stateLock.lock();
        try {
            return stopwatch;
        } finally {
            stateLock.unlock();
        }
    }
    
    /**
     * Submit task for async execution
     */
    public void submitTask(Runnable task) {
        if (isRunning.get()) {
            taskExecutor.submit(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    logger.error("Error in async task execution", e);
                }
            });
        }
    }
    
    /**
     * Check if manager is running
     */
    public boolean isRunning() {
        return isRunning.get();
    }
}
