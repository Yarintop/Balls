package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        ArrayList<CustomShape> circles = new ArrayList<>();
        Scene scene = new Scene(pane, 800, 800);
        pane.setStyle("-fx-background-color: cyan");
        pane.setOnMousePressed(e -> {
            if (e.getTarget() == e.getSource()) {
                CustomShape circle = new CustomShape(circles, scene.getWidth(), scene.getHeight());
                circles.add(circle);
                pane.getChildren().add(circle.getCircle());
                circle.createCircle(e.getX(), e.getY(), circles);
            }
        });
        pane.widthProperty().addListener((observable, oldValue, newValue) -> {
            for (CustomShape c :
                    circles) {
                c.checkCollideWithWidth(circles, newValue);
            }
        });
        pane.heightProperty().addListener((observable, oldValue, newValue) -> {
            for (CustomShape c :
                    circles) {
                c.checkCollideWithHeight(circles, newValue);
            }
        });
        primaryStage.setMinWidth(200);
        primaryStage.setMinHeight(200);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
