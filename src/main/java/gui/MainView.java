package gui;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import parser.XMLReader;
import structures.ZipHandler;
import util.FileDistributer;
public class MainView {

    enum StageSelect {

        MainMenu,
        MapView

    }

    public static Stage stage;
    static StageSelect selectedStage = StageSelect.MainMenu;
    static int sizeX = 800;
    static int sizeY = 600;
    public ResizableCanvas canvas;
    static GraphicsContext gc;
    static Scene lastScene;
    static Rectangle2D screenBounds;
    public XMLReader xmlReader;
    public DrawingMap drawView;
    private File inputFile;


    public MainView(Stage stage){

        MainView.stage = stage;
        screenBounds = Screen.getPrimary().getVisualBounds();
        MainView.stage.setMinWidth(sizeX);
        MainView.stage.setMinHeight(sizeY);
        MainView.stage.setResizable(true);

        drawScene(StageSelect.MainMenu);
    }

    public void setCanvas(ResizableCanvas canvas){
        this.canvas = canvas;
    }

    public void drawScene(StageSelect selected){
        selectedStage = selected;
        VBox root;
        FXMLLoader loader;
        try{
            if (selectedStage == StageSelect.MainMenu){
                loader = new FXMLLoader(new URL("file:" + FileDistributer.start_screen.getFilePath()));
                root =loader.load();
            }else { // else its mapView
                loader = new FXMLLoader(new URL("file:" + FileDistributer.main.getFilePath()));
                root = loader.load();
            }

            ControllerInterface controller = (ControllerInterface) loader.getController();
            controller.start(this); // To initialize the controller with a reference to this object
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if (selectedStage == StageSelect.MapView){
            System.out.println("INITIALZING");
            drawView.initialize(canvas);
        }
    }

    public void loadXMLReader(String filePath){
        xmlReader = new XMLReader(filePath);
    }

    public void loadDrawingMap(){
        drawView = new DrawingMap(this, xmlReader);
    }

    public void draw(){

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawView.DrawMap(canvas);
        
    }


    /**
     * Function for finding a file path for chosen file
     * via a filechooser dialog window and unzipping to a give directory if the file is a zip file
     * @return String path to the file chosen
     */
    public static String pathFindFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Ressource File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("OSM Files", "*.osm"),
            new FileChooser.ExtensionFilter("ZIP", "*.zip")
            );
            fileChooser.setInitialDirectory(new java.io.File("C:\\Users\\"));
            java.io.File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
            if(file.toString().contains(".zip")) {
            ZipHandler zip = new ZipHandler();
                try {
                    zip.unzip(file.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.toString();
    }

    public DrawingMap getDrawingMap() {
        return drawView;
    }

}