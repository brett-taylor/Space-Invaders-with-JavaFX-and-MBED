package mbed.mbed;

/**
 * An immutable data container of Accelerometer or Magnemometer (Compass) data
 * as read from the MBed. This is stored as 3D vector with dimensions X, Y and Z
 * with arbitrary units.
 *
 * @author kg246`
 */
public class XYZData {

    private final double x, y, z;

    /*package*/ XYZData(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the amount of acceleration in the X direction measure by the
     * MBed.
     *
     * @return Returns the amount of acceleration in the X direction measure by
     * the MBed.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the amount of acceleration in the Y direction measure by the
     * MBed.
     *
     * @return Returns the amount of acceleration in the Y direction measure by
     * the MBed.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the amount of acceleration in the Z direction measure by the
     * MBed.
     *
     * @return Returns the amount of acceleration in the Z direction measure by
     * the MBed.
     */
    public double getZ() {
        return z;
    }

    /**
     * Returns the size of the acceleration.
     *
     * @return The magnitude of acceleration.
     */
    public double getMagnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Returns a normalised unit vector of the data. Useful for advanced math.
     *
     * @return A new instance containing a normalised representation of the data.
     */
    public XYZData getNormalised() {
        double m = getMagnitude();
        if (m == 0.0) return this;
        return new XYZData(x / m, y / m, z / m);
    }

    /**
     * Returns a scaled version of the data. Useful for advanced math.
     *
     * @param scalar The amount to multiply by
     * @return A new instance contained a scaled version of the data.
     */
    public XYZData getScaled(double scalar) {
        return new XYZData(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Return a representation of the x, y, z values.
     *
     * @return A string containing the x, y and z values.
     */
    @Override
    public String toString() {
        return String.format("XYZData{x=%.4f, y=%.4f, z=%.4f}", x, y, z);
    }

}
