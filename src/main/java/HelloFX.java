
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import javafx.scene.canvas.*;

public class HelloFX extends Application {


    Canvas canvas = new Canvas(640, 480);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    @Override
    public void start(Stage stage) {
        
        MainView mv = new MainView(stage);
        
    }

    public static void main(String[] args) {
        launch();
    }

}