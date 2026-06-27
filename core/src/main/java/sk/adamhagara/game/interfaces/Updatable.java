package sk.adamhagara.game.interfaces;

/**
 * Interface for objects that can be updated
 * Provides contract for update functionality
 */
public interface Updatable {

    /**
     * Update the object state
     * @param delta time since last frame in seconds
     */
    void update(float delta);
}
