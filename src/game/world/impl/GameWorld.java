package game.world.impl;

import game.entities.mobs.player.Player;
import game.world.World;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * The game scene.
 *
 * @author Brett Taylor
 */
public class GameWorld extends World {
    private Player player;

    /**
     * Creates a World
     *
     * @param screenWidth
     * @param screenHeight
     */
    public GameWorld(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
        scene.setFill(new Color(0, 0, 0, 0.86f));

        player = new Player(
                new Point2D(screenWidth / 2, screenHeight - 70),
                new Point2D(30, 30)
        );
        addEntity(player);
    }
}
