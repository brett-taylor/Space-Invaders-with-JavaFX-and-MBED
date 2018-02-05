package mbed.mbed;

import java.util.*;
import java.util.concurrent.ForkJoinPool;

/**
 * @author kg246
 */
/*package*/ abstract class AbstractPotentiometer extends AbstractComponent implements Potentiometer {

    // Listener sleep period.
    private static final long THREAD_DELAY = 100L;
    // Default epsilon for changes.
    private static final double DEFAULT_EPSILON = 1e-4;

    private static final Set<AbstractPotentiometer> POTS_TO_MONITOR = new HashSet<>(2);
    @SuppressWarnings("SleepWhileInLoop")
    private static final Thread MONITOR_THREAD = new Thread(() -> {
        while (true) {
            synchronized (POTS_TO_MONITOR) {
                if (!POTS_TO_MONITOR.isEmpty()) {
                    for (Iterator<AbstractPotentiometer> it = POTS_TO_MONITOR.iterator(); it.hasNext(); ) {
                        AbstractPotentiometer pot = it.next();
                        if (!pot.getParent().isOpen()) {
                            it.remove();
                        } else {
                            double value = pot.getValue();
                            double diff = value - pot.lastMonitor;
                            if (diff > pot.epsilon || diff < -pot.epsilon) {
                                pot.lastMonitor = value;
                                ForkJoinPool.commonPool().execute(() -> {
                                    for (PotentiometerListener l : new ArrayList<>(pot.listeners)) {
                                        l.change(value);
                                    }
                                });
                            }
                        }
                    }
                }
            }
            try {
                Thread.sleep(THREAD_DELAY);
            } catch (InterruptedException ie) {
            }
        }
    });

    static {
        MONITOR_THREAD.setDaemon(true);
        MONITOR_THREAD.setPriority(Thread.MIN_PRIORITY);
        MONITOR_THREAD.start();
    }

    private static void startMonitoring(AbstractPotentiometer pot) {
        synchronized (POTS_TO_MONITOR) {
            pot.lastMonitor = pot.getValue();
            POTS_TO_MONITOR.add(pot);
            POTS_TO_MONITOR.notify();
        }
    }

    private static void stopMonitoring(AbstractPotentiometer pot) {
        synchronized (POTS_TO_MONITOR) {
            POTS_TO_MONITOR.remove(pot);
        }
    }

    private double lastMonitor = 0.0;

    private volatile double epsilon;
    private final Collection<PotentiometerListener> listeners;

    public AbstractPotentiometer(ComponentLocation location) {
        super(location);
        epsilon = DEFAULT_EPSILON;
        listeners = new ArrayList<>(1);
    }

    @Override
    public String getType() {
        return "Potentiometer";
    }

    @Override
    public void addListener(PotentiometerListener listener) {
        synchronized (POTS_TO_MONITOR) {
            if (!POTS_TO_MONITOR.contains(this)) {
                startMonitoring(this);
            }
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(PotentiometerListener listener) {
        if (listeners.contains(listener)) {
            synchronized (POTS_TO_MONITOR) {
                if (POTS_TO_MONITOR.contains(this)) {
                    stopMonitoring(this);
                }
                listeners.remove(listener);
            }
        }
    }

    @Override
    public void removeAllListeners() {
        if (listeners.isEmpty()) {
            return;
        }
        synchronized (POTS_TO_MONITOR) {
            if (POTS_TO_MONITOR.contains(this)) {
                stopMonitoring(this);
            }
            listeners.clear();
        }
    }

    @Override
    public double getEpsilon() {
        return epsilon;
    }

    @Override
    public void setEpsilon(double epsilon) {
        if (epsilon <= Double.MIN_NORMAL) {
            throw new MBedStateException("Epsilon value too small or negative.");
        }
        this.epsilon = epsilon;
    }

}
