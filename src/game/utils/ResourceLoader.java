package game.utils;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

/**
 * Handles loading all resources that the game will need for later on.
 * @author Brett Taylor
 */
public class ResourceLoader {
    /**
     * The Player Sprite
     */
    public static Image PLAYER_SPRITE = null;

    /**
     * The enemy sprite.
     */
    public static Image ENEMY_SPRITE = null;

    /**
     * Starts loading all resources
     */
    public static void loadAllResources() {
        PLAYER_SPRITE = loadImage(Settings.SPRITES.PLAYER_SPRITE_LOCATION);
        ENEMY_SPRITE = loadImage(Settings.SPRITES.ENEMY_SPRITE_LOCATION);

        Font.loadFont(ClassLoader.getSystemClassLoader().getResourceAsStream("resources/fonts/Adventure.otf"), 14);
    }

    /**
     * Will load a image and return that image
     * @param location the location to load the image from
     * @return The image that was loaded.
     */
    private static Image loadImage(String location) {
        Image image;

        try {
            image = new Image(location);
        } catch (Exception exception) {
            throw new Error("Failed to load new image " + location);
        }

        return image;
    }
}
