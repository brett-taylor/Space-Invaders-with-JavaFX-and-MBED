package mbed.mbed;

/**
 * @author kg246
 */
/*package*/ abstract class AbstractPiezo extends AbstractComponent implements Piezo {

    public AbstractPiezo() {
        super(ComponentLocation.Shield);
    }

    @Override
    public String getType() {
        return "Piezo";
    }

}
