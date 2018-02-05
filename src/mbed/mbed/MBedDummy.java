package mbed.mbed;

import javax.imageio.ImageIO;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author kg246
 */
public class MBedDummy implements MBed {
    private final Jitter jitter;
    private final JFrame frame;
    private final int dpi;

    private final PotentiometerDummy pot1, pot2;
    private final AccelerometerDummy accelB, accelS;
    private final MagnemometerDummy mag;
    private final PiezoDummy piezo;
    private final ThermometerDummy therm;
    private final LEDDummy ledB, ledS;
    private final LCDDummy lcd;
    private final ButtonDummy sw2, sw3, up, down, left, right, fire;

    public MBedDummy(long seed) {
        this(new Random(seed));
    }

    /*package*/ MBedDummy(Random random) {
        dpi = Toolkit.getDefaultToolkit().getScreenResolution();

        jitter = new Jitter(random);
        frame = new JFrame("K64F");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pot1 = new PotentiometerDummy(ComponentLocation.Left);
        pot2 = new PotentiometerDummy(ComponentLocation.Right);
        accelB = new AccelerometerDummy(ComponentLocation.Board, frame);
        accelS = new AccelerometerDummy(ComponentLocation.Shield, frame);
        mag = new MagnemometerDummy();
        piezo = new PiezoDummy();
        therm = new ThermometerDummy();
        ledB = new LEDDummy(ComponentLocation.Board);
        ledS = new LEDDummy(ComponentLocation.Shield);
        lcd = new LCDDummy();
        sw2 = new ButtonDummy(ComponentLocation.Left, "Switch");
        sw3 = new ButtonDummy(ComponentLocation.Right, "Switch");
        up = new ButtonDummy(ComponentLocation.Up, "Joystick");
        down = new ButtonDummy(ComponentLocation.Down, "Joystick");
        left = new ButtonDummy(ComponentLocation.Left, "Joystick");
        right = new ButtonDummy(ComponentLocation.Right, "Joystick");
        fire = new ButtonDummy(ComponentLocation.Centre, "Joystick");
        layoutFrame();

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                accelB.timer.start();
                accelS.timer.start();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                accelB.timer.stop();
                accelS.timer.stop();
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            boolean pressed;
            switch (e.getID()) {
                case KeyEvent.KEY_PRESSED:
                    pressed = true;
                    break;
                case KeyEvent.KEY_RELEASED:
                    pressed = false;
                    break;
                default:
                    return false;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    up.notifyListeners(pressed);
                    return true;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    down.notifyListeners(pressed);
                    return true;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    left.notifyListeners(pressed);
                    return true;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    right.notifyListeners(pressed);
                    return true;
                case KeyEvent.VK_SPACE:
                    fire.notifyListeners(pressed);
                    return true;
                case KeyEvent.VK_CONTROL:
                    sw2.notifyListeners(pressed);
                    return true;
                case KeyEvent.VK_ALT:
                    sw3.notifyListeners(pressed);
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public String getLocation() {
        return "DUMMY";
    }

    public void open() {
//		frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void close() throws MBedStateException {
        try {
            frame.setVisible(false);
            frame.dispose();
        } catch (Exception ex) {
            throw new MBedStateException(ex);
        }
    }

    @Override
    public boolean isOpen() {
        return frame.isVisible() && frame.isDisplayable();
    }

    private void checkOpen() {
        if (!frame.isVisible()) throw new MBedStateException("MBed connection closed.", null);
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
        return sw2;
    }

    @Override
    public Button getSwitch3() {
        return sw3;
    }

    @Override
    public Button getJoystickUp() {
        return up;
    }

    @Override
    public Button getJoystickDown() {
        return down;
    }

    @Override
    public Button getJoystickLeft() {
        return left;
    }

    @Override
    public Button getJoystickRight() {
        return right;
    }

    @Override
    public Button getJoystickFire() {
        return fire;
    }

    @Override
    public LED getLEDBoard() {
        return ledB;
    }

    @Override
    public LED getLEDShield() {
        return ledS;
    }

    public JFrame getFrame() {
        return frame;
    }

    private void layoutFrame() {
        frame.setResizable(false);
        frame.add(generatePane(), BorderLayout.CENTER);
        frame.add(generateTemp(), BorderLayout.WEST);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public JPanel generateTemp()
    {
        JPanel temp = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Dimension size = getSize();
                ((Graphics2D) g).setPaint(new GradientPaint(0, 0, Color.red, 0, size.height, Color.blue));
                g.fillRect(0, 0, size.width, size.height);
            }
        };
        temp.add(therm.gauge, new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        temp.add(therm.text, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        JLabel lbl = new JLabel(" °C");
        lbl.setOpaque(true);
        temp.add(lbl, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        return temp;
    }

    public JPanel generatePane()
    {
        final BufferedImage bkgd = getBackground(dpi);

        JPanel pane = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(bkgd, 0, 0, null);
            }
        };

        pane.add(pot1.twiddle);
        pot1.twiddle.setLocation(dpi * 27 / 100, dpi * 250 / 100);

        pane.add(pot2.twiddle);
        pot2.twiddle.setLocation(dpi * 146 / 100, dpi * 250 / 100);

        pane.add(mag.twiddle);
        mag.twiddle.setLocation(dpi * 161 / 100, dpi * 17 / 100);

        pane.add(lcd.comp);
        lcd.comp.setLocation(dpi * 31 / 100, dpi * 133 / 100);

        pane.add(ledB.lbl);
        ledB.lbl.setLocation(dpi * 48 / 100 - dpi / 8, dpi * 322 / 100 - dpi / 8);

        pane.add(ledS.lbl);
        ledS.lbl.setLocation(dpi * 177 / 100 - dpi / 8, dpi * 95 / 100 - dpi / 8);

        pane.add(sw2.btn);
        sw2.btn.setLocation(dpi * 184 / 100, dpi * 307 / 100);

        pane.add(sw3.btn);
        sw3.btn.setLocation(dpi / 5, dpi * 307 / 100);

        pane.add(fire.btn);
        fire.btn.setLocation(dpi, dpi * 265 / 100);

        pane.add(up.btn);
        up.btn.setLocation(fire.btn.getX(), fire.btn.getY() - fire.btn.getWidth());

        pane.add(down.btn);
        down.btn.setLocation(fire.btn.getX(), fire.btn.getY() + fire.btn.getWidth());

        pane.add(left.btn);
        left.btn.setLocation(fire.btn.getX() - fire.btn.getHeight(), fire.btn.getY());

        pane.add(right.btn);
        right.btn.setLocation(fire.btn.getX() + fire.btn.getHeight(), fire.btn.getY());

        pane.setPreferredSize(new Dimension(bkgd.getWidth(), bkgd.getHeight()));

        return pane;
    }

    private static BufferedImage getBackground(int dpi) {
        try {
            BufferedImage mbed = ImageIO.read(ClassLoader.getSystemResource("mbed/images/MBed.png"));
            BufferedImage img = new BufferedImage(dpi * 137 / 64, dpi * 215 / 64, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.drawImage(mbed, 0, 0, img.getWidth(), img.getHeight(), null);
            g.dispose();
            return img;
        } catch (IOException ex) {
            BufferedImage bkgd = new BufferedImage(dpi * 137 / 64, dpi * 215 / 64, BufferedImage.TYPE_BYTE_BINARY);
            Graphics2D g = bkgd.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, bkgd.getWidth(), bkgd.getHeight());
            g.dispose();
            return bkgd;
        }
    }

    private class Twiddle extends JComponent {

        private final BufferedImage image;
        private double rot;
        private final AffineTransform tx;
        private final double rotationRange;
        private final double rotationOffset;

        public Twiddle(BufferedImage image, double rotationRange, double rotationOffset) {
            super.setOpaque(false);
            this.image = image;
            this.rotationRange = rotationRange;
            this.rotationOffset = rotationOffset;
            tx = new AffineTransform();
            rot = 0;
            tx.rotate(rotationOffset, image.getWidth() * 0.5, image.getHeight() * 0.5);

            MouseAdapter ma = new MouseAdapter() {
                boolean dragging;

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragging) {
                        pos(e);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1)
                        dragging = false;
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        dragging = true;
                        pos(e);
                    }
                }

                private void pos(MouseEvent e) {
                    Dimension size = Twiddle.this.getSize();
                    double x = e.getX() - size.width * 0.5;
                    double y = e.getY() - size.height * 0.5;
                    double rotation = Math.atan2(y, x) - rotationOffset;
                    if (rotation < 0 || rotation > Twiddle.this.rotationRange) {
                        double loDiff, hiDiff, shifted;
                        if (rotation < 0) {
                            shifted = rotation + Math.PI * 2.0;
                            loDiff = Math.abs(rotation);
                            hiDiff = Math.abs(shifted - Twiddle.this.rotationRange);
                        } else {
                            shifted = rotation - Math.PI * 2.0;
                            loDiff = Math.abs(shifted);
                            hiDiff = Math.abs(rotation - Twiddle.this.rotationRange);
                        }
                        if (shifted < 0 || shifted > Twiddle.this.rotationRange) {
                            rotation = loDiff < hiDiff ? 0 : Twiddle.this.rotationRange;
                        } else {
                            rotation = shifted;
                        }
                    }
                    Twiddle.this.setRotation(rotation);
                }
            };

            super.addMouseListener(ma);
            super.addMouseMotionListener(ma);
        }

        @Override
        public Dimension getPreferredSize() {
            if (isPreferredSizeSet())
                return super.getPreferredSize();
            return new Dimension(image.getWidth(), image.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            AffineTransform old = g2.getTransform();
            g2.transform(tx);
            g2.drawImage(image, 0, 0, null);
            g2.setTransform(old);
        }

        public void setRotation(double rotation) {
            if (rot == rotation) return;
            rot = rotation;
            double wrapped = rot + rotationOffset;
            if (wrapped < 0)
                wrapped += Math.PI * 2.0;
            else if (wrapped > Math.PI)
                wrapped -= Math.PI * 2.0;
            tx.setToIdentity();
            tx.rotate(wrapped, image.getWidth() * 0.5, image.getHeight() * 0.5);
            repaint();
        }

        public double getRotation() {
            return rot;
        }

    }

    private class PotentiometerDummy extends AbstractPotentiometer {
        private final Twiddle twiddle;

        public PotentiometerDummy(ComponentLocation location) {
            super(location);
            final int size = (int) (0.375 * dpi);
            BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, size, size);
            g.setColor(new Color(0, 130, 225));
            g.fillOval(0, 0, size, size);
            g.setColor(Color.BLACK);
            g.drawLine(size >> 2, size >> 1, size * 3 / 4, size >> 1);
            g.drawLine(size * 3 / 4, size >> 1, size >> 1, size * 3 / 4);
            g.drawLine(size * 3 / 4, size >> 1, size >> 1, size / 4);
            g.dispose();
            twiddle = new Twiddle(img, Math.PI * 1.6, Math.PI * 0.2);
            twiddle.setSize(size, size);
        }

        @Override
        public MBed getParent() {
            return MBedDummy.this;
        }

        @Override
        public double getValue() {
            checkOpen();
            double val = (twiddle.getRotation() + jitter.getJitter(0.4769245, 0.02)) / (Math.PI * 1.6);
            if (val < 0.0) return 0.0;
            if (val > 1.0) return 1.0;
            return val;
        }
    }

    private class AccelerometerDummy extends AbstractAccelerometer {

        private final Timer timer;
        private double x, y, z;

        public AccelerometerDummy(ComponentLocation location, final JFrame frame) {
            super(location);
            timer = new Timer(100, new ActionListener() {
                int prevX = frame.getX();
                int prevY = frame.getY();

                @Override
                public void actionPerformed(ActionEvent e) {
                    y = (-(frame.getX() - prevX)) * 0.0001;
                    prevX = frame.getX();
                    x = (-(frame.getY() - prevY)) * 0.0001;
                    prevY = frame.getY();
                }
            });
            z = 1;
        }

        @Override
        public MBed getParent() {
            return MBedDummy.this;
        }

        @Override
        public XYZData getAcceleration() {
            checkOpen();
            return new XYZData(
                    (float) (x + jitter.getJitter(0.4852745, 0.1) - 0.075),
                    (float) (y + jitter.getJitter(0.7521648, 0.1) - 0.075),
                    (float) (z + jitter.getJitter(0.5367163, 0.1) - 0.075));
        }
    }

    private class MagnemometerDummy extends AbstractMagnemometer {
        private final Twiddle twiddle;

        public MagnemometerDummy() {
            BufferedImage img = new BufferedImage((dpi >> 1), (dpi >> 1), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.CYAN);
            g.fillOval(0, 0, dpi >> 1, dpi >> 1);
            g.setColor(Color.RED);
            g.fillPolygon(new int[]{dpi >> 1, dpi >> 2, dpi >> 2}, new int[]{dpi >> 2, dpi * 5 / 16, dpi * 3 / 16}, 3);
            g.setColor(Color.BLUE);
            g.fillPolygon(new int[]{0, dpi >> 2, dpi >> 2}, new int[]{dpi >> 2, dpi * 5 / 16, dpi * 3 / 16}, 3);
            g.setColor(Color.WHITE);
            g.fillOval(dpi * 3 / 16, dpi * 3 / 16, dpi >> 3, dpi >> 3);
            g.dispose();
            twiddle = new Twiddle(img, Math.PI * 2.0, Math.PI * -0.5);
            twiddle.setSize((dpi >> 1), (dpi >> 1));
        }

        @Override
        public MBed getParent() {
            return MBedDummy.this;
        }

        @Override
        public Orientation getOrientation() {
            checkOpen();
            return Orientation.Down;
        }

        @Override
        public Orientation getSide() {
            checkOpen();
            return Orientation.Front;
        }

        private double getAngle() {
            double a = twiddle.getRotation() + jitter.getJitter(0.38968369, 0.030625 * 0.125);
            if (a > 360.0) return a - 360.0;
            if (a < 0) return a + 360.0;
            return a;
        }

        @Override
        public double getHeading() {
            return getAngle() * 180.0 / Math.PI;
        }

        @Override
        public XYZData getMagnetism() {
            double rot = getAngle();
            return new XYZData(oneDP(Math.cos(rot) * 30 + jitter.getJitter(5.46)), oneDP(Math.sin(rot) * 30 + jitter.getJitter(2.46456)), oneDP(jitter.getJitter(2.46836, 46.5, 47.5)));
        }

        private double oneDP(double x) {
            return (float) (Math.round(x * 10.0) / 10.0);
        }
    }

    private class PiezoDummy extends AbstractPiezo {

        private final Synthesizer synthesizer;
        private final MidiChannel channel;
        private int lastNote = -1;

        public PiezoDummy() {
            Synthesizer synth;
            MidiChannel chan;
            try {
                synth = MidiSystem.getSynthesizer();
                synth.open();
                chan = synth.getChannels()[0];
            } catch (MidiUnavailableException e) {
                synth = null;
                chan = null;
            }
            synthesizer = synth;
            channel = chan;
        }

        @Override
        public MBed getParent() {
            return MBedDummy.this;
        }

        private double log2(double x) {
            return Math.log(x) / Math.log(2);
        }

        @Override
        public void playSound(double volume, double frequency) {
            if (channel != null) {
                channel.noteOn(lastNote = (int) (12 * log2(frequency / 440)) + 69, (int) (volume * 127.0));
            }
        }

        @Override
        public void playNote(Note note) {
            if (note == null)
                silence();
            else if (channel != null) {
                channel.noteOn(lastNote = note.getMidiNumber(), 127);
            }
        }

        @Override
        public void silence() {
            if (channel != null && lastNote >= 0) {
                channel.noteOff(lastNote);
                lastNote = -1;
            }
        }

        public void close() {
            synthesizer.close();
        }
    }

    private class ThermometerDummy extends AbstractThermometer {

        private final JSlider gauge;
        private final JFormattedTextField text;

        public ThermometerDummy() {
            gauge = new JSlider(JSlider.VERTICAL, 30, 60, 50); //0.5 precision
            gauge.setOpaque(false);

            text = new JFormattedTextField(new DecimalFormat("##.0"));
            text.setValue(25.0);

            gauge.addChangeListener(e -> {
                text.setValue(gauge.getValue() * 0.5);
            });

            text.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        text.select(0, text.getText().length());
                    });
                }

                @Override
                public void focusLost(FocusEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        text.select(0, 0);
                    });
                }
            });

            text.addActionListener(e -> {
                gauge.setValue((int) (((Number) text.getValue()).doubleValue() * 2.0));
                gauge.requestFocus();
            });
        }

        @Override
        public MBed getParent() {
            return MBedDummy.this;
        }

        @Override
        public double getTemperature() {
            checkOpen();
            return gauge.getValue() * 0.5;
        }
    }

    private class LEDDummy extends AbstractLED {
        private final JLabel lbl;
        private final BufferedImage led;

        public LEDDummy(ComponentLocation location) {
            super(location);
            led = new BufferedImage(dpi >> 2, dpi >> 2, BufferedImage.TYPE_INT_ARGB);
            lbl = new JLabel(new ImageIcon(led));
            lbl.setOpaque(false);
            lbl.setSize(dpi >> 2, dpi >> 2);
        }

        @Override
        public MBed getParent() {
            return MBedDummy.this;
        }

        @Override
        protected void applyColor(LEDColor col) {
            checkOpen();

            int r = (col.ordinal() & 4) == 0 ? 0 : 255;
            int g = (col.ordinal() & 2) == 0 ? 0 : 255;
            int b = (col.ordinal() & 1) == 0 ? 0 : 255;

            for (int[] bank : ((DataBufferInt) led.getRaster().getDataBuffer()).getBankData()) {
                for (int i = bank.length; i-- > 0; )
                    bank[i] = 0;
            }
            float alpha = Math.max(r, Math.max(g, b)) / 255f;
            if (alpha > 0f) {
                float[] hsb = Color.RGBtoHSB(r, g, b, null);
                int rgb = Color.HSBtoRGB(hsb[0], hsb[1], 1.0f) & 0x00FFFFFF;
                Color col1 = new Color(rgb | ((int) (alpha * 255f) << 24), true);
                Color col2 = new Color(rgb, true);
                Graphics2D gfx = led.createGraphics();
                gfx.setPaint(new RadialGradientPaint(new Point(dpi >> 3, dpi >> 3), alpha * (dpi >> 3), new float[]{0, 1}, new Color[]{col1, col2}));
                gfx.fillRect(0, 0, dpi >> 2, dpi >> 2);
                gfx.dispose();
            }
            lbl.repaint();
        }
    }

    private class LCDDummy extends AbstractLCD {
        private final JComponent comp;
        private final BufferedImage lcd;

        private final Color OFF_COL = new Color(100, 120, 90);
        private final Color ON_COL = new Color(80, 85, 75);

        public LCDDummy() {
            lcd = new BufferedImage(128, 32, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = lcd.createGraphics();
            g.setColor(OFF_COL);
            g.fillRect(0, 0, 128, 32);
            g.dispose();
            comp = new JComponent() {
                @Override
                protected void paintComponent(Graphics g) {
                    Dimension size = comp.getSize();
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.scale(size.width / 128.0, size.height / 32.0);
                    g2.drawImage(lcd, 0, 0, null);
                }
            };
            comp.setSize(dpi * 98 / 64, dpi * 35 / 64);
        }

        @Override
        public MBed getParent() {
            return MBedDummy.this;
        }

        @Override
        public void print(int x, int y, String text) {
            checkOpen();
            Graphics2D g = lcd.createGraphics();

            for (char c : text.toCharArray()) {
                x = MBedFont.drawChar(g, x, y, c, ON_COL, OFF_COL);
            }
            g.dispose();
            comp.repaint();
        }

        @Override
        public void clear() {
            checkOpen();
            Graphics2D g = lcd.createGraphics();
            g.setColor(OFF_COL);
            g.fillRect(0, 0, 128, 32);
            g.dispose();
            comp.repaint();
        }

        @Override
        public void setPixel(int x, int y, PixelColor pixel) {
            checkOpen();
            lcd.setRGB(x, y, (pixel == PixelColor.ON ? ON_COL : OFF_COL).getRGB());
            comp.repaint();
        }

        @Override
        public void drawCircle(int centerX, int centerY, int radius, PixelColor pixel) {
            checkOpen();
            Graphics2D g = lcd.createGraphics();
            g.setColor((pixel == PixelColor.ON ? ON_COL : OFF_COL));
            g.drawOval(centerX - radius, centerY - radius, radius * 2 - 1, radius * 2 - 1);
            g.dispose();
            comp.repaint();
        }

        @Override
        public void fillCircle(int centerX, int centerY, int radius, PixelColor pixel) {
            checkOpen();
            Graphics2D g = lcd.createGraphics();
            g.setColor((pixel == PixelColor.ON ? ON_COL : OFF_COL));
            g.fillOval(centerX - radius, centerY - radius, radius * 2 - 1, radius * 2 - 1);
            g.dispose();
            comp.repaint();
        }

        @Override
        public void drawLine(int startX, int startY, int endX, int endY, PixelColor pixel) {
            checkOpen();
            Graphics2D g = lcd.createGraphics();
            g.setColor((pixel == PixelColor.ON ? ON_COL : OFF_COL));
            g.drawLine(startX, startY, endX, endY);
            g.dispose();
            comp.repaint();
        }

        @Override
        public void drawRectangle(int left, int top, int width, int height, PixelColor pixel) {
            checkOpen();
            Graphics2D g = lcd.createGraphics();
            g.setColor((pixel == PixelColor.ON ? ON_COL : OFF_COL));
            g.drawRect(left, top, width, height);
            g.dispose();
            comp.repaint();
        }

        @Override
        public void fillRectangle(int left, int top, int width, int height, PixelColor pixel) {
            checkOpen();
            Graphics2D g = lcd.createGraphics();
            g.setColor((pixel == PixelColor.ON ? ON_COL : OFF_COL));
            g.fillRect(left, top, width, height);
            g.dispose();
            comp.repaint();
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

    private class ButtonDummy extends AbstractButton implements Button {
        private final JComponent btn;
        private boolean pressed;
        private final String type;

        public ButtonDummy(ComponentLocation location, String type) {
            super(location);
            this.type = type;

            btn = new JComponent() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(pressed ? Color.WHITE : Color.BLACK);
                    g.fillRect(0, 0, 10, 10);
                }

            };
            btn.setSize(10, 10);
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        ButtonDummy.this.notifyListeners(false);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        ButtonDummy.this.notifyListeners(true);
                    }
                }
            });
        }

        @Override
        void notifyListeners(boolean isDown) {
            pressed = isDown;
            super.notifyListeners(isDown); //To change body of generated methods, choose Tools | Templates.
            btn.repaint();
        }

        @Override
        public MBed getParent() {
            return MBedDummy.this;
        }

        @Override
        boolean queryState() {
            checkOpen();
            return pressed;
        }

        @Override
        public String getType() {
            return type;
        }
    }

}

class MBedFont {

    private static final char[] FONT_8X8 = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // space 0x20
            0x30, 0x78, 0x78, 0x30, 0x30, 0x00, 0x30, 0x00, // !
            0x6C, 0x6C, 0x6C, 0x00, 0x00, 0x00, 0x00, 0x00, // "
            0x6C, 0x6C, 0xFE, 0x6C, 0xFE, 0x6C, 0x6C, 0x00, // #
            0x18, 0x3E, 0x60, 0x3C, 0x06, 0x7C, 0x18, 0x00, // $
            0x00, 0x63, 0x66, 0x0C, 0x18, 0x33, 0x63, 0x00, // %
            0x1C, 0x36, 0x1C, 0x3B, 0x6E, 0x66, 0x3B, 0x00, // &
            0x30, 0x30, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, // '
            0x0C, 0x18, 0x30, 0x30, 0x30, 0x18, 0x0C, 0x00, // (
            0x30, 0x18, 0x0C, 0x0C, 0x0C, 0x18, 0x30, 0x00, // )
            0x00, 0x66, 0x3C, 0xFF, 0x3C, 0x66, 0x00, 0x00, // *
            0x00, 0x30, 0x30, 0xFC, 0x30, 0x30, 0x00, 0x00, // +
            0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x18, 0x30, // ,
            0x00, 0x00, 0x00, 0x7E, 0x00, 0x00, 0x00, 0x00, // -
            0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x18, 0x00, // .
            0x03, 0x06, 0x0C, 0x18, 0x30, 0x60, 0x40, 0x00, // / (forward slash)
            0x3E, 0x63, 0x63, 0x6B, 0x63, 0x63, 0x3E, 0x00, // 0 0x30
            0x18, 0x38, 0x58, 0x18, 0x18, 0x18, 0x7E, 0x00, // 1
            0x3C, 0x66, 0x06, 0x1C, 0x30, 0x66, 0x7E, 0x00, // 2
            0x3C, 0x66, 0x06, 0x1C, 0x06, 0x66, 0x3C, 0x00, // 3
            0x0E, 0x1E, 0x36, 0x66, 0x7F, 0x06, 0x0F, 0x00, // 4
            0x7E, 0x60, 0x7C, 0x06, 0x06, 0x66, 0x3C, 0x00, // 5
            0x1C, 0x30, 0x60, 0x7C, 0x66, 0x66, 0x3C, 0x00, // 6
            0x7E, 0x66, 0x06, 0x0C, 0x18, 0x18, 0x18, 0x00, // 7
            0x3C, 0x66, 0x66, 0x3C, 0x66, 0x66, 0x3C, 0x00, // 8
            0x3C, 0x66, 0x66, 0x3E, 0x06, 0x0C, 0x38, 0x00, // 9
            0x00, 0x18, 0x18, 0x00, 0x00, 0x18, 0x18, 0x00, // :
            0x00, 0x18, 0x18, 0x00, 0x00, 0x18, 0x18, 0x30, // ;
            0x0C, 0x18, 0x30, 0x60, 0x30, 0x18, 0x0C, 0x00, // <
            0x00, 0x00, 0x7E, 0x00, 0x00, 0x7E, 0x00, 0x00, // =
            0x30, 0x18, 0x0C, 0x06, 0x0C, 0x18, 0x30, 0x00, // >
            0x3C, 0x66, 0x06, 0x0C, 0x18, 0x00, 0x18, 0x00, // ?
            0x3E, 0x63, 0x6F, 0x69, 0x6F, 0x60, 0x3E, 0x00, // @ 0x40
            0x18, 0x3C, 0x66, 0x66, 0x7E, 0x66, 0x66, 0x00, // A
            0x7E, 0x33, 0x33, 0x3E, 0x33, 0x33, 0x7E, 0x00, // B
            0x1E, 0x33, 0x60, 0x60, 0x60, 0x33, 0x1E, 0x00, // C
            0x7C, 0x36, 0x33, 0x33, 0x33, 0x36, 0x7C, 0x00, // D
            0x7F, 0x31, 0x34, 0x3C, 0x34, 0x31, 0x7F, 0x00, // E
            0x7F, 0x31, 0x34, 0x3C, 0x34, 0x30, 0x78, 0x00, // F
            0x1E, 0x33, 0x60, 0x60, 0x67, 0x33, 0x1F, 0x00, // G
            0x66, 0x66, 0x66, 0x7E, 0x66, 0x66, 0x66, 0x00, // H
            0x3C, 0x18, 0x18, 0x18, 0x18, 0x18, 0x3C, 0x00, // I
            0x0F, 0x06, 0x06, 0x06, 0x66, 0x66, 0x3C, 0x00, // J
            0x73, 0x33, 0x36, 0x3C, 0x36, 0x33, 0x73, 0x00, // K
            0x78, 0x30, 0x30, 0x30, 0x31, 0x33, 0x7F, 0x00, // L
            0x63, 0x77, 0x7F, 0x7F, 0x6B, 0x63, 0x63, 0x00, // M
            0x63, 0x73, 0x7B, 0x6F, 0x67, 0x63, 0x63, 0x00, // N
            0x3E, 0x63, 0x63, 0x63, 0x63, 0x63, 0x3E, 0x00, // O
            0x7E, 0x33, 0x33, 0x3E, 0x30, 0x30, 0x78, 0x00, // P 0x50
            0x3C, 0x66, 0x66, 0x66, 0x6E, 0x3C, 0x0E, 0x00, // Q
            0x7E, 0x33, 0x33, 0x3E, 0x36, 0x33, 0x73, 0x00, // R
            0x3C, 0x66, 0x30, 0x18, 0x0C, 0x66, 0x3C, 0x00, // S
            0x7E, 0x5A, 0x18, 0x18, 0x18, 0x18, 0x3C, 0x00, // T
            0x66, 0x66, 0x66, 0x66, 0x66, 0x66, 0x7E, 0x00, // U
            0x66, 0x66, 0x66, 0x66, 0x66, 0x3C, 0x18, 0x00, // V
            0x63, 0x63, 0x63, 0x6B, 0x7F, 0x77, 0x63, 0x00, // W
            0x63, 0x63, 0x36, 0x1C, 0x1C, 0x36, 0x63, 0x00, // X
            0x66, 0x66, 0x66, 0x3C, 0x18, 0x18, 0x3C, 0x00, // Y
            0x7F, 0x63, 0x46, 0x0C, 0x19, 0x33, 0x7F, 0x00, // Z
            0x3C, 0x30, 0x30, 0x30, 0x30, 0x30, 0x3C, 0x00, // [
            0x60, 0x30, 0x18, 0x0C, 0x06, 0x03, 0x01, 0x00, // \ (back slash)
            0x3C, 0x0C, 0x0C, 0x0C, 0x0C, 0x0C, 0x3C, 0x00, // ]
            0x08, 0x1C, 0x36, 0x63, 0x00, 0x00, 0x00, 0x00, // ^
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFF, // _
            0x18, 0x18, 0x0C, 0x00, 0x00, 0x00, 0x00, 0x00, // ` 0x60
            0x00, 0x00, 0x3C, 0x06, 0x3E, 0x66, 0x3B, 0x00, // a
            0x70, 0x30, 0x3E, 0x33, 0x33, 0x33, 0x6E, 0x00, // b
            0x00, 0x00, 0x3C, 0x66, 0x60, 0x66, 0x3C, 0x00, // c
            0x0E, 0x06, 0x3E, 0x66, 0x66, 0x66, 0x3B, 0x00, // d
            0x00, 0x00, 0x3C, 0x66, 0x7E, 0x60, 0x3C, 0x00, // e
            0x1C, 0x36, 0x30, 0x78, 0x30, 0x30, 0x78, 0x00, // f
            0x00, 0x00, 0x3B, 0x66, 0x66, 0x3E, 0x06, 0x7C, // g
            0x70, 0x30, 0x36, 0x3B, 0x33, 0x33, 0x73, 0x00, // h
            0x18, 0x00, 0x38, 0x18, 0x18, 0x18, 0x3C, 0x00, // i
            0x06, 0x00, 0x06, 0x06, 0x06, 0x66, 0x66, 0x3C, // j
            0x70, 0x30, 0x33, 0x36, 0x3C, 0x36, 0x73, 0x00, // k
            0x38, 0x18, 0x18, 0x18, 0x18, 0x18, 0x3C, 0x00, // l
            0x00, 0x00, 0x66, 0x7F, 0x7F, 0x6B, 0x63, 0x00, // m
            0x00, 0x00, 0x7C, 0x66, 0x66, 0x66, 0x66, 0x00, // n
            0x00, 0x00, 0x3C, 0x66, 0x66, 0x66, 0x3C, 0x00, // o
            0x00, 0x00, 0x6E, 0x33, 0x33, 0x3E, 0x30, 0x78, // p
            0x00, 0x00, 0x3B, 0x66, 0x66, 0x3E, 0x06, 0x0F, // q
            0x00, 0x00, 0x6E, 0x3B, 0x33, 0x30, 0x78, 0x00, // r
            0x00, 0x00, 0x3E, 0x60, 0x3C, 0x06, 0x7C, 0x00, // s
            0x08, 0x18, 0x3E, 0x18, 0x18, 0x1A, 0x0C, 0x00, // t
            0x00, 0x00, 0x66, 0x66, 0x66, 0x66, 0x3B, 0x00, // u
            0x00, 0x00, 0x66, 0x66, 0x66, 0x3C, 0x18, 0x00, // v
            0x00, 0x00, 0x63, 0x6B, 0x7F, 0x7F, 0x36, 0x00, // w
            0x00, 0x00, 0x63, 0x36, 0x1C, 0x36, 0x63, 0x00, // x
            0x00, 0x00, 0x66, 0x66, 0x66, 0x3E, 0x06, 0x7C, // y
            0x00, 0x00, 0x7E, 0x4C, 0x18, 0x32, 0x7E, 0x00, // z
            0x0E, 0x18, 0x18, 0x70, 0x18, 0x18, 0x0E, 0x00, // {
            0x0C, 0x0C, 0x0C, 0x00, 0x0C, 0x0C, 0x0C, 0x00, // |
            0x70, 0x18, 0x18, 0x0E, 0x18, 0x18, 0x70, 0x00, // }
            0x3B, 0x6E, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // ~
            0x1C, 0x36, 0x36, 0x1C, 0x00, 0x00, 0x00, 0x00 // DEL
    };

    public static int drawChar(Graphics g, int x, int y, char c, Color one, Color zero) {

        if (c < 32 || c > 127) return x;

        int index = (c - 32) * 8;

        g.setColor(zero);
        g.fillRect(x, y, 8, 8);
        g.setColor(one);
        for (int _y = 0; _y < 8; _y++, index++) {
            for (int _x = 0; _x < 8; _x++) {
                if (((FONT_8X8[index] >> (7 - _x)) & 1) == 1)
                    g.fillRect(x + _x, y + _y, 1, 1);
            }
        }
        return x + 8;
    }

}