



import java.io.File;

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

        /* 
        String destDirChunks = System.getProperty("user.dir").toString() + "\\src\\main\\resources\\chunks\\";
        File chunkFile = new File(destDirChunks);
        if(chunkFile.isDirectory()){
            for(File f : chunkFile.listFiles()){
                f.delete();
            }
        }*/

    }

}
