package GUI;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;

import java.net.URL;

import org.parser.FileDistributer;
import org.parser.XMLReader;

import javafx.fxml.FXMLLoader;

public class MainView {

    enum StageSelect {

        MainMenu,
        MapView

    }

    public static Stage stage;
    static StageSelect selectedStage = StageSelect.MainMenu;
    static int sizeX = 800;
    static int sizeY = 600;
    public static Canvas canvas = new Canvas(sizeX, sizeY);
    static GraphicsContext gc = canvas.getGraphicsContext2D();
    static Scene lastScene;
    static Rectangle2D screenBounds;
    private static XMLReader xmlReader;
    private DrawingMap drawView;


    public MainView(Stage stage){

        MainView.stage = stage;
        screenBounds = Screen.getPrimary().getVisualBounds();
        MainView.stage.setMinWidth(sizeX);
        MainView.stage.setMinHeight(sizeY);
        MainView.stage.setResizable(true);

        drawScene(StageSelect.MainMenu);
    }

    public void drawScene(StageSelect selected){
        selectedStage = selected;
        VBox root;
        FXMLLoader loader;
        try{
            if (selectedStage == StageSelect.MainMenu){
                loader = new FXMLLoader(new URL("file:" + FileDistributer.start_screen.getFilePath()));
                root = loader.load();
            }else { // else its mapView
                loader = new FXMLLoader(new URL("file:" + FileDistributer.main.getFilePath()));
                root = loader.load();
            }

            ControllerInterface controller = (ControllerInterface) loader.getController();
            controller.start(this); // To initialize the controller with a reference to this object

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void loadXMLReader(String filePath){
        //xmlReader = new XMLReader(filePath);
        drawView = new DrawingMap();
        
    }
}