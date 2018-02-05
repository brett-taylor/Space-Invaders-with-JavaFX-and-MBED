package mbed.mbed;

/**
 * An interface for referencing all the buttons on the MBed and MBed Application
 * Shield. Listeners may be attached.
 *
 * @author kg246 and djb
 */
public interface Button extends MBedComponent {

    /**
     * Adds a listener to the button.
     * <p>
     * The {@link ButtonListener#changed(boolean)} method will be called
     * with the current state of the button when it is pressed or released.
     * <p>
     * <p>
     * An example usage using Java 8 Lambda notation:
     * <pre> {@code
     * Button btn = mbed.getSwitch2();
     * btn.addListener((boolean isPressed) -> {
     *  //code to be excuted.
     * });
     * } </pre>
     *
     * @param listener The listener to be added.
     */
    public void addListener(ButtonListener listener);

    /**
     * Remove the given listener from the button.
     *
     * @param listener The listener to be removed.
     */
    public void removeListener(ButtonListener listener);

    /**
     * Returns a boolean representation if the button is pressed.
     *
     * @return True if the button is pressed, otherwise false.
     */
    public boolean isPressed();

    /**
     * A helper function that will wait until the button is pressed.
     */
    public void blockUntilPressed();

    /**
     * Remove all listeners attached to the Button.
     */
    public void removeAllListeners();

    /**
     * A simple descriptor useful in identifying components.
     *
     * @return a ComponentLocation that identifies the location of the Button
     */
    @Override
    public ComponentLocation getLocation();

}
