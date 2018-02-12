package game.content.worlds;

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
 * The opening scene of the game.

 * @author Brett Taylor
 */
public class IntroductionWorld extends World {
    private Text centeredText;

    /**
     * Creates the introduction world that handles showing the entry scenes of the game.
     */
    public IntroductionWorld() {
        super();
        Engine.getMainStage().getScene().setFill(Settings.COLORS.SCENE_GOOD_BACKGROUND);

        centeredText = new Text("");
        centeredText.setFill(Color.WHITE);
        centeredText.setOpacity(0.f);
        centeredText.setFont(Font.font(20.f));
        root.getChildren().add(centeredText);

        doFadeText(Settings.INTRODUCTION_SCENE.FIRST_TEXT_INTRO,
                Settings.INTRODUCTION_SCENE.TEXT_FADE_TIME,
                Settings.INTRODUCTION_SCENE.STARTING_TEXT_OPACITY,
                Settings.INTRODUCTION_SCENE.ENDING_TEXT_OPACITY,
                Settings.INTRODUCTION_SCENE.FIRST_TEXT);

        doFadeText(Settings.INTRODUCTION_SCENE.FIRST_TEXT_OUTRO,
                Settings.INTRODUCTION_SCENE.TEXT_FADE_TIME,
                Settings.INTRODUCTION_SCENE.ENDING_TEXT_OPACITY,
                Settings.INTRODUCTION_SCENE.STARTING_TEXT_OPACITY,
                Settings.INTRODUCTION_SCENE.FIRST_TEXT);


        doFadeText(Settings.INTRODUCTION_SCENE.SECOND_TEXT_INTRO,
                Settings.INTRODUCTION_SCENE.TEXT_FADE_TIME,
                Settings.INTRODUCTION_SCENE.STARTING_TEXT_OPACITY,
                Settings.INTRODUCTION_SCENE.ENDING_TEXT_OPACITY,
                Settings.INTRODUCTION_SCENE.SECOND_TEXT);

        doFadeText(Settings.INTRODUCTION_SCENE.SECOND_TEXT_OUTRO,
                Settings.INTRODUCTION_SCENE.TEXT_FADE_TIME,
                Settings.INTRODUCTION_SCENE.ENDING_TEXT_OPACITY,
                Settings.INTRODUCTION_SCENE.STARTING_TEXT_OPACITY,
                Settings.INTRODUCTION_SCENE.SECOND_TEXT);

        new Timeline(new KeyFrame(Duration.millis(Settings.INTRODUCTION_SCENE.TIME_TILL_SWAP_TO_NEXT_SCENE),
                ae -> Engine.setWorld(new InstructionWorld()))).play();
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
