package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * GameFrame handles creation of the JavaFX frame and handles creating an Engine;
 * @author Brett Taylor
 */
public class GameFrame extends Application {
    public static void main(String[] args) {
        for (String s : args) {
            if (s.equalsIgnoreCase("debug")) {
                Engine.setDebug(true);
                break;
            }
        }

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Engine.initialise(stage);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                Engine.update(now);
            }
        }.start();
    }
}