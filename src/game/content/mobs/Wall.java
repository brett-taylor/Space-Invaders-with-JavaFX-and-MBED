package game.content.mobs;

import game.utils.Settings;
import javafx.geometry.Point2D;

/**
 * The wall class will spawn above players and be able to block missles from both sides.
 */
public class Wall extends Mob {
    public Wall(Point2D position) {
        setSize(new Point2D(Settings.WALL.WIDTH, Settings.WALL.HEIGHT));
        setPosition(position);
        setRenderColor(Settings.WALL.COLOR);
        setHealth(1);
    }
}
