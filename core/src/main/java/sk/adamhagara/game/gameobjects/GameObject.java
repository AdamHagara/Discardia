package sk.adamhagara.game.gameobjects;

import sk.adamhagara.game.interfaces.Disposable;
import sk.adamhagara.game.interfaces.Renderable;
import sk.adamhagara.game.interfaces.Updatable;

/**
 * Abstract base class for all game objects
 * Implements common interfaces and provides basic functionality
 */
public abstract class GameObject implements Updatable, Renderable, Disposable {
    protected float x, y;
    protected boolean active = true;
    protected boolean visible = true;
    protected boolean disposed = false;

    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Position methods with encapsulation
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // Visibility management
    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    // Disposal management

    @Override
    public void dispose() {
        if (!disposed) {
            disposed = true;
            onDispose();
        }
    }

    /**
     * Called when object is disposed - override in subclasses
     */
    protected void onDispose() {
        // Default implementation
    }

    // Abstract methods from interfaces
    @Override
    public abstract void update(float delta);
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + x + ", " + y + ")";
    }
}
