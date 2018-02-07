package game.utils;

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
    }

    /**
     * MBED
     */
    public class MBED {
        public static final int MBED_EMULATOR_WIDTH = 230;
    }

    /**
     * Colours
     */
    public static class COLORS {
        public static final Color SCENE_GOOD_BACKGROUND = new Color(0, 0, 0, 0.86);
        public static final Color SCENE_BAD_BACKGROUND = new Color(0.83, 0.47, 0.49, 0.79);
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
}
