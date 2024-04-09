package gui;
import java.net.URL;
import java.util.ResourceBundle;

import gui.MainView.StageSelect;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Controller implements Initializable, ControllerInterface{
    
    @FXML private Button menuButton1;
    @FXML private Button menuButton2;
    @FXML private Button searchButton;
    @FXML private Pane leftBurgerMenu;
    @FXML private TextField searchBarStart;
    @FXML private TextField searchBarDestination;
    @FXML private Button mainMenuButton;
    @FXML private HBox mainUIHBox;
    @FXML private BorderPane mainBorderPane;

    private boolean isMenuOpen = false;
    private static MainView mainView;

    double lastX;
    double lastY;

    double zoomMultiplier = 1.01f;

    public void start(MainView mw){ // this is only ran after the stage is shown
        mainView = mw;

        ResizableCanvas c = new ResizableCanvas(mainView);
        Pane p = new Pane(c);
        mainBorderPane.setCenter(p);
        mainView.setCanvas(c);
        c.widthProperty().bind(p.widthProperty());
        c.heightProperty().bind(p.heightProperty());

        mainView.loadDrawingMap();
        panZoomInitialize();
    }

    private void panZoomInitialize(){ 
        mainView.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });
        
        mainView.canvas.setOnScroll(event -> {

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