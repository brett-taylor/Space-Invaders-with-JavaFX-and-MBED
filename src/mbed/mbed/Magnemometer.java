package mbed.mbed;

/**
 * @author kg246
 */
public interface Magnemometer extends MBedComponent {

    /**
     * Gets the current orientation of the MBed.
     *
     * @return Returns Up, Down, Left, Right or Unknown depending on which way
     * the MBed is tilted.
     * @throws MBedStateException If a low level issue occurs whilst attempting
     *                            to communicate with the MBed board.
     */
    public Orientation getOrientation();

    /**
     * Gets whether the MBed is the right way up (Front), up-side down (Back) or
     * Unknown if the MBed cannot determine.
     *
     * @return Front, Back or Unknown
     * @throws MBedStateException If a low level issue occurs whilst attempting
     *                            to communicate with the MBed board.
     */
    public Orientation getSide();

    /**
     * Estimates the current compass heading based on the Magnetometer data from
     * the MBed.
     *
     * @return A value between 0 and 360 degrees, where 0 (and 360) degrees
     * represent North.
     * @throws MBedStateException If a low level issue occurs whilst attempting
     *                            to communicate with the MBed board.
     */
    public double getHeading();

    /**
     * Requests and returns a reading from the MBed object.
     *
     * @return An XYZData wrapper containing the X, Y and Z component readings.
     * @throws MBedStateException If a low level issue occurs whilst attempting
     *                            to communicate with the MBed board.
     */
    public XYZData getMagnetism();
}
