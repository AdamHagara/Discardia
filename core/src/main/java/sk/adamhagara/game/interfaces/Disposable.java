package sk.adamhagara.game.interfaces;

/**
 * Interface for objects that need to dispose resources
 * Provides contract for cleanup functionality
 */
public interface Disposable {

    void dispose();

    /**
     * Check if resources are already disposed
     * @return true if disposed
     */
}
