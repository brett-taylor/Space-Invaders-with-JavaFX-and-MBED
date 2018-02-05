package mbed.mbed;

/**
 * Interface for Thermometer device wrappers.
 *
 * @author kg246
 */
public interface Thermometer extends MBedComponent {

    /**
     * Requests and returns the current temperature from the MBed device.
     *
     * @return The current temperature in Celcius.
     */
    public double getTemperature();

}
