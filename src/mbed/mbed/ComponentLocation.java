package mbed.mbed;

/**
 * @author kg246
 */
public enum ComponentLocation {

    /**
     * Enum value to identify the centre of the joystick
     */
    Centre,

    /**
     * Enum value to identify the up key of the joystick
     */
    Up,

    /**
     * Enum value to identify the component is on the main board as opposed to
     * the shield.
     */
    Board,

    /**
     * Enum value to identify the component is on the shield as opposed to
     * the main board.
     */
    Shield,

    /**
     * Enum value to identify the right key of the joystick, or the right switch
     * on the board.
     */
    Right,

    /**
     * Enum value to identify the left key of the joystick, or the left switch
     * on the board.
     */
    Left,

    /**
     * Enum value to identify the down key of the joystick
     */
    Down;

}
