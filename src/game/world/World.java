package game.world;

import game.entities.Entity;
import game.entities.IDrawable;
import game.entities.IUpdatable;
import game.utils.Input;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A World is a Level.
 *
 * @author Brett Taylor
 */
public abstract class World implements IDrawable {
    public Pane root;
    protected Canvas canvas;
    protected Scene scene;

    private ArrayList<IUpdatable> updatables;
    private ArrayList<IDrawable> drawables;

    /**
     * Creates a World
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the
     */
    public World(double screenWidth, double screenHeight) {
        root = new Pane();
        canvas = new Canvas(screenWidth, screenHeight);
        root.getChildren().add(canvas);
        scene = new Scene(root, screenWidth, screenHeight);

        updatables = new ArrayList<>();
        drawables = new ArrayList<>();
        setInputBindings();
    }

    /**
     * Correctly sets up a game.world to no longer be used by the game.GameFrame and removed successfully.
     */
    public void destroy() {
    }

    /**
     * Updates the object
     * If overwritten remember to call super.update();
     *
     * @param deltaTime The delta time of the current frame.
     */
    public void update(float deltaTime) {
        Iterator<IUpdatable> iterator = updatables.iterator();
        while (iterator.hasNext())
            iterator.next().update(deltaTime);
    }

    @Override
    public void render(GraphicsContext graphics, double screenWidth, double screenHeight) {
    }

    /**
     * Renders the drawables
     */
    public void startRender() {
        root.getChildren().removeAll(canvas);
        canvas = new Canvas(root.getWidth(), root.getHeight());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        double width = canvas.getWidth(), height = canvas.getHeight();
        render(graphicsContext, width, height);
        Iterator<IDrawable> iterator = drawables.iterator();
        while (iterator.hasNext())
            iterator.next().render(graphicsContext, width, height);

        root.getChildren().add(canvas);
    }

    /**
     * Called when a key is pressed
     *
     * @param event The key that was pressed
     */
    protected void onKeyDown(KeyEvent event) {
        Input.onKeyDown(event);
    }

    /**
     * Called when a key is released
     *
     * @param event The key that was released
     */
    protected void onKeyReleased(KeyEvent event) {
        Input.onKeyReleased(event);
    }

    /**
     * Called when a mouse button is clicked
     *
     * @param event The mouse clicked event
     */
    protected void onMouseClicked(MouseEvent event) {

    }

    /**
     * Sets up the input binding for the scene.
     */
    private void setInputBindings() {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> onKeyDown(event));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> onKeyReleased(event));
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMouseClicked(event));
    }

    /**
     * Gets The level's scene
     *
     * @return the level's scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Adds a updatable object to the world.
     *
     * @param updatable the object to be added to the world
     */
    public void addUpdatable(IUpdatable updatable) {
        updatable.onStart();
        updatables.add(updatable);
    }

    /**
     * Removes an updatable object from the world.
     *
     * @param updatable the object to be removed
     */
    private void removeUpdatable(IUpdatable updatable, Iterator<IUpdatable> iterator) {
        updatable.onEnd();
        if (iterator != null)
            iterator.remove();
        else
            updatables.remove(updatable);
    }

    /**
     * Adds a drawable object to the world.
     *
     * @param drawable the object to be drawn to the world
     */
    public void addDrawable(IDrawable drawable) {
        drawables.add(drawable);
    }

    /**
     * Removes an drawable from the world.
     *
     * @param drawable the drawable to be removed
     */
    private void removeDrawable(IDrawable drawable, Iterator<IDrawable> iterator) {
        if (iterator != null)
            iterator.remove();
        else
            drawables.remove(drawable);
    }

    /**
     * Adds a entity to the world.
     *
     * @param entity The entity that will be added to the world.
     */
    public void addEntity(Entity entity) {
        addUpdatable(entity);
        addDrawable(entity);
    }
}
