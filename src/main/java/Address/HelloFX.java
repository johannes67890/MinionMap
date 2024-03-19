package Address;



import GUI.Controller;
import GUI.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class HelloFX extends Application {

    TextField input;
    TextArea output;
    BorderPane pane;
    Scene scene;

    @Override
    public void start(Stage stage) {

        MainView mainView = new MainView(stage);
        Controller controller = new Controller(mainView);

        stage.show();

    }

    public void startScene(Stage stage){


        scene = new Scene(pane);

        stage.setTitle("Address Parsing");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}