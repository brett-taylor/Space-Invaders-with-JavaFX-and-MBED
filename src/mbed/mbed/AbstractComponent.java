package mbed.mbed;

/**
 * @author kg246
 */
/*package*/ abstract class AbstractComponent implements MBedComponent {

    private final ComponentLocation location;

    public AbstractComponent(ComponentLocation location) {
        this.location = location;
    }

    @Override
    public ComponentLocation getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (getClass() != other.getClass()) return false;
        AbstractComponent comp = (AbstractComponent) other;
        return location.equals(comp.location) && getParent().equals(comp.getParent());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash += hash * 41 + getClass().hashCode();
        hash += hash * 41 + location.hashCode();
        hash += hash * 41 + getType().hashCode();
        hash += hash * 41 + getParent().hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return getType() + "{Location: " + location + '}';
    }

}
