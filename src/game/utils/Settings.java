package game.utils;

import game.utils.input.MBedKeyCode;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Settings of the game
 * @author Brett Taylor
 */
public class Settings {
    /**
     * Game Settings
     */
    public class GAME {
        public static final String GAME_NAME = "Space Invaders";
        public static final double SCREEN_WIDTH = 700;
        public static final double SCREEN_HEIGHT = 500;
        public static final double TITLE_BAR_HEIGHT = 40;
        public static final double RIGHT_SIDE_PADDING = 17;
    }

    /**
     * MBED
     */
    public class MBED {
        public static final int MBED_EMULATOR_WIDTH = 207;
    }

    /**
     * Colours
     */
    public static class COLORS {
        public static final Color SCENE_GOOD_BACKGROUND = new Color(0, 0, 0, 0.86);
        public static final Color SCENE_BAD_BACKGROUND = new Color(0.83, 0.47, 0.49, 0.79);
        public static final Color SPACE_INVADER_GREEN = new Color(0.2, 0.949, 0.5765, 1);
    }

    /**
     * Player Settings
     */
    public static class PLAYERS {
        public static final KeyCode LEFT_KEYBOARD = KeyCode.A;
        public static final MBedKeyCode LEFT_MBED = MBedKeyCode.JOYSTICK_LEFT;
        public static final KeyCode RIGHT_KEYBOARD = KeyCode.D;
        public static final MBedKeyCode RIGHT_MBED = MBedKeyCode.JOYSTICK_RIGHT;
    }

    /**
     * Introduction scene
     */
    public class INTRODUCTION_SCENE {
        public static final float STARTING_TEXT_OPACITY = 0.f;
        public static final float ENDING_TEXT_OPACITY = 0.8f;
        public static final int FIRST_TEXT_INTRO = 3000;
        public static final int FIRST_TEXT_OUTRO = FIRST_TEXT_INTRO + 3000;
        public static final int SECOND_TEXT_INTRO = FIRST_TEXT_OUTRO + 3000;
        public static final int SECOND_TEXT_OUTRO = SECOND_TEXT_INTRO + 3000;
        public static final int TEXT_FADE_TIME = 1000;
        public static final String FIRST_TEXT = "space invaders";
        public static final String SECOND_TEXT = "a game by: brett taylor, toby james and jack rimmington.";
        public static final int TIME_TILL_SWAP_TO_NEXT_SCENE = SECOND_TEXT_OUTRO + 1000;
    }

    /**
     * Game Scene
     */
    public class GAME_SCENE {
        public static final int PADDING = 10;
        public static final int RECTNAGLE_THICKNESS = 5;
        public static final int RECTNAGLE_LENGTH = 20;
    }
}
