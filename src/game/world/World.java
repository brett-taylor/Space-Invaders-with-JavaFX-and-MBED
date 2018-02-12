package game.world;

import game.Engine;
import game.utils.MBedKeyCode;
import game.world.objects.GameObject;
import game.world.objects.ICollidable;
import game.world.objects.IDrawable;
import game.world.objects.IUpdatable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A World is a Level.
 * @author Brett Taylor
 */
public abstract class World {
    protected Pane root;
    protected Canvas canvas;
    protected double screenWidth;
    protected double screenHeight;
    private ArrayList<IUpdatable> updatables;
    private ArrayList<IDrawable> drawables;
    private ArrayList<ICollidable> collidables;

    /**
     * Creates a World
     */
    public World() {
        screenWidth = Engine.getPlayAreaWidth();
        screenHeight = Engine.getPlayAreaHeight();

        root = new Pane();
        canvas = new Canvas(screenWidth, screenHeight);
        root.getChildren().add(canvas);
        root.setPrefWidth(screenWidth);
        root.setPrefHeight(screenHeight);

        updatables = new ArrayList<>();
        drawables = new ArrayList<>();
        collidables = new ArrayList<>();
    }

    /**
     * Correctly sets up a world to no longer be used by the game.GameFrame and removed successfully.
     */
    public void destroy() {
    }

    /**
     * Updates the object
     * If overwritten remember to call super.update();
     * @param deltaTime The delta time of the current frame.
     */
    public void update(float deltaTime) {
        screenWidth = Engine.getPlayAreaWidth();
        screenHeight = Engine.getPlayAreaHeight();

        for (IUpdatable updatable : new ArrayList<>(updatables)) {
            if (updatable.isBeingDestroyed()) {
                updatable.onEnd();
                updatables.remove(updatable);
            }
            else {
                if (!updatable.hasOnStartBeenCalled())
                    updatable.onStart();

                updatable.update(deltaTime);
            }
        }
    }

    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
    }

    /**
     * Renders the drawables
     */
    public void startRender() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, screenWidth, screenHeight);
        render(graphicsContext, screenWidth, screenHeight);
        for (IDrawable drawable : new ArrayList<>(drawables)) {
            if (drawable.isBeingDestroyed()) {
                drawables.remove(drawable);
            }
            else {
                drawable.render(graphicsContext, screenWidth, screenHeight);
            }
        }
    }

    /**
     * Called when a key is pressed
     * @param event The key that was pressed
     */
    public void onKeyDown(KeyEvent event) {
    }

    /**
     * Called when a key is released
     * @param event The key that was released
     */
    public void onKeyReleased(KeyEvent event) {
    }

    /**
     * Called when a key on the mbed is pressed
     * @param mBedKeyCode The mbed key that was pressed
     */
    public void onKeyDown(MBedKeyCode mBedKeyCode) {
    }

    /**
     * Called when a key on the mbed is released
     * @param mBedKeyCode The mbedkey that was released
     */
    public void onKeyReleased(MBedKeyCode mBedKeyCode) {
    }

    /**
     * Gets The level's root component
     * @return the level's root component
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * Adds a GameObject to the world.
     * @param go The GameObject that will be added to the world.
     */
    public void addGameObject(GameObject go) {
        updatables.add(go);
        drawables.add(go);
    }

    /**
     * Adds a collidable to the world.
     * @param collidable the collidable to be added to the world.
     */
    public void addCollidable(ICollidable collidable) {
        collidables.add(collidable);
    }

    /**
     * Removes a ICollidable that is currently in the world. (No checks for this)
     * @param collidable the ICollidable to remove.
     */
    public void removeCollidable(ICollidable collidable) {
        collidables.remove(collidable);
    }

    /**
     * Gets all the collidables in the world.
     * @return a collection containing all the collidables in the current wolr.d
     */
    public ArrayList<ICollidable> getCollidables() {
        return collidables;
    }
}
