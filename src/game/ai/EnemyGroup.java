package game.ai;

import game.events.enemy.OnEnemyDestroyed;
import game.events.enemy.OnEnemyGroupDestroyed;
import game.events.mob.OnMobHealthChanged;
import game.content.mobs.Enemy;
import game.content.mobs.Mob;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Enemy Groups are either enemy rows or columns they are used to keep track of what row and columns
 * still have enemies so the AiController knows how far the enemies have to move left + right and down
 * Enemy groups will also handle moving all enemies in its group left/right or downwards.
 * @author Brett Taylor
 */
public abstract class EnemyGroup implements OnEnemyDestroyed {
    /**
     * The collection of enemies in this group
     */
    protected Enemy[] enemies;

    /**
     * The max amount of enemies that this group is allowed.
     */
    protected final int maxEnemies;

    /**
     * The current amount of enemies alive in the group
     */
    private int enemiesAlive;

    /**
     * The current overall position of the group. Each enemy still has their own position they use.
     */
    protected Point2D position;

    /**
     * The collection of objects listening to the OnEnemyGroupDestroyed event.
     */
    private ArrayList<OnEnemyGroupDestroyed> onEnemyGroupDestroyedEvent;

    /**
     * Creates a enemy group.
     * @param maxEnemies the amount of enemies the group will allow.
     * @param position the starting position of the enemy group
     */
    public EnemyGroup(int maxEnemies, Point2D position) {
        this.maxEnemies = maxEnemies;
        enemies = new Enemy[this.maxEnemies];
        this.position = position;
        onEnemyGroupDestroyedEvent = new ArrayList<>();
    }

    /**
     * Checks all enemies are dead in the group or not
     * @return true if all enemies in the group are dead.
     */
    private boolean areAllEnemiesDead() {
        return enemiesAlive == 0;
    }

    /**
     * Adds a enemy to the enemy group
     * @param enemy the new enemy group
     */
    public void addEnemy(Enemy enemy) {
        if (enemiesAlive >= maxEnemies) {
            System.out.println("Max enemies reached in enemy group.");
            return;
        }

        enemies[enemiesAlive] = enemy;
        enemiesAlive++;
        enemy.listenToOnEnemyDestroyed(this);
    }

    /**
     * Handles positioning the enemies in the required enemy layout.
     */
    public abstract void positionEnemies();

    /**
     * Gets the group's position
     * @return the position of the group
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Sets the position of the group
     * @param position the new position of the group.
     */
    private void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Adds a offset to the position of the group.
     * @param newOffset the offset to be added.
     */
    public void addOffset(Point2D newOffset) {
        setPosition(getPosition().add(newOffset));
        positionEnemies();
    }

    /**
     * Called when a enemy is destroyed
     * @param enemy
     */
    private void enemyDestroyed(Enemy enemy) {
        for (int i = 0; i < maxEnemies; i++) {
            if (enemies[i] == enemy)
                enemies[i] = null;
        }
        enemiesAlive--;
    }

    /**
     * Listens to the OnEnemyGroupDestroyed for this group.
     * @param onEnemyGroupDestroyed The listener
     */
    public void listenToOnEnemyGroupDestroyed(OnEnemyGroupDestroyed onEnemyGroupDestroyed) {
        onEnemyGroupDestroyedEvent.add(onEnemyGroupDestroyed);
    }

    /**
     * Stops listening to the OnEnemyGroupDestroyed changed for this group.
     * @param onEnemyGroupDestroyed The current listener ot stop listening.
     */
    public void delistenToOnEnemyGroupDestroyed(OnEnemyGroupDestroyed onEnemyGroupDestroyed) {
        onEnemyGroupDestroyedEvent.remove(onEnemyGroupDestroyed);
    }

    @Override
    public void onEnemyDestroyed(Enemy enemy) {
        enemyDestroyed(enemy);
        if (areAllEnemiesDead()) {
            for (OnEnemyGroupDestroyed event : onEnemyGroupDestroyedEvent)
                event.onEnemyGroupDestroyed(this);
        }
    }
}
