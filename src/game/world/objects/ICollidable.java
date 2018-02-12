package game.world.objects;

import java.awt.Rectangle;

/**
 * This allows an item to be checked if its been collided with other ollidables.
 * They are added to the World but must handle telling the World to remove themselves as well.
 * @author Brett Taylor
 */
public interface ICollidable {
    /**
     * Returns the collision box.
     * @return Returns the collision box.
     */
    Rectangle getCollisionBox();

    /**
     * Called when the ICollidable collides with another ICollidable.
     * @param other The ICollidable it collided with.
     */
    void collided(ICollidable other);
}
