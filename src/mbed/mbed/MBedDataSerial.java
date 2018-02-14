package mbed.mbed;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author kg246
 */
/*package*/ class MBedDataSerial implements AutoCloseable {

    private static final boolean DEBUG = false;
    private static final long RECEIVE_TIMEOUT = 1000L;

    private final SerialPort port;
    private final BlockingQueue<Object> transfer = new ArrayBlockingQueue<>(100);
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream(100);

    private SpecialCommandHandler scHandler = null;

    public MBedDataSerial(String portName) {
        port = new SerialPort(portName);
    }

    public SpecialCommandHandler getSpecialCommandHandler() {
        return scHandler;
    }

    public void setSpecialCommandHandler(SpecialCommandHandler scHandler) {
        this.scHandler = scHandler;
    }

    public void connect() {
        try {
            port.openPort();
            port.setParams(SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            port.setEventsMask(SerialPort.MASK_RXCHAR | SerialPort.MASK_TXEMPTY);//Set mask
            port.purgePort(SerialPort.PURGE_RXCLEAR);
            port.addEventListener(new SerialPortReader());
            port.purgePort(SerialPort.PURGE_RXCLEAR);
        } catch (SerialPortException ex) {
            error("MBed connection failed.", ex);
        }

    }

    /**
     * This method will always throw an MBedStateException, but will first try
     * to clean up the connection, as the internal read thread can prevent the
     * correct termination of the program.
     *
     * @param message
     */
    public void error(String message) {
        error(message, null);
    }

    /**
     * This method will always throw an MBedStateException, but will first try
     * to clean up the connection, as the internal read thread can prevent the
     * correct termination of the program.
     *
     * @param message The message the exception should contain.
     * @param cause   The encapsulated error/exception object of the original
     *                cause.
     * @throws MBedStateException Always
     */
    public void error(String message, Throwable cause) throws MBedStateException {
        if (port != null && port.isOpened())
            try {
                port.closePort();
            } catch (SerialPortException ex) {
            }
        throw new MBedStateException(message, cause);
    }

    @Override
    public void close() {
        try {
            port.closePort();
        } catch (SerialPortException ex) {
            error("MBed connection failed.", ex);
        }
    }

    public boolean isOpen() {
        return port.isOpened();
    }

    public void appendCommand(Command cmd) {
        appendBytes(cmd.ordinal());
    }

    public void appendBytes(int b) {
        if (!port.isOpened()) error("MBed connection closed.", null);
        synchronized (buffer) {
            buffer.write(1);
            buffer.write(b);
        }
    }

    public void appendBytes(int b1, int b2) {
        if (!port.isOpened()) error("MBed connection closed.", null);
        synchronized (buffer) {
            buffer.write(2);
            buffer.write(b1);
            buffer.write(b2);
        }
    }

    /**
     * Appends a sequence of bytes
     *
     * @param bytes
     */
    public void appendBytes(int... bytes) {
        if (!port.isOpened()) error("MBed connection closed.", null);
        synchronized (buffer) {
            buffer.write(bytes.length);
            for (int b : bytes) {
                buffer.write(b);
            }
        }
    }

    public void appendChars(char... chars) {
        if (!port.isOpened()) error("MBed connection closed.", null);
        synchronized (buffer) {
            buffer.write(chars.length);
            for (char c : chars) {
                buffer.write((byte) c);
            }
        }
    }

    public void appendChars(char c) {
        if (!port.isOpened()) error("MBed connection closed.", null);
        synchronized (buffer) {
            buffer.write(1);
            buffer.write((byte) c);
        }
    }

    public void appendChars(char c1, char c2) {
        if (!port.isOpened()) error("MBed connection closed.", null);
        synchronized (buffer) {
            buffer.write(2);
            buffer.write((byte) c1);
            buffer.write((byte) c2);
        }
    }

    /**
     * @param floats
     */
    public void appendFloats(float... floats) {
        for (float f : floats) {
            int i = Float.floatToIntBits(f);
            appendBytes(i, i >>> 8, i >>> 16, i >>> 24);
        }
    }

    /**
     * Flushes all pending data to MBed without waiting for acknowledgement.
     */
    public void flush() {
        flush(false);
    }

    /**
     * Flushes all pending data to MBed
     *
     * @param waitAck True if the flush method is to wait for an acknowledgement.
     */
    public void flush(boolean waitAck) {
        try {
            byte[] array;
            synchronized (buffer) {
                array = buffer.toByteArray();
                buffer.reset();
            }
            if (DEBUG) {
                System.out.print("Sending: ");
                System.out.println(Arrays.toString(array));
            }
            synchronized (port) {
                port.writeBytes(array);
                if (waitAck && !getBool()) {
                    error("Acknowledgement Failed");
                }
            }
        } catch (SerialPortException ex) {
            error("MBed connection failed.", ex);
        }
    }

    /**
     * Sends special characters immediately (does not append to buffer)
     *
     * @param char1
     * @param char2
     */
    public void sendSpecialChars(char char1, char char2) {
        if (!port.isOpened()) error("MBed connection closed.", null);
        try {
            if (DEBUG) {
                System.out.print("Sending Special: ");
                System.out.print(char1);
                System.out.println(char2);
            }
            synchronized (port) {
                port.writeByte((byte) 0);
                port.writeByte((byte) char1);
                port.writeByte((byte) char2);
            }
        } catch (SerialPortException ex) {
            error("MBed connection failed.", ex);
        }
    }

    private int[] getBytes(int len) {
        try {
            Object polled = transfer.poll(RECEIVE_TIMEOUT, TimeUnit.MILLISECONDS);
            if (polled == null) error("MBed connection timed out.", null);
            if (polled instanceof int[]) {
                int[] bytes = (int[]) polled;
                if (DEBUG) {
                    System.out.print("Received: ");
                    System.out.println(Arrays.toString(bytes));
                }
                if (bytes.length == len) return bytes;
                error("MBed connection failed. Expected " + len + " bytes, received " + (bytes.length == 1 ? "1 byte: " : bytes.length + " bytes: ") + Arrays.toString(bytes), null);
            }
            if (polled instanceof Exception) error("MBed connection failed.", (Exception) polled);
            error("MBed connection failed. Unknown receive state", null);
        } catch (InterruptedException ex) {
            error("MBed connection interrupted.", null);
        }
        return null; //unreachable.
    }

    public int getByte() {
        return getBytes(1)[0];
    }

    public char getChar() {
        return (char) getBytes(1)[0];
    }

    public float getFloat() {
        int[] bytes = getBytes(4);
        return Float.intBitsToFloat(bytes[0] | (bytes[1] << 8) | (bytes[2] << 16) | (bytes[3] << 24));
    }

    public int getInt16() {
        int[] bytes = getBytes(2);
        return (bytes[0] | (bytes[1] << 8));
    }

    public boolean getBool() {
        return getChar() == 't';
    }

    public static interface SpecialCommandHandler {

        void handleSpecialCommand(char c1, char c2);

    }

    private class SerialPortReader implements SerialPortEventListener {

        int[] buffer;
        int index = 0;
        final int[] interruptCache = new int[1];

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                try {
                    for (byte b : port.readBytes(event.getEventValue())) {
                        if (DEBUG) {
                            System.out.print("Raw In: ");
                            if (b >= 0 && b < 32)
                                System.out.println("#" + b);
                            else
                                System.out.println((char) b);
                        }
                        if (buffer == null) {
                            if (b == 0) buffer = interruptCache;
                            else buffer = new int[b];
                            index = 0;
                        } else if (buffer == interruptCache) {
                            if (index == 0) {
                                interruptCache[0] = b & 0xFF;
                                index = 1;
                            } else {
                                if (DEBUG) {
                                    System.out.print("Sending Special: ");
                                    System.out.print((char) interruptCache[0]);
                                    System.out.println((char) b);
                                }

                                char c1 = (char) interruptCache[0], c2 = (char) b;
                                if (scHandler != null)
                                    scHandler.handleSpecialCommand(c1, c2);
                                buffer = null;
                            }
                        } else {
                            buffer[index++] = b & 0xFF;
                            if (index == buffer.length) { //has read full packet
                                transfer.add(buffer);
                                buffer = null;
                            }
                        }

                    }
                } catch (SerialPortException ex) {
                    transfer.add(ex);
                }
            }
        }
    }

}
