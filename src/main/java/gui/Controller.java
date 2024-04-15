package gui;
import java.net.URL;
import java.util.ResourceBundle;

import gui.MainView.StageSelect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import parser.XMLReader;
import parser.TagAddress.SearchAddress;
import util.MecatorProjection;

public class Controller implements Initializable, ControllerInterface{
    
    @FXML private Button menuButton1;
    @FXML private Button menuButton2;
    @FXML private Button searchButton;
    @FXML private Pane leftBurgerMenu;
    @FXML private ComboBox<String> searchBarStart;
    @FXML private ComboBox<String> searchBarDestination;
    @FXML private Button mainMenuButton;
    @FXML private HBox mainUIHBox;
    @FXML private BorderPane mainBorderPane;

    private boolean isMenuOpen = false;
    private static MainView mainView;
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

        mainMenuButton.setOnAction((ActionEvent e) -> {
            mainView.drawScene(StageSelect.MainMenu);
        });

        menuButton1.setOnAction((ActionEvent e) -> {
            leftBurgerMenu.setVisible(!isMenuOpen);
            isMenuOpen = !isMenuOpen;
        });
        menuButton2.setOnAction((ActionEvent e) -> {
            leftBurgerMenu.setVisible(!isMenuOpen);
            isMenuOpen = !isMenuOpen;
        });

        // TODO: remake this function so it registers everytime the value is changed!
        searchBarStart.setOnAction((ActionEvent e) -> {
            System.out.println("Searching for startpoint: " + searchBarStart.getValue());
            search(searchBarStart.getValue());

            if (searchBarStart.getValue().equals("")){
                setEnableDestinationTextField(false);
            }else{
                setEnableDestinationTextField(true);
            }
        });

        //Will be changed later
        searchBarDestination.setOnAction((ActionEvent e) -> {
            System.out.println("Searching for destination: " + searchBarDestination.getValue());
            search(searchBarDestination.getValue());
        });

        searchButton.setOnAction((ActionEvent e) -> {
            System.out.println("Searching for destination: " + searchBarDestination.getValue());
            search(searchBarDestination.getValue());
        });

    }

    private void setEnableDestinationTextField(boolean isEnabled){
        if (isEnabled){
            searchBarDestination.setMaxWidth(1000000);
        }else{
            searchBarDestination.setMaxWidth(0);
        }
        searchBarDestination.setVisible(isEnabled);
    }


    private void search(String address){
        //Search s = new Search(XMLReader.getAddresses());
        //System.out.println(s.toString());
        SearchAddress addressObj = s.searchForAddress(address);
        double[] bounds = mainView.getDrawingMap().getScreenBounds();
        double x = ((bounds[2] - bounds[0]) / 2) + bounds[0];
        double y = ((bounds[3] - bounds[1]) / 2) + bounds[1];
        System.out.println(x + " " + y);
        System.out.println(0.56 * s.getLongitudeByStreet(address) - x + " delta lon");
        System.out.println(-1.0 * s.getLatitudeByStreet(address) - y + " delta lat");
        mainView.getDrawingMap().pan(
            10*(0.56 * s.getLongitudeByStreet(address) - x),
            -1.0 * s.getLatitudeByStreet(address) - y
        );

        searchBarStart.getItems().setAll(
            addressObj.street + " " + 
            addressObj.house + " " + 
            addressObj.floor + " " + 
            addressObj.side + " " + 
            addressObj.postcode + " " + 
            addressObj.city
        );
        searchBarStart.show();

    }

}
