package game.entities.mobs.player;

import game.Engine;
import game.entities.mobs.Mob;
import game.utils.Input;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Player is ship that the player will take control of.
 *
 * @author Brett Taylor
 */
public class Player extends Mob {
    /**
     * Creates a entity.
     *
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

        if (Input.isKeyDown(KeyCode.A))
            left();

    }

    /**
     * Moves the player left by their movement amount and delta time.
     */
    public void left() {
        setPosition(getPosition().add(new Point2D(-200 * Engine.getDeltaTime(), 0)));
    }
}
