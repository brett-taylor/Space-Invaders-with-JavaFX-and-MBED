package game.events.handlers;

import game.Engine;
import game.content.mobs.Enemy;
import game.content.text.TextUtils;
import game.events.enemy.OnEnemyDestroyed;
import game.events.enemy.OnMultipleEnemyDestroyed;
import game.utils.Settings;

import javafx.geometry.Point2D;
import java.util.ArrayList;

/**
 * Intergration that is related to enemies.
 * E.g. When a enemy dies and a point burst occurs it is handled here.
 * Singleton Pattern
 * @author Brett Taylor
 */
public class EnemyRelatedTextIntergration implements OnEnemyDestroyed, OnMultipleEnemyDestroyed {
    /**
     * The singleton instance.
     */
    private static EnemyRelatedTextIntergration instance = null;

    /**
     * Creates the single version of text intergration.
     */
    private EnemyRelatedTextIntergration() {
    }

    /**
     * Will return the EnemyRelatedTextIntergration instance. If no instance is currently created one will be created.
     * @return
     */
    public static EnemyRelatedTextIntergration getInstance() {
        if (instance == null) {
            instance = new EnemyRelatedTextIntergration();
        }

        return instance;
    }

    @Override
    public void onEnemyDestroyed(Enemy enemy) {
        TextUtils.createPointsPopup(Engine.getCurrentWorld(), "+" + Settings.POINTS.POINTS_ON_ENEMY_DEATH, enemy.getPosition());
    }

    @Override
    public void onMultipleEnemyDestroyed(ArrayList<Enemy> enemies) {
        int killCount = enemies.size();
        String textString = "Double Kill!!!";
        switch (killCount) {
            case 2:
                textString = "Double Kill!!!";
                break;
            case 3:
                textString = "Triple Kill!!!";
                break;
            case 4:
                textString = "Quadruple Kill!!!";
                break;
            default:
                textString = "DOMINATION!!!";
                break;
        }

        Point2D medianPoint = Point2D.ZERO;
        for (Enemy enemy : enemies)
            medianPoint = medianPoint.add(enemy.getPosition());
        medianPoint = new Point2D(Math.abs(medianPoint.getX() / enemies.size()), Math.abs(medianPoint.getY() / enemies.size()));
        TextUtils.createPointsPopup(Engine.getCurrentWorld(), textString, medianPoint);
    }
}
