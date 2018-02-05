package mbed.mbed;

/**
 * @author kg246
 */
/*package*/ abstract class AbstractThermometer extends AbstractComponent implements Thermometer {

    public AbstractThermometer() {
        super(ComponentLocation.Shield);
    }

    @Override
    public String getType() {
        return "Thermometer";
    }

}
