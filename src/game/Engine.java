package game;

import game.utils.Input;
import game.world.World;
import game.world.impl.IntroductionWorld;
import game.world.impl.MBedIssueWorld;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mbed.mbed.MBed;
import mbed.mbed.MBedDummy;
import mbed.mbed.MBedUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Lowest level of the game. Handles input, updating objects and connection to mbed
 *
 * @author Brett Taylor
 */
public class Engine {
    public static boolean IS_DEBUG = false;

    private static boolean isInitialised = false;
    private static long lastUpdate = 0;
    private static Stage mainStage = null;
    private static World currentWorld = null;
    private static float deltaTime = 0.f;
    private static MBed mbed = null;

    /**
     * Handles the start up of the game.Engine
     *
     * @param stage The stage for the game.GameFrame the game will be using.
     */
    public static void initialise(Stage stage) {
        if (isInitialised)
            return;

        isInitialised = true;
        mainStage = stage;
        mainStage.setTitle("Asteroid Shooter");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainStage.setWidth((screenSize.width / 100) * 75);
        mainStage.setHeight((screenSize.height / 100) * 75);
        mainStage.centerOnScreen();

        mbed = MBedUtils.getMBed();
        if (mbed == null || !mbed.isOpen()) {
            setWorld(new MBedIssueWorld(getMainStage().getWidth(), getMainStage().getHeight()));
            mainStage.show();
            return;
        }

        // If we are using a dummy mbed lets incorporate the mbed emulator into our frame.
        /*if (mbed.getLocation() == "DUMMY") {
            MBedDummy dummy = (MBedDummy) mbed;
            mainStage.setWidth(230);
            mainStage.setHeight(375);

            final SwingNode swingNode = new SwingNode();
            SwingUtilities.invokeLater(() -> {
                swingNode.setContent(dummy.generatePane());
            });

            Pane pane = new Pane();
            pane.getChildren().add(swingNode); // Adding swing node
            stage.setScene(new Scene(pane, 100, 50));
            stage.show();

            dummy.getFrame().setState(Frame.ICONIFIED);
            mbed.getLCD().print(5, 5, "Helo World.");
            return;
        }*/

        setWorld(new IntroductionWorld(getMainStage().getWidth(), getMainStage().getHeight()));
        mainStage.show();
    }

    /**
     * Handles updating the game.
     *
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
     *
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

        currentWorld = world;
        mainStage.setScene(world.getScene());
    }

    /**
     * The current world.
     *
     * @return the current world.
     */
    public static World getCurrentWorld() {
        return currentWorld;
    }

    /**
     * Returns the last delta time calculated
     *
     * @return the last delta time calculated
     */
    public static float getDeltaTime() {
        return deltaTime;
    }
}
