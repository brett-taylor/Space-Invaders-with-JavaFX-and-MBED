package game.content.mobs;

import game.content.events.mob.OnMobHealthChanged;
import game.world.objects.Entity;
import game.world.objects.ICollidable;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Mobs will be entites that having a shoot mechanic. (Player and Enemy ships)
 * They are also have health and also have a callBack every time they take damage.
 */
public abstract class Mob extends Entity {
    private float shootingTimer = 0.f;
    private float shootingRefractoryPeriod  = 2.f;
    private Point2D missleSpawnOffset = Point2D.ZERO;
    private int currentHealth = 0;
    private ArrayList<OnMobHealthChanged> onMobHealthChangedEvent;

    /**
     * Creates a mob.
     */
    protected Mob() {
        super();
        currentHealth = 1;
        onMobHealthChangedEvent = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (shootingTimer >= 0)
            shootingTimer -= deltaTime;
    }

    /**
     * Shoots a missle.
     * @return true if it successfully shot (it was not on cooldown).
     */
    protected boolean shoot() {
        if (shootingTimer > 0.f) {
            return false;
        }

        shootingTimer = shootingRefractoryPeriod;
        return true;
    }

    /**
     * Sets the offset of the missle spawn position.
     * @param offset The new offset.
     */
    protected void setMissleSpawnOffset(Point2D offset) {
        missleSpawnOffset = offset;
    }

    /**
     * Gets where the missle should spawn in absolute position.
     * @return the absolute position where the missle should spawn.
     */
    protected Point2D getMissleSpawnAbosluteLocation() {
        return getPosition().add(missleSpawnOffset).add(new Point2D(0, -(getSize().getY() + 1)));
    }

    @Override
    public void collided(ICollidable other) {
        super.collided(other);

        if (other instanceof Missle) {
            setHealth(getHealth() - 1);
        }
    }

    /**
     * Gets the Mob's current health.
     * @return the current health of the mob.
     */
    public int getHealth() {
        return currentHealth;
    }

    /**
     * sets the mob's health
     * @param newHealth the new Mob's health
     */
    public void setHealth(int newHealth) {
        int oldHealth = getHealth();
        currentHealth = newHealth;
        for (OnMobHealthChanged event : onMobHealthChangedEvent)
            event.onMobHealthChanged(this, oldHealth, getHealth());

        if (newHealth <= 0)
            destroy();
    }

    /**
     * Listens to the OnMobHealthEvent changed for this mob.
     * @param onMobHealthChanged The listener
     */
    public void listenToOnMobHealthChanged(OnMobHealthChanged onMobHealthChanged) {
        onMobHealthChangedEvent.add(onMobHealthChanged);
    }

    /**
     * Stops listening to the OnMobHealthEvent changed for this mob.
     * @param onMobHealthChanged The current listener ot stop listening.
     */
    public void delistenToOnMobHealthChanged(OnMobHealthChanged onMobHealthChanged) {
        onMobHealthChangedEvent.remove(onMobHealthChanged);
    }
}
