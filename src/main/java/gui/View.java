package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import gui.MainView.StageSelect;
import parser.Model;

import java.net.URL;
/**
 * The View class is the superclass for all views in the application
 * It is responsible for initializing the controller and the scene of the view
 */
public abstract class View {
    private FXMLLoader loader;
    private VBox vBox;
    private ControllerInterface controller;
    private MainView mainView;
    private Scene scene;
    private Model model;
    private DrawingMap drawingMap;

    /**
     * Constructor for the View class
     * @param mainView the mainView object
     * @param url the URL of the FXML file
     * @throws Exception if the FXML file is not found
     */
    protected View(MainView mainView, URL url) throws Exception{
        this.mainView = mainView;
        this.loader = new FXMLLoader(url);
        this.vBox = loader.load();
        this.controller = (ControllerInterface) loader.getController();
    }

    /**
     * Getter for the current scene
     * @return the current scene
     */
    public Scene getScene(){
        return this.scene;
    }

    /**
     * Setter for the drawingmap
     * @param drawingMap
     */
    protected void setDrawingMap(DrawingMap drawingMap){
        this.drawingMap = drawingMap;
    }

    /**
     * Getter for the drawingmap
     * @return the drawingmap
     */
    protected DrawingMap getDrawingMap(){
        return this.drawingMap;
    }

    /**
     * Getter for the model
     * @return the model
     */
    protected Model getModel(){
        return model;
    }

    /**
     * Getter for the model
     * @return the model
     */
    protected void setModel(Model model){
        this.model = model;
    }

    /**
     * Getter for the stage
     * @return the stage
     */
    protected Stage getStage(){
        return this.mainView.getStage();
    }

    /**
     * drawScene method to draw the scene
     * @param bool a boolean to determine which scene to draw
     */
    protected void drawScene(Boolean bool){
        if(bool){
            mainView.drawScene(StageSelect.MapView);
        } else {
            mainView.drawScene(StageSelect.MainMenu);
        }
    }
    /**
     * Setter for the scene
     * @param scene the scene to be set
     */
    private void setScene(Scene scene){
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

        this.scene = scene;
    }

    /**
     * This method is called in the constructor of the subclass
     * It initializes the controller with a reference to this object
     * It sets the scene of the view
     */
    protected void initializeView(){
        controller.start(this); // To initialize the controller with a reference to this object

        setScene(new Scene(vBox));
    }


}
