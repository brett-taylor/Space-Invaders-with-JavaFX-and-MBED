package game.content.mobs;

import game.Engine;
import game.utils.ResourceLoader;
import game.utils.Settings;
import game.world.objects.Entity;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;

/**
 * The enemy that the player must shoot at
 * @author Brett Taylor
 */
public class Enemy extends Mob {
    /**
     * Creates a enemy
     */
    public Enemy() {
        super();
        setImage(ResourceLoader.ENEMY_SPRITE);
        setSize(new Point2D(Settings.ENEMY.WIDTH, Settings.ENEMY.HEIGHT));
        setMissleSpawnOffset(new Point2D(getSize().getX() / 2, 60));
        setShootingRefractoryPeriod(5.f);
    }

    @Override
    public boolean shoot() {
        if (!super.shoot())
            return false;

        Missle missle = new Missle(VerticalDirection.DOWN, Settings.MISSLE.ENEMY_MISSLE, Settings.MISSLE.ENEMY_MOVEMENT_SPEED);
        missle.setPosition(getMissleSpawnAbosluteLocation());
        Engine.getCurrentWorld().addGameObject(missle);
        return true;
    }
}
