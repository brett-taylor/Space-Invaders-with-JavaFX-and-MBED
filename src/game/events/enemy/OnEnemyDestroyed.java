package game.events.enemy;

import game.content.mobs.Enemy;

/**
 * A interface for the OnEnemyDestroyed event.
 * Allows objects to be updated when a enemy is destroyed.
 * @author Brett Taylor
 */
public interface OnEnemyDestroyed {
    /**
     * Called when a enemy is destroyed
     * @param enemy The enemy destroyed
     */
    void onEnemyDestroyed(Enemy enemy);
}
