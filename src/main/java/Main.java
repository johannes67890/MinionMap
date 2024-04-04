import GUI.Controller;
import GUI.MainView;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.File;

public class Main extends Application {

    private MainView mainView;

    @Override
    public void start(Stage stage) {

        mainView = new MainView(stage);

        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void stop(){
        String destDir = System.getProperty("user.dir").toString() + "\\src\\main\\resources\\osmFiles\\";
        File file = new File(destDir);
        if(file.isDirectory()){
            for(File f : file.listFiles()){
                f.delete();
            }
        }
    }

}
