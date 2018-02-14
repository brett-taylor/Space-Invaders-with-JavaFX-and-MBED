package game.AI;

import game.Engine;
import game.MBedEngine;
import game.content.events.enemy.OnAllEnemiesDestroyed;
import game.content.events.enemy.OnEnemyGroupDestroyed;
import game.content.events.mob.OnMobHealthChanged;
import game.content.mobs.Enemy;
import game.content.mobs.Mob;
import game.content.worlds.WinWorld;
import game.utils.Settings;
import game.world.World;

import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Handles controlling the ai on the world scene.
 * @author Brett Taylor
 */
public class AIController implements OnEnemyGroupDestroyed, OnAllEnemiesDestroyed, OnMobHealthChanged {
    /**
     * The world the AIController belongs to
     */
    private World world = null;

    /**
     * The enemy rows.
     */
    private EnemyGroupRow[] enemyRows = null;

    /**
     * The enemy columns
     */
    private EnemyGroupColumn[] enemyColumns = null;

    /**
     * The current left most enemy group.
     */
    private EnemyGroup leftMostGroup = null;

    /**
     * The current right most enemy group.
     */
    private EnemyGroup rightMostGroup = null;

    /**
     * The current bottom most enemy group.
     */
    private EnemyGroup bottomMostGroup = null;

    /**
     * The current horizontal direction that the enemies are moving
     */
    private HorizontalDirection movingHorizontal = HorizontalDirection.LEFT;

    /**
     * The current distance the enemies have left to travel downwards to reach the next row.
     */
    private float down_movement_amount_to_travel_per_row = 0.f;

    /**
     * Stores whether the enemies are on the last row or not.
     */
    private boolean lastRow = false;

    /**
     * Stores whether the enemies should be moving or not.
     */
    private boolean shouldEnemiesBeMoving = true;

    /**
     * Stores the extra speed that is created from there being less enemies.
     */
    private float movementMultiplier = 0.f;

    /**
     * The collection of objects listening to the OnALlEnemiesDestroyed event.
     */
    private ArrayList<OnAllEnemiesDestroyed> onAllEnemiesDestroyedEvent;

    /**
     * Creates a AI controller for the world.
     * @param world the world the ai controller will exist in
     */
    public AIController(World world) {
        this.world = world;
        enemyColumns = new EnemyGroupColumn[Settings.GAME_SCENE.AMOUNT_OF_ENEMIES_ACROSS_X_AXIS];
        enemyRows = new EnemyGroupRow[Settings.GAME_SCENE.AMOUNT_OF_ENEMIES_ACROSS_Y_AXIS];
        createEnemies();
        positionEnemies();
        calculateAndSetLeftMostGroup();
        calculateAndSetRightMostGroup();
        calculateAndSetBottomMostGroup();
        onAllEnemiesDestroyedEvent = new ArrayList<>();
        listenToOnAllEnemiesDestroyed(this);
    }

    /**
     * Creates all the enemies and assign them to groups
     */
    private void createEnemies() {
        Point2D startingPosition = getDefaultOffset();

        for (int i = 0; i < enemyRows.length; i++) {
            Point2D position = new Point2D(startingPosition.getX(),
                    startingPosition.getY() + (i * Settings.ENEMY.HEIGHT) + Settings.GAME_SCENE.ENEMY_SPACING
            );
            EnemyGroupRow row = new EnemyGroupRow(position);
            enemyRows[i] = row;
            row.listenToOnEnemyGroupDestroyed(this);
        }

        for (int i = 0; i < enemyColumns.length; i++) {
            Point2D position = new Point2D(
                    startingPosition.getX()+ (i * Settings.ENEMY.WIDTH) + Settings.GAME_SCENE.ENEMY_SPACING,
                    startingPosition.getY());
            EnemyGroupColumn column = new EnemyGroupColumn(position);
            enemyColumns[i] = column;
            column.listenToOnEnemyGroupDestroyed(this);
        }

        int columnNo = 0;
        int rowNo = 0;
        for (int i = 0; i < (Settings.GAME_SCENE.AMOUNT_OF_ENEMIES_ACROSS_X_AXIS * Settings.GAME_SCENE.AMOUNT_OF_ENEMIES_ACROSS_Y_AXIS); i++) {
            Enemy enemy = new Enemy();
            world.addGameObject(enemy);
            enemyColumns[columnNo].addEnemy(enemy);
            enemyRows[rowNo].addEnemy(enemy);
            enemy.listenToOnMobHealthChanged(this);

            columnNo++;
            if (columnNo == (Settings.GAME_SCENE.AMOUNT_OF_ENEMIES_ACROSS_X_AXIS)) {
                columnNo = 0;
                rowNo++;
            }
        }
    }

    /**
     * Will make each group reposition themselves.
     */
    private void positionEnemies() {
        for (EnemyGroup group : enemyColumns)
            group.positionEnemies();

        for (EnemyGroup group : enemyRows)
            group.positionEnemies();
    }

    /**
     * Works out the starting position of the topmost column and leftmost row
     * @return a point2D containing the starting position of the first enemy which will be in the
     * topmost column and leftmost row.
     */
    private Point2D getDefaultOffset() {
        double positionX = Engine.getPlayAreaWidth() / 2;
        int halfEnemiesInRow = Settings.GAME_SCENE.AMOUNT_OF_ENEMIES_ACROSS_X_AXIS / 2;
        positionX -= (halfEnemiesInRow * Settings.ENEMY.WIDTH) + (halfEnemiesInRow * Settings.GAME_SCENE.PADDING);
        return new Point2D(positionX, Settings.GAME_SCENE.ENEMY_STARTING_Y);
    }

    /**
     * Adds a offset to all groups.
     * @param newOffset the offset.
     */
    private void addOffsetToGroups(Point2D newOffset) {
        for (EnemyGroup group : enemyColumns)
            if (group != null)
                group.addOffset(newOffset);

        for (EnemyGroup group : enemyRows)
            if (group != null)
                group.addOffset(newOffset);
    }

    /**
     * Will update the AI manager.
     * @param deltaTime the delta time of the last frame
     */
    public void update(float deltaTime) {
        if (!shouldEnemiesBeMoving)
            return;

        float movementLeftRight = (Settings.ENEMY.MOVEMENT_SPEED * deltaTime) + (movementMultiplier * deltaTime);
        float movementDown = 0.f;
        if (movingHorizontal == HorizontalDirection.LEFT)
            movementLeftRight = -movementLeftRight;

        if (down_movement_amount_to_travel_per_row >= 0.f) {
            movementLeftRight = 0.f;
            movementDown = (Settings.ENEMY.MOVEMENT_SPEED * deltaTime) + (movementMultiplier * deltaTime);
            down_movement_amount_to_travel_per_row -= Math.abs(movementDown);
        } else if ((leftMostGroup.getPosition().getX() - movementLeftRight <= Settings.GAME_SCENE.PADDING && movingHorizontal == HorizontalDirection.LEFT)
                || (rightMostGroup.getPosition().getX() + movementLeftRight >= (Engine.getPlayAreaWidth() - (Settings.ENEMY.WIDTH * 4) - Settings.GAME_SCENE.PADDING) && movingHorizontal == HorizontalDirection.RIGHT)) {
            movementLeftRight = 0;
            movingHorizontal = movingHorizontal == HorizontalDirection.RIGHT ? HorizontalDirection.LEFT : HorizontalDirection.RIGHT;
            down_movement_amount_to_travel_per_row = Settings.GAME_SCENE.ENEMY_DROP_AMOUNT;
            if (bottomMostGroup.getPosition().getY() >= Settings.GAME_SCENE.ENEMY_WIN_HEIGHT) {
                if (lastRow) {
                    shouldEnemiesBeMoving = false;
                } else {
                    lastRow = true;
                }
            }
        }

        addOffsetToGroups(new Point2D(movementLeftRight, movementDown));

        for (EnemyGroupColumn group : enemyColumns) {
            if (group != null)
                group.updateShooting(deltaTime);
        }
    }

    /**
     * Works out the left most group that is still active
     */
    private void calculateAndSetLeftMostGroup() {
        for (int i = 0; i < enemyColumns.length; i++) {
            if (enemyColumns[i] != null) {
                leftMostGroup = enemyColumns[i];
                return;
            }
        }

        System.out.println("Warning: calculateAndSetLeftMostGroup worked out no group");
    }

    /**
     * Works out the right most group that is still active
     */
    private void calculateAndSetRightMostGroup() {
        for (int i = enemyColumns.length - 1; i >= 0; i--) {
            if (enemyColumns[i] != null) {
                rightMostGroup = enemyColumns[i];
                return;
            }
        }

        System.out.println("Warning: calculateAndSetRightMostGroup worked out no group");
    }

    /**
     * Works out top right most group that is still active
     */
    private void calculateAndSetBottomMostGroup() {
        for (int i = enemyRows.length - 1; i >= 0; i--) {
            if (enemyRows[i] != null) {
                bottomMostGroup = enemyRows[i];
                return;
            }
        }

        System.out.println("Warning: calculateAndSetBottomMostGroup worked out no group");
    }

    @Override
    public void onEnemyGroupDestroyed(EnemyGroup enemyGroup) {
        for (int i = enemyColumns.length - 1; i >= 0; i--) {
            if (enemyColumns[i] == enemyGroup) {
                enemyColumns[i] = null;
            }
        }

        for (int i = enemyRows.length - 1; i >= 0; i--) {
            if (enemyRows[i] == enemyGroup) {
                enemyRows[i] = null;
            }
        }

        if (enemyGroup == leftMostGroup) {
            calculateAndSetLeftMostGroup();
        } else if (enemyGroup == rightMostGroup) {
            calculateAndSetRightMostGroup();
        } else if (enemyGroup == bottomMostGroup) {
            calculateAndSetBottomMostGroup();
        }

        // Start checking for win condition. If all columns and rows are empty all enemies are destroyed.
        for (int i = enemyColumns.length - 1; i >= 0; i--) {
            if (enemyColumns[i] != null) {
                return;
            }
        }

        for (int i = enemyRows.length - 1; i >= 0; i--) {
            if (enemyRows[i] != null) {
                return;
            }
        }

        for (OnAllEnemiesDestroyed event : onAllEnemiesDestroyedEvent)
            event.onAllEnemiesDestroyed();
    }

    /**
     * Listens to the OnAllEnemiesDestroyed event.
     * @param onAllEnemiesDestroyed The listener
     */
    public void listenToOnAllEnemiesDestroyed(OnAllEnemiesDestroyed onAllEnemiesDestroyed) {
        onAllEnemiesDestroyedEvent.add(onAllEnemiesDestroyed);
    }

    /**
     * Stops listening to the OnAllEnemiesDestroyed event.
     * @param onAllEnemiesDestroyed The current listener ot stop listening.
     */
    public void delistenToOnAllEnemiesDestroyed(OnAllEnemiesDestroyed onAllEnemiesDestroyed) {
        onAllEnemiesDestroyedEvent.remove(onAllEnemiesDestroyed);
    }

    @Override
    public void onAllEnemiesDestroyed() {
        Engine.setWorld(new WinWorld());
    }

    @Override
    public void onMobHealthChanged(Mob mob, int oldHealth, int newHealth) {
        if (newHealth <= 0) {
            movementMultiplier += Settings.GAME_SCENE.ENEMY_SPEED_BOOST_PER_DEATH;
        }
    }
}
