package game;

import game.content.worlds.GameWorld;
import game.content.worlds.MBedIssueWorld;
import game.utils.Input;
import game.utils.ResourceLoader;
import game.utils.Settings;
import game.world.World;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Lowest level of the game. Handles input, updating objects and connection to mbed
 * @author Brett Taylor
 */
public class Engine {
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
        mainStage.setWidth(Settings.GAME.SCREEN_WIDTH);
        mainStage.setHeight(Settings.GAME.SCREEN_HEIGHT);
        mainStage.setResizable(false);
        mainStage.centerOnScreen();
        layoutContainer = new BorderPane();
        gamePane = new Pane();
        layoutContainer.setCenter(gamePane);
        mainStage.setScene(new Scene(layoutContainer));

        Input.setUpGlobalInputBindings();
        ResourceLoader.loadAllResources();

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
     * Returns the play area width of the game. This takes into consideration the mbed emulator.
     * @return the play area width of the game.
     */
    public static double getPlayAreaWidth() {
        return MBedEngine.shouldAttachEmulator() ?
                getMainStage().getWidth() - Settings.MBED.MBED_EMULATOR_WIDTH - Settings.GAME.RIGHT_SIDE_PADDING :
                getMainStage().getWidth() - Settings.GAME.RIGHT_SIDE_PADDING;
    }

    /**
     * Returns the play area height of the game.
     * @return the play area height of the game.
     */
    public static double getPlayAreaHeight() {
        return getMainStage().getHeight() - Settings.GAME.TITLE_BAR_HEIGHT;
    }

    /**
     * The current world.
     * @return the current world.
     */
    public static World getCurrentWorld() {
        return currentWorld;
    }
}
