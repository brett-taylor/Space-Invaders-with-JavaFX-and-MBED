package mbed.mbed;

/**
 * @author kg246
 */
/*package*/ abstract class AbstractLCD extends AbstractComponent implements LCD {

    public AbstractLCD() {
        super(ComponentLocation.Board);
    }

    @Override
    public String getType() {
        return "LCD";
    }

}
