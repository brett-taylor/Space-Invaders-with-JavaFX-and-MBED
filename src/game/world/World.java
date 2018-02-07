package game.world;

import game.Engine;
import game.entities.Entity;
import game.entities.IDrawable;
import game.entities.IUpdatable;
import game.utils.input.MBedKeyCode;
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
public abstract class World implements IDrawable {
    protected Pane root;
    protected Canvas canvas;
    protected double screenWidth;
    protected double screenHeight;

    private ArrayList<IUpdatable> updatables;
    private ArrayList<IDrawable> drawables;

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
        for (Iterator<IUpdatable> iterator = updatables.iterator(); iterator.hasNext();)
            iterator.next().update(deltaTime);
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
    }

    /**
     * Renders the drawables
     */
    public void startRender() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, screenWidth, screenHeight);
        render(graphicsContext, screenWidth, screenHeight);
        for (Iterator<IDrawable> iterator = drawables.iterator(); iterator.hasNext();)
            iterator.next().render(graphicsContext, screenWidth, screenHeight);
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
     * Adds a updatable object to the world.
     * @param updatable the object to be added to the world
     */
    public void addUpdatable(IUpdatable updatable) {
        updatable.onStart();
        updatables.add(updatable);
    }

    /**
     * Adds a drawable object to the world.
     * @param drawable the object to be drawn to the world
     */
    public void addDrawable(IDrawable drawable) {
        drawables.add(drawable);
    }

    /**
     * Adds a entity to the world.
     * @param entity The entity that will be added to the world.
     */
    public void addEntity(Entity entity) {
        addUpdatable(entity);
        addDrawable(entity);
    }
}
