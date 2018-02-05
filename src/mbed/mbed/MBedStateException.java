package mbed.mbed;

/**
 * The exception wrapper for when issues arise during the MBed run time.
 *
 * @author kg246
 */
public class MBedStateException extends IllegalStateException {

    /*package*/ MBedStateException(String s) {
        super(s);
    }

    /*package*/ MBedStateException(String message, Throwable cause) {
        super(message, cause);
    }

    /*package*/ MBedStateException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
