package game.events.enemy;

import game.content.mobs.Enemy;

import java.util.ArrayList;

/**
 * A interface for the OnMultiEnemyDestroyed event.
 * Allows objects to be updated when multiple enemies die in quick succession.
 * @author Brett Taylor
 */
public interface OnMultipleEnemyDestroyed {
    /**
     * Called when multiple enemies are killed in quick succession.
     * @param enemies The collection of enemies that were killed.
     */
    void onMultipleEnemyDestroyed(ArrayList<Enemy> enemies);
}
