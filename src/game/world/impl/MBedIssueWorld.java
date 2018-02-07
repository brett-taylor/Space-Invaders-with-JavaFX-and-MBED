package game.world.impl;

import game.Engine;
import game.utils.Settings;
import game.world.World;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The scene that will display any mbed issue.
 * @author Brett Taylor
 */
public class MBedIssueWorld extends World {
    private Text centeredText;

    /**
     * Creates the introduction world that handles showing the entry scenes of the game.
     */
    public MBedIssueWorld() {
        super();
        Engine.getMainStage().getScene().setFill(Settings.COLORS.SCENE_BAD_BACKGROUND);

        centeredText = new Text("");
        centeredText.setFill(Color.WHITE);
        centeredText.setOpacity(0.f);
        centeredText.setFont(Font.font(20.f));
        root.getChildren().add(centeredText);

        doFadeText(1000, 1000, 0.f, .8f, "Some Issue With The MBed Occured.");
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
        super.render(graphics, screenWidth, screenHeight);
        centeredText.setX((screenWidth / 2) - (centeredText.getLayoutBounds().getWidth() / 2));
        centeredText.setY((screenHeight / 2) - (centeredText.getLayoutBounds().getHeight() / 2));
    }

    /**
     * Does a fade text on the centered text in the middle of the screen.
     */
    private void doFadeText(int initialDelay, int textFadeTime, float startingOpacity, float endingOpacity, String text) {
        new Timeline(new KeyFrame(
                Duration.millis(initialDelay),
                ae -> {
                    centeredText.setText(text);
                    FadeTransition ft = new FadeTransition(Duration.millis(textFadeTime), centeredText);
                    ft.setFromValue(startingOpacity);
                    ft.setToValue(endingOpacity);
                    ft.play();
                }
        )).play();
    }
}
