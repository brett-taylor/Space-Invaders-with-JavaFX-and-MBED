package game.world.objects;

import javafx.scene.canvas.GraphicsContext;

/**
 * A GameObject is the lowest possible object that can exist in the game world.
 * It will be updated every frame and can draw to the canvas every frame.
 */
public abstract class GameObject implements IUpdatable, IDrawable {
    private boolean isBeingDestroyed = false;
    private boolean onStartCalled = false;

    /**
     * Creates a GameObject.
     */
    public GameObject() {
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public boolean hasOnStartBeenCalled() {
        return onStartCalled;
    }

    @Override
    public void onStart() {
        onStartCalled = true;
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
    }

    /**
     * Will remove the GameObject from the world.
     */
    public final void destroy() {
        isBeingDestroyed = true;
    }

    /**
     * Returns wether or not the GameObject is being removed from the world.
     * @return true if the GameObject will be removed by the world.
     */
    public final boolean isBeingDestroyed() {
        return isBeingDestroyed;
    }
}
