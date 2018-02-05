package game.world.impl;

import game.Engine;
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
 *
 * @author Brett Taylor
 */
public class IntroductionWorld extends World {
    private static final float STARTING_TEXT_OPACITY = 0.f;
    private static final float ENDING_TEXT_OPACITY = 0.8f;
    private static final int FIRST_TEXT_INTRO = 3000;
    private static final int FIRST_TEXT_OUTRO = FIRST_TEXT_INTRO + 3000;
    private static final int SECOND_TEXT_INTRO = FIRST_TEXT_OUTRO + 3000;
    private static final int SECOND_TEXT_OUTRO = SECOND_TEXT_INTRO + 3000;
    private static final int TEXT_FADE_TIME = 1000;
    private static final String FIRST_TEXT = "space invaders";
    private static final String SECOND_TEXT = "a game by: brett taylor, toby james and jack rimmington.";
    private static final int TIME_TILL_SWAP_TO_NEXT_SCENE = SECOND_TEXT_OUTRO + 1000;

    private Text centeredText;

    /**
     * Creates the introduction world that handles showing the entry scenes of the game.
     *
     * @param screenWidth
     * @param screenHeight
     */
    public IntroductionWorld(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
        scene.setFill(new Color(0, 0, 0, 0.86f));

        centeredText = new Text("");
        centeredText.setFill(Color.WHITE);
        centeredText.setOpacity(0.f);
        centeredText.setFont(Font.font(20.f));
        root.getChildren().add(centeredText);

        doFadeText(FIRST_TEXT_INTRO, TEXT_FADE_TIME, STARTING_TEXT_OPACITY, ENDING_TEXT_OPACITY, FIRST_TEXT);
        doFadeText(FIRST_TEXT_OUTRO, TEXT_FADE_TIME, ENDING_TEXT_OPACITY, STARTING_TEXT_OPACITY, FIRST_TEXT);
        doFadeText(SECOND_TEXT_INTRO, TEXT_FADE_TIME, STARTING_TEXT_OPACITY, ENDING_TEXT_OPACITY, SECOND_TEXT);
        doFadeText(SECOND_TEXT_OUTRO, TEXT_FADE_TIME, ENDING_TEXT_OPACITY, STARTING_TEXT_OPACITY, SECOND_TEXT);

        new Timeline(new KeyFrame(Duration.millis(TIME_TILL_SWAP_TO_NEXT_SCENE),
                ae -> Engine.setWorld(new InstructionWorld(screenWidth, screenHeight)))).play();
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
