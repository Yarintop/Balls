package sample;

import javafx.animation.*;
import javafx.geometry.Point2D;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class CustomShape extends Circle {
    Circle circle;
    Point2D centerBeforeMoving;
    Point2D clickWhilePressed;
    Random r;

    public CustomShape(ArrayList<CustomShape> circles) {
        //r = new Random();
        //circle = new Circle(x, y, r.nextInt(50) + 25, Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble()));
        circle = new Circle();
        circle.setCursor(Cursor.HAND);

        circle.setOnMousePressed(e -> {
            centerBeforeMoving = new Point2D(this.circle.getCenterX(), this.circle.getCenterY());
            clickWhilePressed = new Point2D(e.getX(), e.getY());
            circle.toFront();
        });

        circle.setOnMouseDragged(e -> {
            this.circle.setCenterX(centerBeforeMoving.getX() + e.getX() - clickWhilePressed.getX());
            this.circle.setCenterY(centerBeforeMoving.getY() + e.getY() - clickWhilePressed.getY());
            checkCollide(circles, this);
        });
    }

    public void createCircle(double x, double y, ArrayList<CustomShape> circles) {
        r = new Random();
        CustomShape customShape = this;
        //circle = new Circle(x, y, r.nextInt(50) + 25, Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble()));
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setFill(Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble()));
        circle.setRadius(1);
        double radius = r.nextInt(50) + 25;

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                checkCollide(circles, customShape);
            }
        };

        Timeline timeline1 = new Timeline();
        KeyValue kv1 = new KeyValue(circle.radiusProperty(), radius + 5, Interpolator.EASE_OUT);
        KeyFrame kf1 = new KeyFrame(Duration.millis(200), kv1);
        timeline1.getKeyFrames().add(kf1);

        Timeline timeline2 = new Timeline();
        KeyValue kv2 = new KeyValue(circle.radiusProperty(), radius - 2, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.millis(100), kv2);
        timeline2.getKeyFrames().add(kf2);

        Timeline timeline3 = new Timeline();
        KeyValue kv3 = new KeyValue(circle.radiusProperty(), radius, Interpolator.EASE_OUT);
        KeyFrame kf3 = new KeyFrame(Duration.millis(50), event -> timer.stop(), kv1);
        timeline3.getKeyFrames().add(kf3);

        SequentialTransition sequence = new SequentialTransition(timeline1, timeline2, timeline3);
        timer.start();
        sequence.play();
    }


    public void checkCollide(ArrayList<CustomShape> customShapes, CustomShape customShape) {
        for (CustomShape c :
                customShapes) {
            Circle tempCircle = c.getCircle();
            Circle circle = customShape.getCircle();
            if (tempCircle != circle) {
                double totalDist = circle.getRadius() + tempCircle.getRadius();
                double dist = Math.sqrt(Math.pow(circle.getCenterX() - tempCircle.getCenterX(), 2) + Math.pow(circle.getCenterY() - tempCircle.getCenterY(), 2));
                if (dist < totalDist) {
                    ArrayList<CustomShape> temp = (ArrayList) customShapes.clone();
                    temp.remove(customShape);
                    temp.remove(c);
                    checkCollide(temp, c);
                    double angle = Math.atan2(circle.getCenterY() - tempCircle.getCenterY(),
                            circle.getCenterX() - tempCircle.getCenterX());
                    tempCircle.setCenterX(circle.getCenterX() - (totalDist * Math.cos(angle)));
                    tempCircle.setCenterY(circle.getCenterY() - (totalDist * Math.sin(angle)));
                }
            }
        }
    }

    public Circle getCircle() {
        return circle;
    }
}
