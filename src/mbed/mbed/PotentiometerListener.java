package mbed.mbed;

/**
 * An interface for listeners to potentiometer events.
 *
 * @author djb
 */
@FunctionalInterface
public interface PotentiometerListener {

    /**
     * This method is called by and Potentiometer that is attached to when the
     * current value of the
     *
     * @param value The new value of the Potentiometer. This will be in the
     *              range 0.0 to 1.0.
     */
    public void change(double value);
}
