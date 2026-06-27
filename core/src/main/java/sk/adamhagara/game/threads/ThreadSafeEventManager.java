package sk.adamhagara.game.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.adamhagara.game.patterns.observer.GameObserver;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Thread-safe event manager for observer pattern
 * Ensures thread-safe notifications to observers
 */
public class ThreadSafeEventManager {
    private static final Logger logger = LoggerFactory.getLogger(ThreadSafeEventManager.class);
    
    private final CopyOnWriteArrayList<GameObserver> observers;
    private final ExecutorService notificationExecutor;
    
    public ThreadSafeEventManager() {
        this.observers = new CopyOnWriteArrayList<>();
        
        // Create daemon thread factory
        ThreadFactory daemonThreadFactory = r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);  // Mark as daemon thread
            thread.setName("ThreadSafeEventManager-" + thread.getId());
            return thread;
        };
        
        this.notificationExecutor = Executors.newCachedThreadPool(daemonThreadFactory);
        logger.debug("ThreadSafeEventManager initialized with daemon threads");
    }
    
    /**
     * Thread-safe observer registration
     */
    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            logger.debug("Observer added: {}", observer.getClass().getSimpleName());
        }
    }
    
    /**
     * Thread-safe observer removal
     */
    public void removeObserver(GameObserver observer) {
        if (observers.remove(observer)) {
            logger.debug("Observer removed: {}", observer.getClass().getSimpleName());
        }
    }
    
    /**
     * Thread-safe event notification
     * Notifies all observers asynchronously
     */
    public void notifyObservers(GameObserver.GameEventType eventType, Object data) {
        if (observers.isEmpty()) {
            return;
        }
        
        // Notify all observers asynchronously to avoid blocking
        for (GameObserver observer : observers) {
            notificationExecutor.submit(() -> {
                try {
                    observer.onGameStateChanged(eventType, data);
                } catch (Exception e) {
                    logger.error("Error notifying observer: {}", observer.getClass().getSimpleName(), e);
                }
            });
        }
    }
    
    /**
     * Get observer count
     */
    public int getObserverCount() {
        return observers.size();
    }
    
    /**
     * Clear all observers
     */
    public void clearObservers() {
        observers.clear();
        logger.debug("All observers cleared");
    }
    
    /**
     * Shutdown the event manager
     */
    public void shutdown() {
        logger.info("Shutting down ThreadSafeEventManager");
        
        // Clear observers first to prevent new notifications
        clearObservers();
        
        // Shutdown immediately
        notificationExecutor.shutdownNow();
        
        try {
            // Wait for threads to terminate with shorter timeout
            if (!notificationExecutor.awaitTermination(500, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                logger.warn("NotificationExecutor did not terminate gracefully, forcing shutdown");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("ThreadSafeEventManager shutdown interrupted");
        }
        
        logger.info("ThreadSafeEventManager shutdown completed");
    }
}
