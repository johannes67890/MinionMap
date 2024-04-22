package gui;
import java.net.URL;
import java.util.ResourceBundle;

import gui.MainView.StageSelect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Controller implements Initializable, ControllerInterface{
    
    @FXML private Button menuButton1;
    @FXML private Button menuButton2;
    @FXML private Button layerButton;
    @FXML private Button searchButton;
    @FXML private Pane leftBurgerMenu;
    @FXML private TextField searchBarStart;
    @FXML private TextField searchBarDestination;
    @FXML private Button mainMenuButton;
    @FXML private VBox mainMenuVBox;
    @FXML private VBox graphicVBox;
    @FXML private HBox mainUIHBox;
    @FXML private BorderPane mainBorderPane;
    @FXML private ImageView zoomLevelImage;
    @FXML private Label zoomLevelText;

    private boolean isMenuOpen = false;
    private static MainView mainView;

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
            
            mainView.getDrawingMap().zoombarUpdater(zoomLevelText, zoomLevelImage);
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
            mainMenuVBox.setVisible(!isMenuOpen);
            isMenuOpen = !isMenuOpen;
            
        });
        menuButton2.setOnAction((ActionEvent e) -> {
            leftBurgerMenu.setVisible(!isMenuOpen);
            mainMenuVBox.setVisible(!isMenuOpen);
            isMenuOpen = !isMenuOpen;
            
        });
        layerButton.setOnAction((ActionEvent e) -> {
            graphicVBox.setVisible(true);
            mainMenuVBox.setVisible(false);

        });

        searchBarStart.setOnAction((ActionEvent e) -> {
            System.out.println("Searching for startpoint: " + searchBarStart.getText());
            searchForAdress(searchBarStart.getText());
        });

        searchBarDestination.setOnAction((ActionEvent e) -> {
            System.out.println("Searching for destination: " + searchBarDestination.getText());
            searchForAdress(searchBarDestination.getText());
        });

        searchButton.setOnAction((ActionEvent e) -> {
            System.out.println("Searching for destination: " + searchBarDestination.getText());
            searchForAdress(searchBarDestination.getText());
        });

    }


    private void searchForAdress(String address){

    }

}