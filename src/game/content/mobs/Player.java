package game.content.mobs;

import game.Engine;
import game.content.worlds.GameWorld;
import game.events.points.OnPlayerPointsDecreased;
import game.events.points.OnPlayerPointsIncreased;
import game.utils.Input;
import game.utils.ResourceLoader;
import game.utils.Settings;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;

import java.util.ArrayList;


/**
 * Player is ship that the player will take control of.
 * @author Brett Taylor
 */
public class Player extends Mob {
    /**
     * The amount of points the player has earned.
     */
    private int points = 0;

    /**
     * The collection of objects listening to the OnPlayerPointsIncreased event.
     */
    private ArrayList<OnPlayerPointsIncreased> onPlayerPointsIncreasedEvent;

    /**
     * The collection of objects listening to the OnPlayerPointsDecreased event.
     */
    private ArrayList<OnPlayerPointsDecreased> onPlayerPointsDecreasedEvent;

    /**
     * Creates a player.
     */
    public Player() {
        super();
        setImage(ResourceLoader.PLAYER_SPRITE);
        setSize(new Point2D(Settings.PLAYER.WIDTH, Settings.PLAYER.HEIGHT));
        setMissleSpawnOffset(new Point2D(getSize().getX() / 2, 0));
        setShootingRefractoryPeriod(Settings.PLAYER.SHOOTING_REFRACTORY_TIME);
        onPlayerPointsIncreasedEvent = new ArrayList<>();
        onPlayerPointsDecreasedEvent = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (Input.isKeyDown(Settings.PLAYER.LEFT_KEYBOARD) || Input.isKeyDown(Settings.PLAYER.LEFT_MBED))
            if (getPosition().getX() > Settings.GAME_SCENE.PADDING)
                setPosition(new Point2D(getPosition().getX() - (deltaTime * Settings.PLAYER.MOVEMENT_SPEED), getPosition().getY()));

        if (Input.isKeyDown(Settings.PLAYER.RIGHT_KEYBOARD) || Input.isKeyDown(Settings.PLAYER.RIGHT_MBED))
            if (Engine.getPlayAreaWidth() - Settings.GAME_SCENE.PADDING - Settings.GAME.RIGHT_SIDE_PADDING >  getPosition().getX() + Settings.PLAYER.WIDTH)
                setPosition(new Point2D(getPosition().getX() + (deltaTime * Settings.PLAYER.MOVEMENT_SPEED), getPosition().getY()));

        if (Input.isKeyDown(Settings.PLAYER.SHOOT_KEYBOARD) || Input.isKeyDown(Settings.PLAYER.SHOOT_MBED)) {
            shoot();
        }
    }

    @Override
    public boolean shoot() {
        if (!super.shoot())
            return false;

        Missle missle = new Missle(VerticalDirection.UP, Settings.MISSLE.FRIENDLY_MISSLE, Settings.MISSLE.FRIENDLY_MOVEMENT_SPEED);
        missle.setPosition(getMissleSpawnAbosluteLocation());
        Engine.getCurrentWorld().addGameObject(missle);
        return true;
    }

    @Override
    public void setHealth(int newHealth) {
        super.setHealth(newHealth);

        if (newHealth == 0)
            Engine.setWorld(new GameWorld());
    }

    /**
     * Adds some points to the player.
     * @param points the amount of points to add.
     */
    public void addPoints(int points) {
        if (points <= 0) {
            System.out.println("Attempted to add negative points to the player");
            return;
        }

        this.points += points;
        for (OnPlayerPointsIncreased event : onPlayerPointsIncreasedEvent)
            event.onPlayerPointsIncreased(points, this.points);
    }

    /**
     * Subtract some points from the player.
     * @param points the amount of points to subtract.
     */
    public void subtractPoints(int points) {
        if (points <= 0) {
            System.out.println("Attempted to subtract negative points to the player");
            return;
        }

        this.points -= points;
        for (OnPlayerPointsDecreased event : onPlayerPointsDecreasedEvent)
            event.onPlayerPointsDecreased(points, this.points);
    }

    /**
     * Returns the points the player has earned.
     * @return the points the player has earned.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Listens to the OnPlayerPointsIncreased event.
     * @param onPlayerPointsIncreased The listener
     */
    public void listenToOnPlayerPointsIncreased(OnPlayerPointsIncreased onPlayerPointsIncreased) {
        onPlayerPointsIncreasedEvent.add(onPlayerPointsIncreased);
    }

    /**
     * Stops listening to the OnPlayerPointsIncreased event.
     * @param onPlayerPointsIncreased The current listener to stop listening.
     */
    public void delistenToOnPlayerPointsIncreased(OnPlayerPointsIncreased onPlayerPointsIncreased) {
        onPlayerPointsIncreasedEvent.remove(onPlayerPointsIncreased);
    }

    /**
     * Listens to the OnPlayerPointsDecreased event.
     * @param onPlayerPointsDecreased The listener
     */
    public void listenToOnPlayerPointsDecreased(OnPlayerPointsDecreased onPlayerPointsDecreased) {
        onPlayerPointsDecreasedEvent.add(onPlayerPointsDecreased);
    }

    /**
     * Stops listening to the OnPlayerPointsDecreased event.
     * @param onPlayerPointsDecreased The current listener to stop listening.
     */
    public void delistenToOnPlayerPointsDecreased(OnPlayerPointsDecreased onPlayerPointsDecreased) {
        onPlayerPointsDecreasedEvent.remove(onPlayerPointsDecreased);
    }
}
