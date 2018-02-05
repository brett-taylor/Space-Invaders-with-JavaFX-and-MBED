package mbed.mbed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author kg246
 */
public class MBedUtils {

    private static final MBedDummy SINGLETON_DUMMY = new MBedDummy(new Random());

    /**
     * Constructs a new MBed interfacing object. It will perform an automatic
     * search and attempt to find an MBEd FRDM K64F board listening on a device
     * port.
     *
     * @return An instance to communicate with MBed device.
     * @throws MBedSetupException If the MBed failed to connect.
     */
    public static MBedDevice searchForMBed() throws MBedSetupException {
        try {
            String[] devices = MBedUtils.findMBedDevices();
            if (devices == null || devices.length == 0)
                throw new MBedSetupException("Could not locate any MBed device");
            for (String device : devices) {
                try {
                    return new MBedDevice(device);
                } catch (MBedStateException | MBedSetupException ex) {
                    //do nothing, try next port
                }
            }
        } catch (IOException ex) {
            throw new MBedSetupException("Could not located MBed device due to IO Error", ex);
        }
        throw new MBedSetupException("Could not located MBed device");
    }

    /**
     * Attempts to connect to an MBed device
     *
     * @param deviceIdentifier The address/identify to the device "/dev/ttyXXX"
     *                         on Linux and Mac, "COM##" on Windows.
     * @return A instance used to communicate with a physical MBed FRDM K64F
     * device.
     * @throws MBedSetupException If the device could not be found at the
     *                            specified location.
     */
    public static MBedDevice connectToMBed(String deviceIdentifier) throws MBedSetupException {
        return new MBedDevice(deviceIdentifier);
    }

    /**
     * Returns a dummy MBed object for testing.
     *
     * @return A dummy MBed object
     */
    public static MBedDummy getDummyMBed() {
        if (!SINGLETON_DUMMY.isOpen())
            SINGLETON_DUMMY.open();
        return SINGLETON_DUMMY;
    }

    /**
     * This method will attempt to search for a physical MBed device running
     * compatiable software. In the event that it is not found, it will return
     * a dummy object for testing.
     *
     * @return Either a physical MBed wrapper or a dummy object.
     */
    public static MBed getMBed() {
        try {
            return searchForMBed();
        } catch (MBedSetupException | MBedStateException mse) {
            return getDummyMBed();
        }
    }

    /**
     * True if the identified OS is Windows based.
     */
    public static final boolean IS_WINDOWS;

    /**
     * True if the identified OS is Mac based.
     */
    public static final boolean IS_MAC;

    /**
     * True if the identified OS is Unix or Linux based.
     */
    public static final boolean IS_NIX;

    static {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            IS_WINDOWS = true;
            IS_MAC = false;
            IS_NIX = false;
        } else if (OS.contains("mac")) {
            IS_WINDOWS = false;
            IS_MAC = true;
            IS_NIX = false;
        } else if (OS.contains("nux") || OS.contains("nix")) {
            IS_WINDOWS = false;
            IS_MAC = false;
            IS_NIX = true;
        } else { // unknown OS
            IS_WINDOWS = false;
            IS_MAC = false;
            IS_NIX = false;
        }
    }

    public static String[] findMBedDevices() throws IOException {
        ArrayList<String> devices = new ArrayList<>();
        if (IS_WINDOWS) {
            BufferedReader str = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("wmic path Win32_PnPEntity Where \"Caption LIKE '%mbed Serial%'\" Get Caption").getInputStream()));
            String line;
            while ((line = str.readLine()) != null) {
                int i = line.indexOf("COM");
                if (i >= 0) {
                    String com = line.substring(i, line.indexOf(')', i));
                    devices.add(com);
                }
            }
        } else if (IS_MAC || IS_NIX) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("/dev"), IS_MAC ? "tty.usbmodem*" : "ttyACM*")) {
                for (Path file : stream) {
                    devices.add(file.toFile().getAbsolutePath());
                }
            }
        } else {
            throw new IllegalStateException("Unsupported OS");
        }
        return devices.toArray(new String[devices.size()]);
    }

    /**
     * An ease-of-use method to automatically set a button to terminate the
     * application.
     *
     * @param button The ID of the button to cause a shutdown.
     */
    public static void terminateOnPress(Button button) {
        button.addListener(pressed -> {
            if (pressed) {
                button.getParent().close();
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException ex) {
                }
                System.exit(0);
            }
        });
    }

    /**
     * A useful helper function for putting the main thread to sleep, not
     * returning from this method call until "woken up" again by an asynchronous
     * call to either the {@link MBedUtils#wakeThread(MBed)} or
     * {@link MBedUtils#wakeThread(MBedComponent)} methods.
     * <p>
     * This method will not return until it is woken by the wake method or the
     * slept thread is interrupted.
     *
     * @param mbed MBed object to use as sync lock
     */
    public static void sleepThread(MBed mbed) {
        synchronized (mbed) {
            try {
                mbed.wait();
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * A useful helper function for putting the main thread to sleep, not
     * returning from this method call until "woken up" again by an asynchronous
     * call to either the {@link MBedUtils#wakeThread(MBed)} or
     * {@link MBedUtils#wakeThread(MBedComponent)} methods.
     * <p>
     * This method will not return until it is woken by the wake method or the
     * slept thread is interrupted.
     *
     * @param comp MBed component object to use as sync lock
     */
    public static void sleepThread(MBedComponent comp) {
        sleepThread(comp.getParent());
    }

    /**
     * Wakes up all threads slept by the {@link MBedUtils#sleepThread(MBed)}
     * or {@link MBedUtils#sleepThread(MBedComponent)} methods.
     *
     * @param mbed MBed lock to wake from
     */
    public static void wakeThread(MBed mbed) {
        synchronized (mbed) {
            mbed.notifyAll();
        }
    }


    /**
     * Wakes up all threads slept by the {@link MBedUtils#sleepThread(MBed)}
     * or {@link MBedUtils#sleepThread(MBedComponent)} methods.
     *
     * @param comp MBed component lock to wake from
     */
    public static void wakeThread(MBedComponent comp) {
        wakeThread(comp.getParent());
    }

    /**
     * Sets the specified button to call the {@link MBedUtils#wakeThread(MBed)}
     * or {@link MBedUtils#wakeThread(MBedComponent)} methods.
     *
     * @param button The ID of the button to cause a wake.
     */
    public void wakeOnPress(final Button button) {
        button.addListener(pressed -> {
            if (pressed)
                wakeThread(button);
        });
    }

    private MBedUtils() {
    }

}
