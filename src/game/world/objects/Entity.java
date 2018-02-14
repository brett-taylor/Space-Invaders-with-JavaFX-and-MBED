package game.world.objects;

import game.Engine;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;

/**
 * A Entity is a class that has a position, size and has the ability to draw a image to the screen.
 * If the entity is not given a image to draw by default it will render a rectangle to the canvas
 * in its current position with its current width and height. (E.G A entity that does not need a image but
 * rather will want to render by drawing to the canvas would use this.)
 * @author Brett Taylor
 */
public class Entity extends GameObject implements ICollidable {
    /**
     * The position of the enemy
     */
    private Point2D position;

    /**
     * The size of the enemy
     */
    private Point2D size;

    /**
     * The color of the square that is drawn if the entity has no color
     */
    private Color renderColor = Color.PINK;

    /**
     * The image that the entity will draw.
     */
    private Image image = null;

    /**
     * Creates a Entity.
     */
    public Entity() {
        size = Point2D.ZERO;
        position = Point2D.ZERO;
    }

    /**
     * Returns the position of the entity.
     * @return the position of the entity.
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Returns the size of the entity
     * @return the size of the entity
     */
    public Point2D getSize() {
        return size;
    }

    /**
     * Sets the position of the entity
     * @param position the new position of the entity on the screen.
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Sets the size of the entity
     * @param size the size of the entity.
     */
    public void setSize(Point2D size) {
        this.size = size;
    }

    /**
     * Returns the color the entity is currently rendering in
     * @return the current rendering color.
     */
    public Color getRenderColor() {
        return renderColor;
    }

    /**
     * Sets the rendering color for this entity.
     * @param color the new rendering color.
     */
    public void setRenderColor(Color color) {
        this.renderColor = color;
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
        super.render(graphics, screenWidth, screenHeight);

        if (image == null) {
            graphics.setFill(getRenderColor());
            graphics.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
        } else {
            graphics.drawImage(image, getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
        }
    }

    /**
     * Sets the image the canvas is drawing to the canvas
     * @param image the new Image
     */
    protected void setImage(Image image) {
        this.image = image;
    }

    @Override
    public Rectangle getCollisionBox() {
        return new Rectangle((int) getPosition().getX(), (int) getPosition().getY(), (int) getSize().getX(), (int) getSize().getY());
    }

    @Override
    public void collided(ICollidable other) {

    }

    @Override
    public void onStart() {
        super.onStart();

        Engine.getCurrentWorld().addCollidable(this);
    }

    @Override
    public void onEnd() {
        super.onEnd();

        Engine.getCurrentWorld().removeCollidable(this);
    }
}
