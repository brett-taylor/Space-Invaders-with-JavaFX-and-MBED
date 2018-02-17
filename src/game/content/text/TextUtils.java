package game.content.text;

import game.world.World;

import javafx.geometry.Point2D;

/**
 * A handy utility class to quickly create text elements.
 * @author Brett Taylor
 */
public class TextUtils {

    /**
     * Creates a BurstText that will be used when a player gets points.
     * @param world The world to be added to
     * @param text The text the string will display
     * @param position The position of the text
     */
    public static void createPointsPopup(World world, String text, Point2D position) {
        new BurstText(world, text, position, new String[]{"burst-text"}, 200, 700, 1.f, 0.05f);
    }
}
