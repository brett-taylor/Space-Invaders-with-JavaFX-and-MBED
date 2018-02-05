package game.entities;

import javafx.scene.canvas.GraphicsContext;

/**
 * Draw interface.
 *
 * @author Brett Taylor
 */
public interface IDrawable {
    /**
     * Starts rendering of the object
     *
     * @param graphics     the graphics context
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    void render(GraphicsContext graphics, double screenWidth, double screenHeight);
}
