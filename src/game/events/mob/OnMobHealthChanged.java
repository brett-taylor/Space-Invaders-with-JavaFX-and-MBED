package game.events.mob;

import game.content.mobs.Mob;

/**
 * A interface for the onMobHealth changed event.
 * Allows objects to be updated when a mob health changes.
 * @author Brett Taylor
 */
public interface OnMobHealthChanged {
    /**
     * Called when a mob health changes
     * @param mob the mob affected
     * @param oldHealth the old health of the mob
     * @param newHealth the new health of the mob.
     */
    void onMobHealthChanged(Mob mob, int oldHealth, int newHealth);
}
