package mbed.mbed;

/**
 * @author kg246
 */
/*package*/ abstract class AbstractAccelerometer extends AbstractComponent implements Accelerometer {

    public AbstractAccelerometer(ComponentLocation location) {
        super(location);
    }

    @Override
    public String getType() {
        return "Accelerometer";
    }

}
