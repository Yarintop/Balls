package sample;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class CustomShape extends Circle {
    Circle circle;
    Point2D centerBeforeMoving;
    Point2D clickWhilePressed;
    Random r;

    public CustomShape(double x, double y, ArrayList<CustomShape> circles) {
        r = new Random();
        circle = new Circle(x, y, r.nextInt(50) + 25, Color.color(r.nextDouble(), r.nextDouble(), r.nextDouble()));
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
