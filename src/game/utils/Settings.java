package game.utils;

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
     * Sprites
     */
    public static class SPRITES {
        public static final String PLAYER_SPRITE_LOCATION = "resources/player.png";
        public static final String ENEMY_SPRITE_LOCATION = "resources/enemy.png";
    }

    /**
     * Player Settings
     */
    public static class PLAYER {
        public static final KeyCode LEFT_KEYBOARD = KeyCode.A;
        public static final MBedKeyCode LEFT_MBED = MBedKeyCode.JOYSTICK_LEFT;
        public static final KeyCode RIGHT_KEYBOARD = KeyCode.D;
        public static final MBedKeyCode RIGHT_MBED = MBedKeyCode.JOYSTICK_RIGHT;
        public static final KeyCode SHOOT_KEYBOARD = KeyCode.SPACE;
        public static final MBedKeyCode SHOOT_MBED = MBedKeyCode.JOYSTICK_CENTER;
        public static final double WIDTH = 40;
        public static final double HEIGHT = 20;
        public static final double MOVEMENT_SPEED = 200;
        public static final float SHOOTING_REFRACTORY_TIME = .5f;
    }

    /**
     * Enemy Settings
     */
    public static class ENEMY {
        public static final float WIDTH = 25;
        public static final float HEIGHT = 17;
        public static final float MOVEMENT_SPEED = 50.f;
        public static final float MAX_SHOOT_RANDOM_TIME = 5.f;
        public static final float MIN_SHOOT_RANDOM_TIME = 0.f;
        public static final float STARTING_SHOOT_COOLDOWN = 3.f;
    }

    /**
     * Missle Settings
     */
    public static class MISSLE {
        public static final float WIDTH = 2.f;
        public static final float HEIGHT = 20.f;
        public static final float FRIENDLY_MOVEMENT_SPEED = 600.f;
        public static final float ENEMY_MOVEMENT_SPEED = 400.f;
        public static final Color FRIENDLY_MISSLE = Color.GREENYELLOW;
        public static final Color ENEMY_MISSLE = Color.DEEPPINK;
    }

    /**
     * Wall Settings
     */
    public static class WALL {
        public static final float WIDTH = 50.f;
        public static final float HEIGHT = 5.f;
        public static final Color COLOR = Color.GREENYELLOW;
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
        public static final int AMOUNT_OF_ENEMIES_ACROSS_X_AXIS = 7;
        public static final int AMOUNT_OF_ENEMIES_ACROSS_Y_AXIS = 4;
        public static final int ENEMY_SPACING = 10;
        public static final int ENEMY_STARTING_Y = 30 + PADDING;
        public static final float ENEMY_DROP_AMOUNT = 10.f;
        public static final float ENEMY_SPEED_BOOST_PER_DEATH = 6.f;
        public static final float ENEMY_WIN_HEIGHT = 275.f;
    }
}
