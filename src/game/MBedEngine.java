package game;

import game.utils.Settings;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import mbed.mbed.MBed;
import mbed.mbed.MBedDummy;
import mbed.mbed.MBedUtils;

/**
 * Handles setting up the MBed and creating the ability for it to be attached to the game frame.
 * @author Brett Taylor
 */
public class MBedEngine {
    private static MBed mbed = null;
    private static Pane emulatorPane = null;
    private static boolean isUsingEmulatorInGameFrame = false;

    public static boolean startUp(BorderPane layoutContainer) {
        mbed = MBedUtils.getMBed();
        if (mbed == null || !mbed.isOpen()) {
            return false;
        }

        mbed.getLCD().print(5, 5, "Connected.");
        createEmulatorInGameFrame(layoutContainer);
        return true;
    }

    /**
     * If requested will take the emulator and put it into a pane that can be used in the game frame.
     */
    private static void createEmulatorInGameFrame(BorderPane layoutContainer) {
        MBedDummy dummy = (MBedDummy) mbed;
        final SwingNode swingNode = new SwingNode();
        swingNode.setContent(dummy.generatePane());
        emulatorPane = new Pane();
        emulatorPane.getChildren().add(swingNode); // Adding swing node
        emulatorPane.setPrefWidth(Settings.MBED.MBED_EMULATOR_WIDTH);
        isUsingEmulatorInGameFrame = true;
        layoutContainer.setRight(emulatorPane);
    }

    /**
     * Returns whether or not the game frame should be showing the emulator
     * @return true if the gameframe wants the emulator in the screen.
     */
    public static boolean shouldAttachEmulator() {
        return isUsingEmulatorInGameFrame;
    }
}
