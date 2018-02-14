package game.content.worlds;

import game.Engine;
import game.utils.Settings;
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
 * @author Brett Taylor
 */
public class InstructionWorld extends World {
    /**
     * The text that appears in the middle of the screen
     */
    private Text centeredText;

    /**
     * The amount of time in miliseconds before the game switches to the next scene.
     */
    private static final int TIME_TILL_GAME_WORLD = 2000;

    /**
     * Creates the introduction world that handles showing the entry scenes of the game.
     */
    public InstructionWorld() {
        super();
        Engine.getMainStage().getScene().setFill(Settings.COLORS.SCENE_BAD_BACKGROUND);

        centeredText = new Text("This scene will display the mbed picture with what each button does");
        centeredText.setFill(Color.WHITE);
        centeredText.setOpacity(1.f);
        centeredText.setFont(Font.font(20.f));
        root.getChildren().add(centeredText);

        new Timeline(new KeyFrame(Duration.millis(TIME_TILL_GAME_WORLD),
                ae -> Engine.setWorld(new GameWorld()))).play();
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
        super.render(graphics, screenWidth, screenHeight);
        centeredText.setX((screenWidth / 2) - (centeredText.getLayoutBounds().getWidth() / 2));
        centeredText.setY((screenHeight / 2) - (centeredText.getLayoutBounds().getHeight() / 2));
    }
}
