package gui;


import edu.princeton.cs.algs4.Draw;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import gui.MainView.StageSelect;
import parser.Model;

import java.net.URL;



public abstract class View {
    private FXMLLoader loader;
    private VBox vBox;
    private ControllerInterface controller;
    private MainView mainView;
    private Scene scene;
    private static Model model;
    private DrawingMap drawingMap;

    protected View(MainView mainView, URL url) throws Exception{
        this.mainView = mainView;
        this.loader = new FXMLLoader(url);
        this.vBox = loader.load();
        this.controller = (ControllerInterface) loader.getController();
    }

    public Scene getScene(){
        return this.scene;
    }

    protected void setDrawingMap(DrawingMap drawingMap){
        this.drawingMap = drawingMap;
    }
    protected DrawingMap getDrawingMap(){
        return this.drawingMap;
    }

    protected Model getModel(){
        return this.model;
    }
    protected void setModel(Model model){
        this.model = model;
    }
    protected Stage getStage(){
        return this.mainView.getStage();
    }
    protected void drawScene(Boolean bool){
        if(bool){
            mainView.drawScene(StageSelect.MapView);
        } else {
            mainView.drawScene(StageSelect.MainMenu);
        }
    }
    private void setScene(Scene scene){
        this.scene = scene;
    }
    protected void initializeView(){
        controller.start(this); // To initialize the controller with a reference to this object
        setScene(new Scene(vBox));
    }


}
