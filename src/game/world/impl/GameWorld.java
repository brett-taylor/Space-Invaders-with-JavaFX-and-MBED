package game.world.impl;

import game.Engine;
import game.entities.mobs.Player;
import game.utils.FPSCounter;
import game.utils.Settings;
import game.world.World;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Set;

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

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
        super.render(graphics, screenWidth, screenHeight);
        graphics.setFill(Color.WHITE);

        // Top-left
        graphics.fillRect(Settings.GAME_SCENE.PADDING, Settings.GAME_SCENE.PADDING, Settings.GAME_SCENE.RECTNAGLE_LENGTH, Settings.GAME_SCENE.RECTNAGLE_THICKNESS);
        graphics.fillRect(Settings.GAME_SCENE.PADDING, Settings.GAME_SCENE.PADDING, Settings.GAME_SCENE.RECTNAGLE_THICKNESS, Settings.GAME_SCENE.RECTNAGLE_LENGTH);
        // Bottom-left
        graphics.fillRect(Settings.GAME_SCENE.PADDING, screenHeight - Settings.GAME_SCENE.PADDING - Settings.GAME_SCENE.RECTNAGLE_THICKNESS, Settings.GAME_SCENE.RECTNAGLE_LENGTH, Settings.GAME_SCENE.RECTNAGLE_THICKNESS);
        graphics.fillRect(Settings.GAME_SCENE.PADDING, screenHeight - Settings.GAME_SCENE.PADDING - Settings.GAME_SCENE.RECTNAGLE_LENGTH, Settings.GAME_SCENE.RECTNAGLE_THICKNESS, Settings.GAME_SCENE.RECTNAGLE_LENGTH);
        // Top-right
        graphics.fillRect(screenWidth - Settings.GAME_SCENE.PADDING - Settings.GAME_SCENE.RECTNAGLE_LENGTH, Settings.GAME_SCENE.PADDING, Settings.GAME_SCENE.RECTNAGLE_LENGTH, Settings.GAME_SCENE.RECTNAGLE_THICKNESS);
        graphics.fillRect(screenWidth - Settings.GAME_SCENE.PADDING - Settings.GAME_SCENE.RECTNAGLE_THICKNESS, Settings.GAME_SCENE.PADDING, Settings.GAME_SCENE.RECTNAGLE_THICKNESS, Settings.GAME_SCENE.RECTNAGLE_LENGTH);
        // Bottom-right
        graphics.fillRect(screenWidth - Settings.GAME_SCENE.PADDING - Settings.GAME_SCENE.RECTNAGLE_LENGTH, screenHeight - Settings.GAME_SCENE.PADDING - Settings.GAME_SCENE.RECTNAGLE_THICKNESS, Settings.GAME_SCENE.RECTNAGLE_LENGTH, Settings.GAME_SCENE.RECTNAGLE_THICKNESS);
        graphics.fillRect(screenWidth - Settings.GAME_SCENE.PADDING - Settings.GAME_SCENE.RECTNAGLE_THICKNESS, screenHeight - Settings.GAME_SCENE.PADDING - Settings.GAME_SCENE.RECTNAGLE_LENGTH, Settings.GAME_SCENE.RECTNAGLE_THICKNESS, Settings.GAME_SCENE.RECTNAGLE_LENGTH);

        // Bottom line
        graphics.setFill(Settings.COLORS.SPACE_INVADER_GREEN);
        graphics.fillRect(
                Settings.GAME_SCENE.PADDING * 2,
                screenHeight - (Settings.GAME_SCENE.PADDING * 2) - (Settings.GAME_SCENE.PADDING * 3),
                screenWidth - (Settings.GAME_SCENE.PADDING * 2) - (Settings.GAME_SCENE.PADDING * 2),
                1
        );
    }
}
