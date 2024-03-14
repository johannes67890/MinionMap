
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.canvas.*;

public class HelloFX extends Application {


    Canvas canvas = new Canvas(640, 480);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    @Override
    public void start(Stage stage) {
        
        MainView mv = new MainView(stage);
        Controller controller = new Controller(mv);
        
    }

    public static void main(String[] args) {
        launch();
    }

}