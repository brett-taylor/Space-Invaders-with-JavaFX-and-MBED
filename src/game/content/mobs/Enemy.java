package game.content.mobs;

import game.Engine;
import game.events.handlers.EnemyRelatedTextIntergration;
import game.events.enemy.OnEnemyDestroyed;
import game.utils.ResourceLoader;
import game.utils.Settings;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;

import java.util.ArrayList;

/**
 * The enemy that the player must shoot at
 * @author Brett Taylor
 */
public class Enemy extends Mob {
    /**
     * The collection of objects listening to the OnEnemyDestroyed event.
     */
    private ArrayList<OnEnemyDestroyed> onEnemyDestroyedEvent;

    /**
     * Creates a enemy
     */
    public Enemy() {
        super();
        onEnemyDestroyedEvent = new ArrayList<>();
        setImage(ResourceLoader.ENEMY_SPRITE);
        setSize(new Point2D(Settings.ENEMY.WIDTH, Settings.ENEMY.HEIGHT));
        setMissleSpawnOffset(new Point2D(getSize().getX() / 2, 60));
        setShootingRefractoryPeriod(5.f);

        listenToOnEnemyDestroyed(EnemyRelatedTextIntergration.getInstance());
    }

    @Override
    public boolean shoot() {
        if (!super.shoot())
            return false;

        Missle missle = new Missle(VerticalDirection.DOWN, Settings.MISSLE.ENEMY_MISSLE, Settings.MISSLE.ENEMY_MOVEMENT_SPEED);
        missle.setPosition(getMissleSpawnAbosluteLocation());
        Engine.getCurrentWorld().addGameObject(missle);
        return true;
    }

    @Override
    public void setHealth(int newHealth) {
        super.setHealth(newHealth);
        if (newHealth <= 0) {
            for (OnEnemyDestroyed event : onEnemyDestroyedEvent)
                event.onEnemyDestroyed(this);
        }
    }

    /**
     * Listens to the OnMobHealthEvent changed for this mob.
     * @param onEnemyDestroyed The listener
     */
    public void listenToOnEnemyDestroyed(OnEnemyDestroyed onEnemyDestroyed) {
        onEnemyDestroyedEvent.add(onEnemyDestroyed);
    }

    /**
     * Stops listening to the OnMobHealthEvent changed for this mob.
     * @param onEnemyDestroyed The current listener ot stop listening.
     */
    public void delistenToOnEnemyDestroyed(OnEnemyDestroyed onEnemyDestroyed) {
        onEnemyDestroyedEvent.remove(onEnemyDestroyed);
    }
}
