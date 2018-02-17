package game.ai;

import game.utils.Settings;

import javafx.geometry.Point2D;

/**
 * Handles a enemy group arranged in a row.
 * @author Brett Taylor
 */
public class EnemyGroupRow extends EnemyGroup {
    /**
     * Creates a enemy group that will be arranged in a row.
     * * @param position the starting position of the enemy group.
     */
    public EnemyGroupRow(Point2D position) {
        super(Settings.GAME_SCENE.AMOUNT_OF_ENEMIES_ACROSS_X_AXIS, position);
    }

    @Override
    public void positionEnemies() {
        for (int i = 0; i < maxEnemies; i++) {
            if (enemies[i] != null) {
                enemies[i].setPosition(new Point2D(
                        position.getX() + (i * Settings.GAME_SCENE.PADDING) + (i * Settings.ENEMY.WIDTH),
                        enemies[i].getPosition().getY()));
            }
        }
    }
}
