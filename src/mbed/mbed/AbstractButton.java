package mbed.mbed;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author kg246
 */
/*package*/ abstract class AbstractButton extends AbstractComponent implements Button {

    private final Collection<ButtonListener> listeners;
    private int state;

    private static final int BUTTON_UNKNOWN = -1;
    private static final int BUTTON_RELEASED = 0;
    private static final int BUTTON_PRESSED = 1;


    /*package*/ AbstractButton(ComponentLocation location) {
        super(location);
        this.listeners = new ArrayList<>();
        state = BUTTON_UNKNOWN;
    }

    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    @Override
    public void addListener(ButtonListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove the given listener.
     *
     * @param listener The listener to be removed.
     */
    @Override
    public void removeListener(ButtonListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify the listeners of the button's state.
     *
     * @param isDown Whether the button is down or not.
     */
    /*package*/ void notifyListeners(boolean isDown) {
        if (state == (isDown ? BUTTON_PRESSED : BUTTON_RELEASED)) return; //debounce
        state = isDown ? BUTTON_PRESSED : BUTTON_RELEASED;
        for (ButtonListener listener : new ArrayList<>(listeners)) {
            listener.changed(isDown);
        }
    }

    /*package*/
    abstract boolean queryState();

    /**
     * Returns a boolean representation if the button is pressed
     *
     * @return True if the button is pressed, otherwise false.
     */
    @Override
    public boolean isPressed() {
        state = queryState() ? BUTTON_PRESSED : BUTTON_RELEASED;
        return state == BUTTON_PRESSED;
    }

    @Override
    public void blockUntilPressed() {
        Object lock = new Object();

        ButtonListener listener = isPressed -> {
            if (isPressed) return;
            synchronized (lock) {
                lock.notifyAll();
            }
        };

        synchronized (lock) {
            addListener(listener);
            try {
                lock.wait();
            } catch (InterruptedException ex) {
            }
            removeListener(listener);
        }
    }

    /**
     * Remove all listeners.
     */
    @Override
    public void removeAllListeners() {
        if (listeners != null) {
            listeners.clear();
        }
    }

}
