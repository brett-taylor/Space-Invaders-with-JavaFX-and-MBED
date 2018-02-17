package game.events.points;

/**
 * A interface for the OnPlayerPointsIncreased event.
 * Allows objects to be updated when a player gains points.
 * @author Brett Taylor
 */
public interface OnPlayerPointsIncreased {
    /**
     * Called when a player's points increases.
     * @param increaseAmount the amount the points increased by.
     * @param newPoints the new points of the player.
     */
    void onPlayerPointsIncreased(int increaseAmount, int newPoints);
}
