package game;

import game.utils.input.Input;
import game.utils.input.MBedKeyCode;
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

        createEmulatorInGameFrame(layoutContainer);
        setupInputBindings();
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
        Engine.getMainStage().setWidth(Engine.getMainStage().getWidth() + Settings.MBED.MBED_EMULATOR_WIDTH);
    }

    /**
     * Returns whether or not the game frame should be showing the emulator
     * @return true if the gameframe wants the emulator in the screen.
     */
    public static boolean shouldAttachEmulator() {
        return isUsingEmulatorInGameFrame;
    }

    /**
     * Calls either onKeyDown (true) or onKeyReleased (false) in the Input class depending on isPressed
     * @param keyCode The MBed key code that was pressed.
     * @param isPressed True if that button is down now, false if that button is up now.
     */
    private static void decideKeyPress(MBedKeyCode keyCode, boolean isPressed) {
        if (isPressed)
            Input.onKeyDown(keyCode);
        else
            Input.onKeyReleased(keyCode);
    }

    /**
     * Sets up the mbed input bindings into the Input class.
     */
    private static void setupInputBindings() {
        mbed.getJoystickUp().addListener(isPressed -> decideKeyPress(MBedKeyCode.JOYSTICK_UP, isPressed));
        mbed.getJoystickRight().addListener(isPressed -> decideKeyPress(MBedKeyCode.JOYSTICK_RIGHT, isPressed));
        mbed.getJoystickDown().addListener(isPressed -> decideKeyPress(MBedKeyCode.JOYSTICK_DOWN, isPressed));
        mbed.getJoystickLeft().addListener(isPressed -> decideKeyPress(MBedKeyCode.JOYSTICK_LEFT, isPressed));
        mbed.getJoystickDown().addListener(isPressed -> decideKeyPress(MBedKeyCode.JOYSTICK_CENTER, isPressed));
        mbed.getSwitch2().addListener(isPressed -> decideKeyPress(MBedKeyCode.BUTTON_RIGHT, isPressed));
        mbed.getSwitch3().addListener(isPressed -> decideKeyPress(MBedKeyCode.BUTTON_LEFT, isPressed));
    }
}