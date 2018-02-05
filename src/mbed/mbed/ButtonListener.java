package mbed.mbed;

/**
 * An interface for listeners to button events.
 *
 * @author kg246
 */
@FunctionalInterface
public interface ButtonListener {

    /**
     * This method is called by any Button that is attached to when it is
     * pressed or released.
     *
     * @param isPressed The current pressed state of the button. This will be
     *                  true if the button has just been pressed, or false if it has been
     *                  released.
     */
    public void changed(boolean isPressed);

}
