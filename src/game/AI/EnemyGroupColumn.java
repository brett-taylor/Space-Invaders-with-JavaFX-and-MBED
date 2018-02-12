package game.AI;

import game.utils.Settings;

import javafx.geometry.Point2D;

/**
 * Handles a enemy group arranged in a column.
 * @author Brett Taylor
 */
public class EnemyGroupColumn extends EnemyGroup {
    /**
     * Creates a enemy group that will be arranged in a column
     * @param position the starting position of the enemy group.
     */
    public EnemyGroupColumn(Point2D position) {
        super(Settings.GAME_SCENE.AMOUNT_OF_ENEMIES_ACROSS_Y_AXIS, position);
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
}
