package game.content.mobs;

import game.Engine;
import game.content.worlds.GameWorld;
import game.utils.Input;
import game.utils.MBedKeyCode;
import game.utils.ResourceLoader;
import game.utils.Settings;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;


/**
 * Player is ship that the player will take control of.
 * @author Brett Taylor
 */
public class Player extends Mob {
    /**
     * Creates a player.
     */
    public Player() {
        super();
        setImage(ResourceLoader.PLAYER_SPRITE);
        setSize(new Point2D(Settings.PLAYER.WIDTH, Settings.PLAYER.HEIGHT));
        setMissleSpawnOffset(new Point2D(getSize().getX() / 2, 0));
        setShootingRefractoryPeriod(Settings.PLAYER.SHOOTING_REFRACTORY_TIME);
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
}
