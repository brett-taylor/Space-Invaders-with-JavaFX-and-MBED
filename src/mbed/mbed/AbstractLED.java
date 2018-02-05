package mbed.mbed;

/**
 * @author kg246
 */
/*package*/ abstract class AbstractLED extends AbstractComponent implements LED {

    private LEDColor col;

    public AbstractLED(ComponentLocation location) {
        super(location);
        col = LEDColor.BLACK;
    }

    @Override
    public String getType() {
        return "LED";
    }

    @Override
    public LEDColor getColor() {
        return col;
    }

    @Override
    public void setColor(LEDColor col) {
        if (col == null)
            throw new NullPointerException("Cannot set LED to color: NULL");
        if (col == this.col) return;
        this.col = col;
        applyColor(col);
    }

    protected abstract void applyColor(LEDColor col);

}
