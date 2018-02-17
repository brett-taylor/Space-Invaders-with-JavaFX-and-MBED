package game.content.misc;

import game.utils.Settings;
import game.world.objects.GameObject;
import javafx.scene.canvas.GraphicsContext;

/**
 * A object that will draw the current fps to the screen every frame.
 * @author Brett Taylor
 */
public class FPSCounter extends GameObject {
    private float fps;

    public FPSCounter(){
        super();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        fps = 1 / deltaTime;
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
        super.render(graphics, screenWidth, screenHeight);
        graphics.fillText(Float.toString(fps), 10, screenHeight);
    }
}
