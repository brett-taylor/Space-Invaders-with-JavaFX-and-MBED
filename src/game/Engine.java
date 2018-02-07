package game;

import game.utils.Input;
import game.utils.Settings;
import game.world.World;
import game.world.impl.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Lowest level of the game. Handles input, updating objects and connection to mbed
 * @author Brett Taylor
 */
public class Engine {
    private static boolean isDebug = false;
    private static boolean isInitialised = false;
    private static long lastUpdate = 0;
    private static Stage mainStage = null;
    private static World currentWorld = null;
    private static float deltaTime = 0.f;
    private static BorderPane layoutContainer = null;
    private static Pane gamePane = null;

    /**
     * Handles the start up of the game.Engine*
     * @param stage The stage for the game.GameFrame the game will be using.
     */
    public static void initialise(Stage stage) {
        if (isInitialised)
            return;

        isInitialised = true;
        mainStage = stage;
        mainStage.setTitle(Settings.GAME.GAME_NAME);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainStage.setWidth((screenSize.width / 100) * 75);
        mainStage.setHeight((screenSize.height / 100) * 75);
        mainStage.centerOnScreen();

        layoutContainer = new BorderPane();
        gamePane = new Pane();
        layoutContainer.setCenter(gamePane);
        mainStage.setScene(new Scene(layoutContainer));

        Input.setUpGlobalInputBindings();

        // If returned false mbed didnt start up correctly and needs to show MbedIssueWorld
        if (!MBedEngine.startUp(layoutContainer)) {
            Engine.setWorld(new MBedIssueWorld());
            mainStage.show();
            return;
        }

        setWorld(new GameWorld());
        mainStage.show();
    }

    /**
     * Handles updating the game.
     * @param now The time currently now in nanoseconds.
     */
    public static void update(long now) {
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        deltaTime = (now - lastUpdate) / 1e9f;
        if (currentWorld != null) {
            currentWorld.update(deltaTime);
            currentWorld.startRender();
        }
        Input.update(deltaTime);
        lastUpdate = now;
    }

    /**
     * Returns the main stage of the game frame.
     * @return the main stage of the game frame.
     */
    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * Sets the current World.
     */
    public static void setWorld(World world) {
        if (currentWorld != null)
            currentWorld.destroy();

        gamePane.getChildren().clear();
        gamePane.getChildren().add(world.getRoot());
        currentWorld = world;
    }

    /**
     * The current world.
     * @return the current world.
     */
    public static World getCurrentWorld() {
        return currentWorld;
    }

    /**
     * Returns the last delta time calculated
     * @return the last delta time calculated
     */
    public static float getDeltaTime() {
        return deltaTime;
    }

    /**
     * Returns the play area width of the game. This takes into consideration the mbed emulator.
     * @return the play area width of the game.
     */
    public static double getPlayAreaWidth() {
        return MBedEngine.shouldAttachEmulator() ?
                getMainStage().getWidth() - Settings.MBED.MBED_EMULATOR_WIDTH :
                getMainStage().getWidth();
    }

    /**
     * Returns the play area height of the game.
     * @return the play area height of the game.
     */
    public static double getPlayAreaHeight() {
        return getMainStage().getHeight();
    }

    /**
     * Sets whether the game is in debug mode or not.
     * @param debugStatus true or false for debug mode.
     */
    public static void setDebug(boolean debugStatus) {
        isDebug = debugStatus;
    }

    /**
     * Returns whether the game is in debug or not.
     * @return true if the game is in debug mode.
     */
    public static boolean isDebug() {
        return isDebug;
    }
}
