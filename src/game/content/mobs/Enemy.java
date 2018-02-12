package game.content.mobs;

import game.utils.ResourceLoader;
import game.utils.Settings;
import game.world.objects.Entity;
import javafx.geometry.Point2D;

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
    }
}
