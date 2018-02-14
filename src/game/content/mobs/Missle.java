package game.content.mobs;

import game.Engine;
import game.utils.Settings;
import game.world.objects.Entity;
import game.world.objects.ICollidable;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.paint.Color;

/**
 * A missle class that when collides with another Entity will call hitByMissle() on that Entity and destroy itself.
 * Will ignore GameObjects as they do not have a way to detect physics.
 * @author Brett Taylor
 */
public class Missle extends Entity {
    /**
     * The vertical direction that the missle is heading.
     */
    private VerticalDirection movementDirection = VerticalDirection.DOWN;

    /**
     * The missle movement speed
     */
    private float movementSpeed = 200.f;

    /**
     * Creates a missle class
     @param direction States what vertical direction the missle should be going.
     */
    public Missle(VerticalDirection direction, Color missleColor, float movementSpeed) {
        super();
        movementDirection = direction;
        setRenderColor(missleColor);
        setSize(new Point2D(Settings.MISSLE.WIDTH, Settings.MISSLE.HEIGHT));
        setPosition(new Point2D(100, 350));
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isBeingDestroyed() || getPosition().getY() < -Settings.MISSLE.HEIGHT || getPosition().getY() > Engine.getPlayAreaHeight() + Settings.MISSLE.HEIGHT) {
            destroy();
            return;
        }

        for (ICollidable other: Engine.getCurrentWorld().getCollidables()) {
            if (this == other)
                continue;

            if (getCollisionBox().intersects(other.getCollisionBox())) {
                destroy();
                other.collided(this);
            }
        }

        float movementAmount = movementSpeed * deltaTime;
        if (movementDirection == VerticalDirection.UP) {
            movementAmount = -movementAmount;
        }
        setPosition(getPosition().add(new Point2D(0, movementAmount)));
    }
}
