package game.world.impl;

import game.Engine;
import game.world.World;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The opening scene of the game.
 *
 * @author Brett Taylor
 */
public class InstructionWorld extends World {
    private Text centeredText;
    private static final int TIME_TILL_GAME_WORLD = 2000;

    /**
     * Creates the introduction world that handles showing the entry scenes of the game.
     *
     * @param screenWidth
     * @param screenHeight
     */
    public InstructionWorld(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
        scene.setFill(new Color(0.8392, 0.4784, 0.4902, 0.7961));

        centeredText = new Text("This scene will display the mbed picture with what each button does");
        centeredText.setFill(Color.WHITE);
        centeredText.setOpacity(1.f);
        centeredText.setFont(Font.font(20.f));
        root.getChildren().add(centeredText);

        new Timeline(new KeyFrame(Duration.millis(TIME_TILL_GAME_WORLD),
                ae -> Engine.setWorld(new GameWorld(screenWidth, screenHeight)))).play();
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
        super.render(graphics, screenWidth, screenHeight);
        centeredText.setX((screenWidth / 2) - (centeredText.getLayoutBounds().getWidth() / 2));
        centeredText.setY((screenHeight / 2) - (centeredText.getLayoutBounds().getHeight() / 2));
    }
}
