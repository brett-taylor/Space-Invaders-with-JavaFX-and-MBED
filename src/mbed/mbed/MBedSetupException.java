package mbed.mbed;

/**
 * The exception wrapper for when issues arise during the MBed constructor.
 *
 * @author kg246
 */
public class MBedSetupException extends RuntimeException {

    /*package*/ MBedSetupException(String message, Throwable cause) {
        super(message, cause);
    }

    /*package*/ MBedSetupException(String message) {
        super(message);
    }

}
