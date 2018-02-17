package game.content.text;

import game.Engine;
import game.events.points.OnPlayerPointsDecreased;
import game.events.points.OnPlayerPointsIncreased;
import game.utils.Settings;
import game.world.World;
import javafx.animation.AnimationTimer;
import javafx.scene.text.Text;

/**
 * Player Points that shows in the top left.
 * @author Brett Taylor
 */
public class PlayerPoints implements OnPlayerPointsDecreased, OnPlayerPointsIncreased {
    /**
     * The text node
     */
    private Text text = null;

    /**
     * The player points
     */
    private int points = 0;

    /**
     * Stores the lerp percentage from points displayed to current points
     */
    private float currentPercentage = 0.f;

    /**
     * The current points being displayed.
     */
    private int pointsBeingDisplayed = 0;

    public PlayerPoints(World world) {
        this.text = new Text(Settings.GAME_SCENE.PADDING * 2 + 2, Settings.GAME_SCENE.PADDING * 3 + 10, formatPoints());
        this.text.getStyleClass().add("player-points");
        world.getRoot().getChildren().addAll(text);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        }.start();
    }

    /**
     * Formats the points so it is 5 digits long.
     * @return The formatted string that is 5 digits long.
     */
    private String formatPoints() {
        String pointString = "" + pointsBeingDisplayed;
        if (pointString.length() >= 5)
            return pointString;

        String returnString = "";
        for (int i = 0; i < 5 - pointString.length(); i++)
            returnString += "0";

        returnString += pointsBeingDisplayed;
        return returnString;
    }

    private void update() {
        if (currentPercentage > 1.f)
            return;

        int difference = Math.abs(points - pointsBeingDisplayed);
        int amountToChange = difference;

        if (difference >= 10)
            amountToChange = difference / 10;

        if (points < pointsBeingDisplayed)
            amountToChange = -difference;

        pointsBeingDisplayed += amountToChange;
        text.setText(formatPoints());
    }

    @Override
    public void onPlayerPointsDecreased(int decreaseAmount, int newPoints) {
        points = newPoints;
        currentPercentage = 0.f;
    }

    @Override
    public void onPlayerPointsIncreased(int increaseAmount, int newPoints) {
        points = newPoints;
        currentPercentage = 0.f;
    }
}
