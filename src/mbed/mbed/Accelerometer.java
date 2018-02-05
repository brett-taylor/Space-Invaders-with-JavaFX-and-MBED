package mbed.mbed;

/**
 * An interface to represent an Accelerometers on the MBed or the application
 * shield. Accelerometers reading can be requested from objects implementing
 * this interface.
 *
 * @author kg246
 */
public interface Accelerometer extends MBedComponent {

    /**
     * Requests and returns a reading from the MBed object.
     *
     * @return An XYZData wrapper containing the X, Y and Z component readings.
     * Further processing can be performed on the resultant object.
     * @throws MBedStateException If a low level issue occurs whilst attempting
     *                            to communicate with the MBed board.
     */
    public XYZData getAcceleration();

    /**
     * A simple descriptor useful in identifying components. The accelerometer will
     * return {@link ComponentLocation#Board} or {@link ComponentLocation#Shield}.
     *
     * @return a ComponentLocation that identifies the location of the accelerometer
     */
    @Override
    public ComponentLocation getLocation();

}
