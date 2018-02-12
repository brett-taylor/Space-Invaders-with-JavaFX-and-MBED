package game.world.objects;

/**
 * A interface to allow the object to be updated every frame.
 * @author Brett Taylor
 */
public interface IUpdatable {
    /**
     * Called every frame
     * @param deltaTime The delta time of the current frame.
     */
    void update(float deltaTime);

    /**
     * Returns whether or not the onStart method has been called for this updatable yet.
     * @return true if onStart has already been initiated.
     */
    boolean hasOnStartBeenCalled();

    /**
     * Called when the updatable is added to a world.
     */
    void onStart();

    /**
     * Called when the updatable is removed from a world.
     */
    void onEnd();

    /**
     * Has the IUpdatable has requested to be removed from the world?
     * @return true if the IUpdatable has requested to be removed from the world
     */
    boolean isBeingDestroyed();
}
