package game.world.impl;

import game.Engine;
import game.entities.mobs.Player;
import game.utils.FPSCounter;
import game.utils.Settings;
import game.world.World;
import javafx.geometry.Point2D;

/**
 * The game scene.
 * @author Brett Taylor
 */
public class GameWorld extends World {
    /**
     * Creates a World
     */
    public GameWorld() {
        super();
        Engine.getMainStage().getScene().setFill(Settings.COLORS.SCENE_GOOD_BACKGROUND);

        addEntity(new Player(
                new Point2D(screenWidth / 2, screenHeight - 300),
                new Point2D(30, 30)
        ));
        addEntity(new FPSCounter());
    }
}
