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
    private Circle circle;
    private Point2D centerBeforeMoving;
    private Point2D clickWhilePressed;
    private Random r;
    double screenWidth;
    double screenHeight;


    public CustomShape(ArrayList<CustomShape> circles, double screenWidth, double screenHeight) {
        //r = new Random();
        //circle = new Circle(x, y, r.nextInt(50) + 25, Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble()));
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        circle = new Circle();
        circle.setCursor(Cursor.HAND);

        circle.setOnMousePressed(e -> {
            centerBeforeMoving = new Point2D(this.circle.getCenterX(), this.circle.getCenterY());
            clickWhilePressed = new Point2D(e.getX(), e.getY());
            circle.toFront();
        });

        circle.setOnMouseDragged(e -> {
            double wantedX = centerBeforeMoving.getX() + e.getX() - clickWhilePressed.getX();
            double wantedY = centerBeforeMoving.getY() + e.getY() - clickWhilePressed.getY();
            if (wantedX - circle.getRadius() < 0)
                wantedX = circle.getRadius();
            if (wantedX + circle.getRadius() > this.screenWidth)
                wantedX = this.screenWidth - circle.getRadius();
            if (wantedY - circle.getRadius() < 0)
                wantedY = circle.getRadius();
            if (wantedY + circle.getRadius() > this.screenHeight)
                wantedY = this.screenHeight - circle.getRadius();
            circle.setCenterX(wantedX);
            circle.setCenterY(wantedY);
            checkCollide(circles, this);
        });
    }

    public Circle getCircle() {
        return circle;
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
        KeyFrame kf3 = new KeyFrame(Duration.millis(50), event -> timer.stop(), kv3);
        timeline3.getKeyFrames().add(kf3);

        SequentialTransition sequence = new SequentialTransition(timeline1, timeline2, timeline3);
        timer.start();
        sequence.play();
    }

    public void checkCollideWithWidth(ArrayList<CustomShape> circles, Number width) {
        this.screenWidth = width.doubleValue();
        Circle temp = this.getCircle();
        if (temp.getCenterX() + temp.getRadius() > width.doubleValue()) {
            temp.setCenterX(width.doubleValue() - temp.getRadius());
            checkCollide(circles, this);
        }
        if (temp.getCenterX() - temp.getRadius() < 0) {
            temp.setCenterX(temp.getRadius());
            checkCollide(circles, this);
        }
    }

    public void checkCollideWithHeight(ArrayList<CustomShape> circles, Number height) {
        this.screenHeight = height.doubleValue();
        Circle temp = this.getCircle();
        if (temp.getCenterY() + temp.getRadius() > height.doubleValue()) {
            temp.setCenterY(height.doubleValue() - temp.getRadius());
            checkCollide(circles, this);
        }
        if (temp.getCenterY() - temp.getRadius() < 0) {
            temp.setCenterY(temp.getRadius());
            checkCollide(circles, this);
        }
    }

    public void checkCollide(ArrayList<CustomShape> customShapes, CustomShape customShape) {
        Circle circle = customShape.getCircle();
        if (circle.getCenterX() - circle.getRadius() < 0)
            circle.setCenterX(circle.getRadius());
        if (circle.getCenterX() + circle.getRadius() > this.screenWidth)
            circle.setCenterX(this.screenWidth - circle.getRadius());
        if (circle.getCenterY() - circle.getRadius() < 0)
            circle.setCenterY(circle.getRadius());
        if (circle.getCenterY() + circle.getRadius() > this.screenHeight)
            circle.setCenterY(this.screenHeight - circle.getRadius());
        for (CustomShape c :
                customShapes) {
            Circle tempCircle = c.getCircle();
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
                    if (tempCircle.getCenterX() - tempCircle.getRadius() < 0) {
                        tempCircle.setCenterX(tempCircle.getRadius());
                        circle.setCenterX(tempCircle.getCenterX() + (totalDist * Math.cos(angle)));
                    }
                    if (tempCircle.getCenterX() + tempCircle.getRadius() > this.screenWidth) {
                        tempCircle.setCenterX(screenWidth - tempCircle.getRadius());
                        circle.setCenterX(tempCircle.getCenterX() + (totalDist * Math.cos(angle)));
                    }
                    if (tempCircle.getCenterY() - tempCircle.getRadius() < 0) {
                        tempCircle.setCenterY(tempCircle.getRadius());
                        circle.setCenterY(tempCircle.getCenterY() + (totalDist * Math.sin(angle)));
                    }
                    if (tempCircle.getCenterY() + tempCircle.getRadius() > this.screenHeight) {
                        tempCircle.setCenterY(screenHeight - tempCircle.getRadius());
                        circle.setCenterY(tempCircle.getCenterY() + (totalDist * Math.sin(angle)));
                    }
                }
            }
        }
    }

}
