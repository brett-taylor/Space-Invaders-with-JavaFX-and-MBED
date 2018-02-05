package mbed.mbed;

import java.util.concurrent.ForkJoinPool;

/**
 * @author kg246
 */
public class MBedDevice implements MBed {

    private final String portName;

    // The communications port for the MBed.
    private final MBedDataSerial port;

    private final PotentiometerImpl pot1, pot2;
    private final AccelerometerImpl accelB, accelS;
    private final MagnemometerImpl mag;
    private final PiezoImpl piezo;
    private final ThermometerImpl therm;
    private final LEDImpl ledB, ledS;
    private final LCDImpl lcd;
    private final ButtonImpl btnSW2, btnSW3, btnUp, btnDown, btnLeft, btnRight, btnFire;

    /*package*/ MBedDevice(String portName) {
        this.portName = portName;

        pot1 = new PotentiometerImpl(ComponentLocation.Left, '1');
        pot2 = new PotentiometerImpl(ComponentLocation.Right, '2');
        accelB = new AccelerometerImpl(ComponentLocation.Board, Command.READ_BOARD_ACCEL);
        accelS = new AccelerometerImpl(ComponentLocation.Shield, Command.READ_SHIELD_ACCEL);
        mag = new MagnemometerImpl();
        piezo = new PiezoImpl();
        therm = new ThermometerImpl();
        ledB = new LEDImpl(ComponentLocation.Board, Command.SET_BOARD_LED);
        ledS = new LEDImpl(ComponentLocation.Shield, Command.SET_SHIELD_LED);
        lcd = new LCDImpl();
        btnSW2 = new ButtonImpl(ComponentLocation.Right, "Switch", '2');
        btnSW3 = new ButtonImpl(ComponentLocation.Left, "Switch", '3');
        btnUp = new ButtonImpl(ComponentLocation.Up, "Joystick", 'u');
        btnDown = new ButtonImpl(ComponentLocation.Down, "Joystick", 'd');
        btnLeft = new ButtonImpl(ComponentLocation.Left, "Joystick", 'l');
        btnRight = new ButtonImpl(ComponentLocation.Right, "Joystick", 'r');
        btnFire = new ButtonImpl(ComponentLocation.Centre, "Joystick", 'f');

        port = new MBedDataSerial(portName);
        port.connect();
        port.appendCommand(Command.RECONNECT);
        port.flush();
        if (port.getByte() != Command.RECONNECT.ordinal()) {
            throw new MBedSetupException("MBed hand-shake failed.");
        }

        port.setSpecialCommandHandler(new MBedDataSerial.SpecialCommandHandler() {

            public void buttonChange(ButtonImpl btn, char state) {
                ForkJoinPool.commonPool().execute(() -> {
                    switch (state) {
                        case 'u':
                            btn.notifyListeners(false);
                            break;
                        case 'd':
                            btn.notifyListeners(true);
                            break;
                        default:
                            break;
                    }
                });
            }

            @Override
            public void handleSpecialCommand(char c1, char c2) {
                switch (c1) {
                    case '2':
                        buttonChange(btnSW2, c2);
                        break;
                    case '3':
                        buttonChange(btnSW3, c2);
                        break;
                    case 'u':
                        buttonChange(btnUp, c2);
                        break;
                    case 'd':
                        buttonChange(btnDown, c2);
                        break;
                    case 'l':
                        buttonChange(btnLeft, c2);
                        break;
                    case 'r':
                        buttonChange(btnRight, c2);
                        break;
                    case 'f':
                        buttonChange(btnFire, c2);
                        break;
                }
            }
        });
    }

    @Override
    public String getLocation() {
        return portName;
    }

    @Override
    public Potentiometer getPotentiometer1() {
        return pot1;
    }

    @Override
    public Potentiometer getPotentiometer2() {
        return pot2;
    }

    @Override
    public Piezo getPiezo() {
        return piezo;
    }

    @Override
    public Thermometer getThermometer() {
        return therm;
    }

    @Override
    public Accelerometer getAccelerometerBoard() {
        return accelB;
    }

    @Override
    public Accelerometer getAccelerometerShield() {
        return accelS;
    }

    @Override
    public Magnemometer getMagnemometer() {
        return mag;
    }

    @Override
    public LCD getLCD() {
        return lcd;
    }

    @Override
    public Button getSwitch2() {
        return btnSW2;
    }

    @Override
    public Button getSwitch3() {
        return btnSW3;
    }

    @Override
    public Button getJoystickUp() {
        return btnUp;
    }

    @Override
    public Button getJoystickDown() {
        return btnDown;
    }

    @Override
    public Button getJoystickLeft() {
        return btnLeft;
    }

    @Override
    public Button getJoystickRight() {
        return btnRight;
    }

    @Override
    public Button getJoystickFire() {
        return btnFire;
    }

    @Override
    public LED getLEDBoard() {
        return ledB;
    }

    @Override
    public LED getLEDShield() {
        return ledS;
    }

    @Override
    public void close() throws MBedStateException {
        try {
            if (isOpen()) {
                getPiezo().silence();
                getLCD().clear();
                port.flush();
                port.close();
            }
        } catch (Exception ex) {
            throw new MBedStateException(ex);
        }
    }

    @Override
    public boolean isOpen() {
        return port.isOpen();
    }

    ////////////////////////////////
    //                            //
    //  Component Implementations //
    //                            //
    ////////////////////////////////

    private class AccelerometerImpl extends AbstractAccelerometer {

        private final Command cmd;

        public AccelerometerImpl(ComponentLocation location, Command cmd) {
            super(location);
            this.cmd = cmd;
        }

        @Override
        public MBed getParent() {
            return MBedDevice.this;
        }

        @Override
        public XYZData getAcceleration() {
            synchronized (port) {
                port.appendCommand(cmd);
                port.flush();
                float x, y, z;
                x = port.getFloat();
                y = port.getFloat();
                z = port.getFloat();
                return new XYZData(x, y, z);
            }
        }

    }

    private class PotentiometerImpl extends AbstractPotentiometer {

        private final char ID;

        public PotentiometerImpl(ComponentLocation location, char ID) {
            super(location);
            this.ID = ID;
        }

        @Override
        public MBed getParent() {
            return MBedDevice.this;
        }

        @Override
        public double getValue() {
            synchronized (port) {
                port.appendCommand(Command.READ_POT);
                port.appendChars(ID);
                port.flush();
                return port.getFloat();
            }
        }

    }

    private class PiezoImpl extends AbstractPiezo {

        @Override
        public MBed getParent() {
            return MBedDevice.this;
        }

        @Override
        public void playSound(double volume, double frequency) {
            if (frequency < PIEZO_MIN_FREQUENCY || frequency > PIEZO_MAX_FREQUENCY)
                throw new IllegalArgumentException("Frequency " + frequency + " out of range. [" + PIEZO_MIN_FREQUENCY + ".." + PIEZO_MAX_FREQUENCY + "]");
            if (volume < 0.0 || volume > 1.0)
                throw new IllegalArgumentException("Volume " + volume + " out of range. [0..1]");
            synchronized (port) {
                port.appendCommand(Command.SET_PIEZO);
                port.appendFloats((float) volume, (float) frequency);
                port.flush();
            }
        }

        @Override
        public void playNote(Note note) {
            if (note == null) {
                silence();
            } else {
                playSound(1.0, note.getFreq());
            }
        }

        @Override
        public void silence() {
            playSound(0.0, Piezo.PIEZO_MIN_FREQUENCY);
        }

    }

    private class ButtonImpl extends AbstractButton {

        private final String type;

        private final char ID;

        public ButtonImpl(ComponentLocation location, String type, char ID) {
            super(location);
            this.type = type;
            this.ID = ID;
        }

        @Override
        public MBed getParent() {
            return MBedDevice.this;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public boolean queryState() {
            synchronized (port) {
                port.appendCommand(Command.READ_DIGITAL);
                port.appendChars(ID);
                port.flush();
                return port.getBool();
            }
        }

    }

    private class MagnemometerImpl extends AbstractMagnemometer {

        @Override
        public MBed getParent() {
            return MBedDevice.this;
        }

        @Override
        public Orientation getOrientation() {
            synchronized (port) {
                port.appendCommand(Command.GET_ORIENTATION);
                port.flush();
                return Orientation.values()[port.getByte()];
            }
        }

        @Override
        public Orientation getSide() {
            synchronized (port) {
                port.appendCommand(Command.GET_SIDE);
                port.flush();
                return Orientation.values()[port.getByte()];
            }
        }

        @Override
        public double getHeading() {
            synchronized (port) {
                port.appendCommand(Command.GET_HEADING);
                port.flush();
                return port.getFloat();
            }
        }

        @Override
        public XYZData getMagnetism() {
            synchronized (port) {
                port.appendCommand(Command.READ_BOARD_MAGNO);
                port.flush();
                float x, y, z;
                x = port.getFloat();
                y = port.getFloat();
                z = port.getFloat();
                return new XYZData(x, y, z);
            }
        }

    }

    private class LCDImpl extends AbstractLCD {

        @Override
        public MBed getParent() {
            return MBedDevice.this;
        }

        @Override
        public void print(int x, int y, String text) {
            if (text == null || text.isEmpty()) return;
            int len = text.length();
            if (len > 255) throw new IllegalArgumentException("Can not send strings with length greater than 255.");
            synchronized (port) {
                port.appendCommand(Command.SET_LCD_POSITION);
                port.appendBytes(x, y);
                port.appendCommand(Command.PRINT_TEXT);
                port.appendChars(text.toCharArray());
                port.flush(true);
            }
        }

        @Override
        public void clear() {
            synchronized (port) {
                port.appendCommand(Command.CLEAR_LCD);
                port.flush(true);
            }
        }

        @Override
        public void setPixel(int x, int y, PixelColor pixel) {
            synchronized (port) {
                port.appendCommand(Command.SET_PIXEL);
                port.appendBytes(x, y, pixel.ordinal());
                port.flush();
            }
        }

        @Override
        public void drawCircle(int centerX, int centerY, int radius, PixelColor pixel) {
            synchronized (port) {
                port.appendCommand(Command.DRAW_CIRCLE);
                port.appendBytes(centerX, centerY, radius, pixel.ordinal());
                port.flush();
            }
        }

        @Override
        public void fillCircle(int centerX, int centerY, int radius, PixelColor pixel) {
            synchronized (port) {
                port.appendCommand(Command.FILL_CIRCLE);
                port.appendBytes(centerX, centerY, radius, pixel.ordinal());
                port.flush();
            }
        }

        @Override
        public void drawLine(int startX, int startY, int endX, int endY, PixelColor pixel) {
            synchronized (port) {
                port.appendCommand(Command.DRAW_LINE);
                port.appendBytes(startX, startY, endX, endY, pixel.ordinal());
                port.flush();
            }
        }

        @Override
        public void drawRectangle(int left, int top, int width, int height, PixelColor pixel) {
            synchronized (port) {
                port.appendCommand(Command.DRAW_RECT);
                port.appendBytes(left, top, left + width, top + height, pixel.ordinal());
                port.flush();
            }
        }

        @Override
        public void fillRectangle(int left, int top, int width, int height, PixelColor pixel) {
            synchronized (port) {
                port.appendCommand(Command.FILL_RECT);
                port.appendBytes(left, top, left + width, top + height, pixel.ordinal());
                port.flush();
            }
        }

        @Override
        public int getWidth() {
            return 128;
        }

        @Override
        public int getHeight() {
            return 32;
        }

    }

    private class LEDImpl extends AbstractLED {

        private final Command cmd;

        public LEDImpl(ComponentLocation location, Command cmd) {
            super(location);
            this.cmd = cmd;
        }

        @Override
        public MBed getParent() {
            return MBedDevice.this;
        }

        @Override
        protected void applyColor(LEDColor col) {
            synchronized (port) {
                port.appendCommand(cmd);
                port.appendBytes(col.ordinal());
                port.flush();
            }
        }

    }

    private class ThermometerImpl extends AbstractThermometer {

        @Override
        public MBed getParent() {
            return MBedDevice.this;
        }

        @Override
        public double getTemperature() {
            synchronized (port) {
                port.appendCommand(Command.READ_TEMP);
                port.flush();
                return port.getFloat();
            }
        }

    }

}