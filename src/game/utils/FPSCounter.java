package game.utils;

import game.entities.Entity;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * Entity that will draw the fps to the screen every frame.
 * @author Brett Taylor
 */
public class FPSCounter extends Entity {
    private float fps;

    public FPSCounter(){
        super(Point2D.ZERO, Point2D.ZERO);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        fps = 1 / deltaTime;
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
        super.render(graphics, screenWidth, screenHeight);
        graphics.fillText(Float.toString(fps), 5, 30);
    }
}
