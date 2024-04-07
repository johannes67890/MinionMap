import gui.MainView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {

    TextField input;
    TextArea output;
    BorderPane pane;
    Scene scene;

    @Override
    public void start(Stage stage) {

        MainView mainView = new MainView(stage);

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

    @Override
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