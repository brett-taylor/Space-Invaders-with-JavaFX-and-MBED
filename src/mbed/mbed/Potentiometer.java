package mbed.mbed;

/**
 * Interface to Potentiometer functionality.
 *
 * @author djb
 */
public interface Potentiometer extends MBedComponent {
    /**
     * Return the value of the potentiometer.
     * <p>
     * <p>
     * This is normalised from 0.0 (fully turned counter-clockwise) and 1.0
     * (fully turned clockwise). In practise the value will never fully reach
     * 0.0 or 1.0, but will come close.
     * <p>
     *
     * @return The current value.
     */
    public double getValue();

    /**
     * Adds a listener to the potentiometer.
     * <p>
     * The
     * {@link PotentiometerListener#change(double) change} method will be called
     * with the new value of the potentiometer every time it changes. The value
     * will be in the range of 0.0 to 1.0.
     * <p>
     * The resolution of the update can be controlled with the {@code
     * #setEpsilon(double)} method.
     * <p>
     * <p>
     * An example usage:
     * <pre> {@code
     * Potentiometer pot = mbed.getPotentiometer1();
     * pot.addListener((double newValue) -> {
     *  //code to be excuted.
     * });
     * } </pre>
     *
     * @param listener The listener to be added.
     */
    public void addListener(PotentiometerListener listener);

    /**
     * Remove the given listener from the Potentiometer.
     *
     * @param listener The listener to be removed.
     */
    public void removeListener(PotentiometerListener listener);

    /**
     * Removes all listeners from the Potentiometer.
     */
    public void removeAllListeners();

    /**
     * Get the current epsilon value for change significance.
     *
     * @return The current epsilon.
     */
    public double getEpsilon();

    /**
     * Set the current epsilon value for change significance.
     * <p>
     * <p>
     * All the listeners attached to this object will be called when the
     * potentiometer varies by more than this value.
     * </p>
     *
     * @param epsilon The epsilon.
     */
    public void setEpsilon(double epsilon);

}
