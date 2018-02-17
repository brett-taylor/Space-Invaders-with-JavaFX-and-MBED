package game.content.worlds;

import game.ai.AIController;
import game.Engine;
import game.content.misc.FPSCounter;
import game.content.mobs.Enemy;
import game.content.mobs.Player;
import game.content.mobs.Wall;
import game.content.text.BurstText;
import game.content.text.PlayerPoints;
import game.content.text.TextUtils;
import game.events.enemy.OnEnemyDestroyed;
import game.utils.Input;
import game.utils.Settings;
import game.world.World;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * The game scene.
 * @author Brett Taylor
 */
public class GameWorld extends World implements OnEnemyDestroyed {
    /**
     * The ai Controller for the game.
     */
    private AIController ai;

    /**
     * The player
     */
    private Player player;

    /**
     * Creates a World
     */
    public GameWorld() {
        super();
        Engine.getMainStage().getScene().setFill(Settings.COLORS.SCENE_GOOD_BACKGROUND);
        addGameObject(new FPSCounter());

        player = new Player();
        addGameObject(player);
        player.setPosition(new Point2D(
                screenWidth / 2 - (Settings.GAME_SCENE.PADDING * 2),
                screenHeight - (Settings.GAME_SCENE.PADDING * 2) - (Settings.GAME_SCENE.PADDING * 3) - Settings.PLAYER.HEIGHT - 5
        ));

        ai = new AIController(this);
        PlayerPoints playerPoints = new PlayerPoints(this);
        player.listenToOnPlayerPointsIncreased(playerPoints);
        player.listenToOnPlayerPointsDecreased(playerPoints);

        Point2D leftMost = new Point2D(Settings.GAME_SCENE.PADDING + 20,
                player.getPosition().getY() - 20 - Settings.WALL.HEIGHT);
        Point2D rightMost = new Point2D(screenWidth - Settings.GAME_SCENE.PADDING - Settings.GAME.RIGHT_SIDE_PADDING - Settings.WALL.WIDTH,
                player.getPosition().getY() - 20 - Settings.WALL.HEIGHT);
        Point2D center = new Point2D(screenWidth / 2 - (Settings.WALL.WIDTH / 2),
                player.getPosition().getY() - 20 - Settings.WALL.HEIGHT);

        addGameObject(new Wall(leftMost));
        addGameObject(new Wall(leftMost.add(new Point2D(0, -(Settings.WALL.HEIGHT + 5)))));
        addGameObject(new Wall(leftMost.add(new Point2D(0, -(Settings.WALL.HEIGHT * 2) + 20))));
        addGameObject(new Wall(center));
        addGameObject(new Wall(center.add(new Point2D(0, -(Settings.WALL.HEIGHT + 5)))));
        addGameObject(new Wall(center.add(new Point2D(0, -(Settings.WALL.HEIGHT * 2) + 20))));
        addGameObject(new Wall(rightMost));
        addGameObject(new Wall(rightMost.add(new Point2D(0, -(Settings.WALL.HEIGHT + 5)))));
        addGameObject(new Wall(rightMost.add(new Point2D(0, -(Settings.WALL.HEIGHT * 2) + 20))));
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

        graphics.setFill(Color.ORANGERED);
        graphics.fillRect(0, Settings.GAME_SCENE.ENEMY_WIN_HEIGHT + Settings.GAME_SCENE.PADDING + Settings.GAME.TITLE_BAR_HEIGHT, screenWidth, 2);

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

    @Override
    public void onEnemyDestroyed(Enemy enemy) {
        player.addPoints(Settings.POINTS.POINTS_ON_ENEMY_DEATH);
    }
}
