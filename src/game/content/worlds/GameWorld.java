package game.content.worlds;

import game.AI.AIController;
import game.Engine;
import game.content.FPSCounter;
import game.content.mobs.Enemy;
import game.content.mobs.Missle;
import game.content.mobs.Player;
import game.utils.Settings;
import game.world.World;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The game scene.
 * @author Brett Taylor
 */
public class GameWorld extends World {
    private AIController ai;

    /**
     * Creates a World
     */
    public GameWorld() {
        super();
        Engine.getMainStage().getScene().setFill(Settings.COLORS.SCENE_GOOD_BACKGROUND);
        addGameObject(new FPSCounter());

        Player player = new Player();
        addGameObject(player);
        player.setPosition(new Point2D(
                screenWidth / 2 - (Settings.GAME_SCENE.PADDING * 2),
                screenHeight - (Settings.GAME_SCENE.PADDING * 2) - (Settings.GAME_SCENE.PADDING * 3) - Settings.PLAYER.HEIGHT - 5
        ));

        ai = new AIController(this);
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

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        ai.update(deltaTime);
    }
}
