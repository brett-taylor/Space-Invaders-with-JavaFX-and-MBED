package mbed.mbed;

/**
 * @author kg246
 */
public interface LED extends MBedComponent {

    /**
     * The last colour sent to the LED. Will return BLACK before the first call
     * to setColor
     *
     * @return The LEDColor enum value of the LED.
     */
    public LEDColor getColor();

    /**
     * Sets the LED to a specific colour.
     *
     * @param col The desired color to show
     */
    public void setColor(LEDColor col);

}
