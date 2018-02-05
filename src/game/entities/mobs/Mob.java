package game.entities.mobs;

import game.entities.Entity;
import javafx.geometry.Point2D;

/**
 * Mobs are entites that have the ability to shoot.
 *
 * @author Brett Taylor
 */
public abstract class Mob extends Entity {
    /**
     * Creates a entity.
     *
     * @param position the position the entity will start drawing to on the screen
     * @param size
     */
    public Mob(Point2D position, Point2D size) {
        super(position, size);
    }
}
