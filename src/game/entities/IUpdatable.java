package game.entities;

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
     * Called when the updatable is added to a world.
     */
    void onStart();

    /**
     * Called when the updatable is removed from a world.
     */
    void onEnd();
}
