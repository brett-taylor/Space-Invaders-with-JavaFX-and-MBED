package game.events.points;

/**
 * A interface for the OnPlayerPointsDecreased event.
 * Allows objects to be updated when a player loses points.
 * @author Brett Taylor
 */
public interface OnPlayerPointsDecreased {
    /**
     * Called when a player's points decreases.
     * @param decreaseAmount the amount the points decreases by.
     * @param newPoints the new points of the player.
     */
    void onPlayerPointsDecreased(int decreaseAmount, int newPoints);
}
