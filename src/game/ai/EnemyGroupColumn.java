package game.ai;

import game.content.mobs.Enemy;
import game.content.mobs.Mob;
import game.utils.Settings;

import javafx.geometry.Point2D;

import java.util.Random;

/**
 * Handles a enemy group arranged in a column.
 * @author Brett Taylor
 */
public class EnemyGroupColumn extends EnemyGroup {
    /**
     * The random time till the enemy will shoot after it is able to shoot.
     * Stops all enemies shooting at the same time.
     */
    private float randomTime = 0.f;

    /**
     * The random generator.
     */
    private Random random;

    /**
     * Bottom most enemy
     */
    private Enemy bottomMostEnemy = null;

    /**
     * Creates a enemy group that will be arranged in a column
     * @param position the starting position of the enemy group.
     */
    public EnemyGroupColumn(Point2D position) {
        super(Settings.GAME_SCENE.AMOUNT_OF_ENEMIES_ACROSS_Y_AXIS, position);
        random = new Random();
        calculateAndSetRandomTime();
    }

    @Override
    public void positionEnemies() {
        for (int i = 0; i < maxEnemies; i++) {
            if (enemies[i] != null) {
                enemies[i].setPosition(new Point2D(
                        enemies[i].getPosition().getX(),
                        position.getY() + (i * Settings.GAME_SCENE.PADDING) + (i * Settings.ENEMY.HEIGHT)));
            }
        }
    }

    /**
     *
     * @param deltaTime
     */
    public void updateShooting(float deltaTime) {
        randomTime -= deltaTime;
        if (randomTime < 0.f) {
            bottomMostEnemy.shoot();
            calculateAndSetRandomTime();
        }
    }

    /**
     * Works out the enemy at the bottom (closest to the player) of the column.
     */
    private void calculateAndSetBottomMostEnemy() {
        for (int i = enemies.length - 1; i >= 0; i--) {
            if (enemies[i] != null) {
                bottomMostEnemy = enemies[i];
                return;
            }
        }
    }

    /**
     * Will calculate and set a time that is random between a range for the mob to be able to shoot
     */
    private void calculateAndSetRandomTime() {
        randomTime = random.nextFloat() * (Settings.ENEMY.MAX_SHOOT_RANDOM_TIME - Settings.ENEMY.MIN_SHOOT_RANDOM_TIME) + Settings.ENEMY.MAX_SHOOT_RANDOM_TIME;
    }

    @Override
    public void onEnemyDestroyed(Enemy enemy) {
        super.onEnemyDestroyed(enemy);

        if (enemy == bottomMostEnemy) {
            calculateAndSetBottomMostEnemy();
        }
    }

    @Override
    public void addEnemy(Enemy enemy) {
        super.addEnemy(enemy);
        enemy.setOneOffShootCooldown(Settings.ENEMY.STARTING_SHOOT_COOLDOWN);
        calculateAndSetBottomMostEnemy();
    }
}
