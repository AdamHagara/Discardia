package sk.adamhagara.game.interfaces;

/**
 * Interface for objects that can be rendered
 * Provides contract for rendering functionality
 */
public interface Renderable {

    /**
     * Check if object is visible
     * @return true if visible
     */
    boolean isVisible();

    /**
     * Set visibility
     * @param visible visibility state
     */
    void setVisible(boolean visible);

}
