package game.content.events.enemy;

import game.AI.EnemyGroup;
import game.content.mobs.Mob;

/**
 * A interface for the OnEnemyGroupDestroyed event.
 * Allows objects to be updated when a Enemy group is destroyed.
 * @author Brett Taylor
 */
public interface OnEnemyGroupDestroyed {
    /**
     * Called when a enemy group is destroyed
     * @param enemyGroup the enemy group that was destroyed
     */
    void onEnemyGroupDestroyed(EnemyGroup enemyGroup);
}
