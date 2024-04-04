package Address;
import javafx.application.Application;
import javafx.stage.Stage;

import GUI.MainView;

public class HelloFX extends Application{

    private MainView mw;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mw = new MainView(primaryStage);
    }
}
