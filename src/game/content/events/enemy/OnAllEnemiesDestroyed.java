package game.content.events.enemy;

/**
 * A interface for the OnAllEnemiesDestroyed event.
 * Allows objects to be updated when all enemies are destroyed.
 * @author Brett Taylor
 */
public interface OnAllEnemiesDestroyed {
    /**
     * Called when all enemies are destroyed
     */
    void onAllEnemiesDestroyed();
}
