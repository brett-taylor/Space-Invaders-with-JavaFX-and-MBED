package game.utils;

import game.Engine;
import game.world.World;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;

/**
 * Handles general input among the game.
 * @author Brett Taylor
 */
public class Input {
    private static HashSet<KeyCode> keysDown = new HashSet<>();
    private static HashSet<KeyCode> keysJustPressed = new HashSet<>();

    public static void setUpGlobalInputBindings() {
        Engine.getMainStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> onKeyDown(event));
        Engine.getMainStage().getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> onKeyReleased(event));
    }

    /**
     * Called when a key is pressed
     * @param event The key that was pressed
     */
    public static void onKeyDown(KeyEvent event) {
        if (!keysDown.contains(event.getCode())) {
            keysJustPressed.add(event.getCode());
            keysDown.add(event.getCode());
        }

        World currentWorld = Engine.getCurrentWorld();
        if (currentWorld != null)
            currentWorld.onKeyDown(event);
    }

    /**
     * Called when a key is released
     * @param event The key that was released
     */
    public static void onKeyReleased(KeyEvent event) {
        keysDown.remove(event.getCode());

        World currentWorld = Engine.getCurrentWorld();
        if (currentWorld != null)
            currentWorld.onKeyReleased(event);
    }

    /**
     * Handles updating the input class every frame.
     * @param deltaTime the delta time of the frame
     */
    public static void update(float deltaTime) {
        keysJustPressed.clear();
    }

    /**
     * Returns true if the key is down
     * @param keyCode The key that will be checked if its pressed
     * @return true if the key is down
     */
    public static boolean isKeyDown(KeyCode keyCode) {
        return keysDown.contains(keyCode);
    }

    /**
     * Returns true if the key was pressed this frame.
     * @param keyCode The key that will be checked if it was pressed this frame.
     * @return true if the key was just pressed this frame.
     */
    public static boolean isKeyPressed(KeyCode keyCode) {
        return keysJustPressed.contains(keyCode);
    }
}
