package game.world.impl;

import game.Engine;
import game.world.World;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A simple test game.world
 *
 * @author Brett Taylor
 */
public class TestWorld extends World {
    private Circle circle;
    private double ballRadius = 40;
    private double ballX = 100;
    private double ballY = 200;
    private double xSpeed = 4;

    private int colorsIndex = 0;
    private Color[] colors = {
            Color.RED,
            Color.GREEN,
            Color.PURPLE,
            Color.PINK,
            Color.BLUE
    };

    private int radiusIndex = 0;
    private float radiuses[] = {60, 80, 100, 40};

    public TestWorld(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);

        scene.setFill(Color.YELLOW);
        circle = new Circle();
        circle.setCenterX(ballX);
        circle.setCenterY(ballY);
        circle.setRadius(ballRadius);
        circle.setFill(Color.BLUE);
        root.getChildren().add(circle);

        circle.setOnMouseClicked(event -> circleClicked(event));
    }

    /**
     * Updates the object
     *
     * @param deltaTime The delta time of the current frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // UPDATE
        ballX += xSpeed;

        if (ballX + ballRadius >= Engine.getMainStage().getWidth()) {
            ballX = Engine.getMainStage().getWidth() - ballRadius;
            xSpeed *= -1;
        } else if (ballX - ballRadius < 0) {
            ballX = 0 + ballRadius;
            xSpeed *= -1;
        }

        // RENDER
        circle.setCenterX(ballX);
    }

    @Override
    protected void onMouseClicked(MouseEvent event) {
        super.onMouseClicked(event);

        if (event.getButton() == MouseButton.PRIMARY)
            colorsIndex++;

        if (event.getButton() == MouseButton.SECONDARY || colorsIndex >= colors.length)
            colorsIndex = 0;

        circle.setFill(colors[colorsIndex]);
    }

    private void circleClicked(MouseEvent e) {
        radiusIndex++;
        if (radiusIndex >= radiuses.length)
            radiusIndex = 0;

        circle.setRadius(radiuses[radiusIndex]);
    }
}
