import gui.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private MainView mainView;

    @Override
    public void start(Stage stage) {
        mainView = new MainView(stage);
        stage.show();

    }

    public void startTest(Stage stage) {
        mainView = new MainView(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void stop(){
    
    }

}
