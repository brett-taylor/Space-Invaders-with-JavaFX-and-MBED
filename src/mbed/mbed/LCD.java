package mbed.mbed;

/**
 * @author kg246
 */
public interface LCD extends MBedComponent {

    /**
     * Prints a string to the LCD at a given location.
     *
     * @param x    The x coordinate to print the text at.
     * @param y    The y coordinate to print the text at.
     * @param text The text to print, there must be less than 256 characters.
     * @throws MBedStateException If a low level issue occurs whilst attempting
     *                            to communicate with the MBed board.
     */
    public void print(int x, int y, String text);

    /**
     * Request the MBed clear the LCD screen.
     *
     * @throws MBedStateException If a low level issue occurs whilst attempting
     *                            to communicate with the MBed board.
     */
    public void clear();

    /**
     * Turns on or off a specific pixel on the MBed LCD.
     *
     * @param x     The x coordinate of the pixel to set.
     * @param y     The y coordinate of the pixel to set.
     * @param pixel Whether the pixel is to be turned on (black) or off
     *              (clear).
     * @throws MBedStateException If a low level issue occurs whilst attempting
     *                            to communicate with the MBed board.
     */
    public void setPixel(int x, int y, PixelColor pixel);

    /**
     * Draw the outline of a circle on the LCD with the specified properties.
     *
     * @param centreX The horizontal centre of the circle from the lefthand edge
     * @param centreY The vertical center of the circle from the top edge
     * @param radius  The radius of the circle
     * @param pixel   Whether the pixel is to be turned on (black) or off
     *                (clear).
     */
    public void drawCircle(int centreX, int centreY, int radius, PixelColor pixel);

    /**
     * Fills in a circle on the LCD with the specified properties.
     *
     * @param centreX The horizontal centre of the circle from the lefthand edge
     * @param centreY The vertical center of the circle from the top edge
     * @param radius  The radius of the circle
     * @param pixel   Whether the pixel is to be turned on (black) or off
     *                (clear).
     */
    public void fillCircle(int centreX, int centreY, int radius, PixelColor pixel);

    /**
     * Draw a straight line on the LCD with the specified properties.
     *
     * @param startX The X coordinate of the first point of the line, measuring
     *               from the left hand edge of the LCD.
     * @param startY The Y coordinate of the first point of the line, measuring
     *               from the top edge of the LCD.
     *               rectangle.
     * @param endX   The X coordinate of the second point of the line, measuring
     *               from the left hand edge of the LCD.
     * @param endY   The Y coordinate of the second point of the line, measuring
     *               from the top edge of the LCD.
     * @param pixel  Whether the pixel is to be turned on (black) or off
     *               (clear).
     */
    public void drawLine(int startX, int startY, int endX, int endY, PixelColor pixel);

    /**
     * Draw the outline of a rectangle on the LCD with the specified properties.
     *
     * @param left   The distance from the left edge of the LCD to the left edge
     *               of the rectangle.
     * @param top    The distance between the top of the LCD and the top of the
     *               rectangle.
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @param pixel  Whether the pixel is to be turned on (black) or off
     *               (clear).
     */
    public void drawRectangle(int left, int top, int width, int height, PixelColor pixel);

    /**
     * Fill in a rectangle on the LCD with the specified properties.
     *
     * @param left   The distance from the left edge of the LCD to the left edge
     *               of the rectangle.
     * @param top    The distance between the top of the LCD and the top of the
     *               rectangle.
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @param pixel  Whether the pixel is to be turned on (black) or off
     *               (clear).
     */
    public void fillRectangle(int left, int top, int width, int height, PixelColor pixel);

    /**
     * A simple descriptor useful in identifying components. The LCD will
     * always return {@link ComponentLocation#Board}.
     *
     * @return a ComponentLocation that identifies the location of the LCD
     */
    @Override
    public ComponentLocation getLocation();

    /**
     * Get the width of the LCD screen in pixels. This will typically be 128.
     *
     * @return The width of the LCD in pixels
     */
    public int getWidth();

    /**
     * Get the height of the LCD screen in pixels. This will typically be 32.
     *
     * @return The height of the LCD in pixels
     */
    public int getHeight();

}
