package gui;
import java.net.URL;
import java.util.ResourceBundle;

import gui.GraphicsHandler.GraphicStyle;
import gui.MainView.StageSelect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import parser.TagAddress;
import parser.TagAddress.SearchAddress;

public class Controller implements Initializable, ControllerInterface{
    

    ObservableList<String> style = FXCollections.observableArrayList(
        "default", "dark", "gray scale");

    @FXML private Button menuButton1;
    @FXML private Button menuButton2;
    @FXML private Button menuButton3;
    @FXML private Button layerButton;
    @FXML private Button searchButton;
    @FXML private Pane leftBurgerMenu;
    @FXML private ComboBox<String> searchBarStart;
    @FXML private ComboBox<String> searchBarDestination;
    @FXML private Button mainMenuButton;
    @FXML private VBox mainMenuVBox;
    @FXML private VBox graphicVBox;
    @FXML private HBox mainUIHBox;
    @FXML private BorderPane mainBorderPane;
    @FXML private ChoiceBox<String> styleChoiceBox;





    private boolean isMenuOpen = false;
    private static MainView mainView;
    private String selectedItem;
    private String selectedEndItem;
    Search s = new Search();

    double lastX;
    double lastY;

    double zoomMultiplier = 1.01f;

    long timer = 0;
    
    public void start(MainView mw){ // this is only ran after the stage is shown
        mainView = mw;

        ResizableCanvas c = new ResizableCanvas(mainView);
        Pane p = new Pane(c);
        mainBorderPane.setCenter(p);
        mainView.setCanvas(c);
        mainView.loadDrawingMap();
        c.widthProperty().bind(p.widthProperty());
        c.heightProperty().bind(p.heightProperty());

        System.out.println("DRAWING MAP");

        panZoomInitialize();
    }

    private void panZoomInitialize(){ 
        mainView.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });
        
        mainView.canvas.setOnScroll(event -> {

            if (System.currentTimeMillis() - timer > 500){
                timer = System.currentTimeMillis();
            }

            mainView.getDrawingMap().zoom(Math.pow(zoomMultiplier,event.getDeltaY()), event.getX(), event.getY());

            
            
        });

        mainView.canvas.setOnMouseDragged(e -> {

            double dx = e.getX() - lastX;
            double dy = e.getY() - lastY;
            mainView.getDrawingMap().pan(dx, dy);

            lastX = e.getX();
            lastY = e.getY();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { // This runs when the fxml is loaded and the canvas is injected (before stage is shown)

        mainMenuVBox.setVisible(false);
        leftBurgerMenu.setVisible(false);
        graphicVBox.setVisible(false);

        styleChoiceBox.setItems(style);
        styleChoiceBox.setValue("default");

        styleChoiceBox.setOnAction((ActionEvent e) -> {
            
            switch(styleChoiceBox.getValue()){
                case "default" : {

                    GraphicsHandler.setGraphicsStyle(GraphicStyle.DEFAULT);
                    mainView.draw();

                    System.out.println("HELLO");
                    break;
                }
                case "dark" : {
                    System.out.println("DARKMODE");
                    GraphicsHandler.setGraphicsStyle(GraphicStyle.DARKMODE);
                    mainView.draw();

                    break;

                }
                case "gray scale" : {
                    System.out.println("GRAY SCALE");
                    GraphicsHandler.setGraphicsStyle(GraphicStyle.GRAYSCALE);
                    mainView.draw();
                    break;


                }
            }


        });


        mainMenuButton.setOnAction((ActionEvent e) -> {
            mainView.drawScene(StageSelect.MainMenu);
        });

        menuButton1.setOnAction((ActionEvent e) -> {
            leftBurgerMenu.setVisible(!isMenuOpen);
            mainMenuVBox.setVisible(!isMenuOpen);
            isMenuOpen = !isMenuOpen;
        });
        menuButton2.setOnAction((ActionEvent e) -> {
            leftBurgerMenu.setVisible(!isMenuOpen);
            mainMenuVBox.setVisible(!isMenuOpen);
            isMenuOpen = !isMenuOpen;
        });
        menuButton3.setOnAction((ActionEvent e) -> {
            leftBurgerMenu.setVisible(!isMenuOpen);
            graphicVBox.setVisible(!isMenuOpen);
            isMenuOpen = !isMenuOpen;
        });
        layerButton.setOnAction((ActionEvent e) -> {
            graphicVBox.setVisible(true);
            mainMenuVBox.setVisible(false);

        });

        // TODO: remake this function so it registers everytime the value is changed!
        
        searchBarStart.valueProperty().addListener((observable, oldValue, newValue) -> {
            search(newValue, true);
        });

        searchBarStart.setOnAction((ActionEvent e) -> {
            System.out.println("selected: " + searchBarStart.getSelectionModel().getSelectedItem());
        });

        //Will be changed later
        /*
        searchBarDestination.setOnAction((ActionEvent e) -> {
            System.out.println("Searching for destination: " + searchBarDestination.getValue());
            search(searchBarDestination.getValue());
        });

        searchButton.setOnAction((ActionEvent e) -> {
            System.out.println("Searching for destination: " + searchBarDestination.getValue());
            search(searchBarStart.getValue());
        });
        */

        

    }

     

    private void setEnableDestinationTextField(boolean isEnabled){
        if (isEnabled){
            searchBarDestination.setMaxWidth(1000000);
        }else{
            searchBarDestination.setMaxWidth(0);
        }
        searchBarDestination.setVisible(isEnabled);
    }

    private void chooseDestination(String text, boolean isLeft){
        if (isLeft){
            showAddress(text);
        }else{
        }
    }


    private void search(String address, boolean isStart){
        // Vi har skærmkoordinater i xy og canvas witdh and height
        SearchAddress addressObj = s.searchForAddress(address);
        System.out.println("addressObj: " + addressObj.toString());
        if (isStart && (selectedItem == null || !selectedItem.equals(addressObj.toString()))){
            searchBarStart.getItems().add(0, addressObj.toString());
            if (!searchBarStart.isShowing()){
                searchBarStart.show();
            }

        }else if (!isStart && (selectedEndItem == null || !selectedEndItem.equals(addressObj.toString()))){
            searchBarDestination.getItems().add(0, addressObj.toString());
            if (!searchBarDestination.isShowing()){
                searchBarDestination.show();
            }
        }else{
            if (isStart){
                chooseDestination(selectedItem, true);
            }else{
                chooseDestination(selectedEndItem, false);
            }
        }
        if (isStart){
            selectedItem = addressObj.toString();
        }else{
            selectedEndItem = addressObj.toString();
        }

    }

    private void showAddress(String address){
        SearchAddress addressObj = s.searchForAddress(address);
        double[] bounds = mainView.getDrawingMap().getScreenBounds();
        double x = ((bounds[2] - bounds[0]) / 2) + bounds[0];
        double y = ((bounds[3] - bounds[1]) / 2) + bounds[1];
        TagAddress tagAddress = s.getTagAddressByAddress(addressObj);
        double deltaX = tagAddress.getLon() - x;
        double deltaY = tagAddress.getLat() - y;
        mainView.getDrawingMap().pan(-deltaX, -deltaY);
    }

}
