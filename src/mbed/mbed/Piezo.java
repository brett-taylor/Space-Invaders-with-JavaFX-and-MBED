package mbed.mbed;

/**
 * @author kg246
 */
public interface Piezo extends MBedComponent {

    /**
     * The maximum frequency playable by the MBed's piezo electric speaker.
     */
    public static final double PIEZO_MAX_FREQUENCY = 12000.0;

    /**
     * The minimum frequency playable by the MBed's piezo electric speaker.
     */
    public static final double PIEZO_MIN_FREQUENCY = 20.0;

    /**
     * Play a specific musical note at full volume
     *
     * @param note The desired musical note to play
     */
    public void playNote(Note note);

    /**
     * Play a specified sound at a given volume
     *
     * @param volume    The desired volume of the piezo
     * @param frequency The desired frequency to play (bounded by the min and max frequencies)
     */
    public void playSound(double volume, double frequency);

    /**
     * Turn off the piezo
     */
    public void silence();

}
