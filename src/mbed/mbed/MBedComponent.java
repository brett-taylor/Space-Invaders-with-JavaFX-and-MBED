package mbed.mbed;

/**
 * @author kg246
 */
public interface MBedComponent {

    /**
     * Returns the parent MBed object of this component.
     *
     * @return The MBed object this component was obtained from.
     */
    public MBed getParent();

    /**
     * Returns a string that describes the type of this component.
     *
     * @return A string the provides a simple description of the component.
     */
    public String getType();

    /**
     * A simple descriptor useful in identifying components.
     *
     * @return a ComponentLocation that identifies the location of the component
     */
    public ComponentLocation getLocation();

}
