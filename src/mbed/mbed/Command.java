package mbed.mbed;

/**
 * The low level commands for the MBed protocol.
 *
 * @author kg246
 */
/*package*/ enum Command {

    //read commands
    READ_DIGITAL, READ_POT, READ_TEMP, READ_BOARD_ACCEL, READ_SHIELD_ACCEL, READ_BOARD_MAGNO,
    //set commands
    SET_SHIELD_LED, SET_BOARD_LED, SET_PIEZO,
    //higher level get commands
    GET_ORIENTATION, GET_SIDE, GET_HEADING,
    //lcd commands
    SET_LCD_POSITION, PRINT_TEXT, SET_PIXEL, DRAW_CIRCLE, FILL_CIRCLE, DRAW_LINE, DRAW_RECT, FILL_RECT, CLEAR_LCD,
    //other commands
    RECONNECT;

}
