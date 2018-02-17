package game.content.text;

import game.world.World;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * A BurstText is a text that will move upwards slightly then start falling.
 * @author Brett Taylor
 */
public class BurstText {
    /**
     * The text node
     */
    private Text text = null;

    /**
     * The current movement speed.
     */
    private float movementUpDown = 1.f;

    /**
     * The animation timer.
     */
    private AnimationTimer updater = null;

    /**
     * Creates a burst text that moves up and then starts to fall.
     * @param world The world the text should be added to.
     * @param string What the text will say.
     * @param position The position where the text will appear.
     * @param cssClasses A collection of strings containing all css classes that will be applied.
     * @param timeTillStartToFade In milliseconds, the amount of time till the text starts to fade.
     * @param timeToFade In milliseconds, the amount of time the text takes to fade.
     * @param upDownRange The momentum the text will start at.
     * @param upDownStep The amount momentum is subtracted by every frame.
     */
    public BurstText(World world, String string, Point2D position, String[] cssClasses, float timeTillStartToFade, float timeToFade,
                     float upDownRange, float upDownStep)
    {
        this.text = new Text(position.getX(), position.getY(), string);
        this.text.getStyleClass().addAll(cssClasses);
        movementUpDown = upDownRange;
        world.getRoot().getChildren().add(this.text);

        updater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                movementUpDown -= upDownStep;
                text.setY(text.getY() - movementUpDown);
            }
        };
        updater.start();

        new Timeline(new KeyFrame(Duration.millis(timeTillStartToFade),
                ae -> {
                    FadeTransition ft = new FadeTransition(Duration.millis(timeToFade), text);
                    ft.setFromValue(0.8f);
                    ft.setToValue(0.f);
                    ft.play();
                }
        )).play();

        new Timeline(new KeyFrame(Duration.millis(timeTillStartToFade + timeToFade), ae -> {
            world.getRoot().getChildren().remove(text);
            updater.stop();
        })).play();
    }
}