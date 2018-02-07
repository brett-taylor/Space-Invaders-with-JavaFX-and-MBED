package game.entities;

import game.utils.Settings;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * The base of any object in the game that needs to display a image and move.
 */
public abstract class Entity implements IUpdatable, IDrawable {
    private Point2D position;
    private Point2D size;

    /**
     * Creates a entity.
     * @param position the position the entity will start drawing to on the screen
     */
    public Entity(Point2D position, Point2D size) {
        setPosition(position);
        setSize(size);
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
    }

    /**
     * The position of the entity.
     * The Y coordinate is offset by the title bar height as it seems that the title bar is not taken into
     * consideration normally for some reason.
     * @param pos the new position of the entity
     */
    public void setPosition(Point2D pos) {
        this.position = pos;
    }

    /**
     * Gets the position of the entity.
     * @return the position of the entity.
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Sets the size of the entity
     * @param size the new size of the entity
     */
    public void setSize(Point2D size) {
        this.size = size;
    }

    /**
     * Gets the size of the entity
     * @return the size of the entity.
     */
    public Point2D getSize() {
        return size;
    }
}
