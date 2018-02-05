package mbed.mbed;

/**
 * @author kg246
 */
public interface MBed extends AutoCloseable {

    /**
     * Returns the connection location of the MBed represented by this instance.
     * If the device was instantiated via {@link MBedUtils#connectToMBed(java.lang.String)}
     * then this will return the device identifier used. If the MBed object was
     * instantiated via {@link MBedUtils#searchForMBed()} or {@link MBedUtils#getMBed()}
     * then this is the address found for the current MBed device. The Simulation
     * address is always {@code "Dummy"}.
     *
     * @return The string address of the device.
     */
    public String getLocation();

    /**
     * Gets the left Potentiometer on the shield (labeled Pot1).
     *
     * @return An object representing the left potentiometer on the shield
     */
    public Potentiometer getPotentiometer1();

    /**
     * Gets the right Potentiometer on the shield (labeled Pot2).
     *
     * @return An object representing the right potentiometer on the shield
     */
    public Potentiometer getPotentiometer2();

    /**
     * Gets the Piezo electric speaker located on the shield.
     *
     * @return An object representing the piezo on the shield
     */
    public Piezo getPiezo();

    /**
     * Gets the thermometer located on the shield.
     *
     * @return An object representing the thermometer on the shield
     */
    public Thermometer getThermometer();

    /**
     * Gets the accelerometer located on the board.
     *
     * @return An object representing the accelerometer on the board
     */
    public Accelerometer getAccelerometerBoard();

    /**
     * Gets the accelerometer located on the shield.
     *
     * @return An object representing the accelerometer on the shield
     */
    public Accelerometer getAccelerometerShield();

    /**
     * Gets the magnemometer (compass) located on the shield.
     *
     * @return An object representing the magnemometer on the shield
     */
    public Magnemometer getMagnemometer();

    /**
     * Gets the LCD located on the shield.
     *
     * @return A object representing the LCD on the application shield
     */
    public LCD getLCD();

    /**
     * Gets the right button located on the board (labeld SW2).
     *
     * @return A Button object representing the button in bottom right of the
     * main board.
     */
    public Button getSwitch2();

    /**
     * Gets the left button located on the board (labeld SW3).
     *
     * @return A Button object representing the button in bottom left of the
     * main board.
     */
    public Button getSwitch3();

    /**
     * Gets the up button of the joystick located on the shield.
     *
     * @return A Button object representing moving the joystick up
     */
    public Button getJoystickUp();

    /**
     * Gets the down button of the joystick located on the shield.
     *
     * @return A Button object representing moving the joystick down
     */
    public Button getJoystickDown();

    /**
     * Gets the left button of the joystick located on the shield.
     *
     * @return A Button object representing moving the joystick to the left
     */
    public Button getJoystickLeft();

    /**
     * Gets the right button of the joystick located on the shield.
     *
     * @return A Button object representing moving the joystick to the right
     */
    public Button getJoystickRight();

    /**
     * Gets the fire button of the joystick located on the shield.
     *
     * @return A Button object representing pressing the joystick
     */
    public Button getJoystickFire();

    /**
     * Gets the RGB LED located on the board.
     *
     * @return A LED object representing the LED on the bottom left on the main
     * board
     */
    public LED getLEDBoard();

    /**
     * Gets the RGB LED located on the shield.
     *
     * @return A LED object representing the LED on the top right on the Shield
     */
    public LED getLEDShield();

    /**
     * Returns true if the connection to the MBed device is open and active for
     * communications.
     *
     * @return True if the communication channel to the mbed is still active.
     * False is an error has occured, the device has been manually disconnected
     * or {@link MBed#close() close} has been called.
     */
    public boolean isOpen();

    /**
     * Cleanly terminates the connection to the MBed device. Should be used on
     * program termination.
     *
     * @throws MBedStateException Exception on failure to cleanly close the connection
     */
    @Override
    public void close() throws MBedStateException;

}
