package mbed.mbed;

/**
 * @author kg246
 */
/*package*/ abstract class AbstractMagnemometer extends AbstractComponent implements Magnemometer {

    public AbstractMagnemometer() {
        super(ComponentLocation.Board);
    }

    @Override
    public String getType() {
        return "Magnemometer";
    }

}
