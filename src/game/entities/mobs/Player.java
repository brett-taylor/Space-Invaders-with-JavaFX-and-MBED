package game.entities.mobs;

import game.Engine;
import game.utils.Settings;
import game.utils.input.Input;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Player is ship that the player will take control of.*
 * @author Brett Taylor
 */
public class Player extends Mob {
    /**
     * Creates a entity.
     * @param position the position the entity will start drawing to on the screen
     * @param size
     */
    public Player(Point2D position, Point2D size) {
        super(position, size);
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
        super.render(graphics, screenWidth, screenHeight);

        graphics.setFill(Color.RED);
        graphics.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        setPosition(new Point2D(1 * getPosition().getX(), Engine.getPlayAreaHeight() - getSize().getY() - 200));

        /*if (Input.isKeyDown(Settings.PLAYERS.LEFT_KEYBOARD) || Input.isKeyDown(Settings.PLAYERS.LEFT_MBED))
            setPosition(getPosition().add(new Point2D(-200 * Engine.getDeltaTime(), getPosition().getY())));
        else if (Input.isKeyDown(Settings.PLAYERS.RIGHT_KEYBOARD) || Input.isKeyDown(Settings.PLAYERS.RIGHT_MBED))
            setPosition(getPosition().add(new Point2D(200 * Engine.getDeltaTime(), getPosition().getY())));*/
    }
}
