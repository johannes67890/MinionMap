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

    private boolean isMenuOpen = false;
    private static MainView mainView;

    double lastX;
    double lastY;

    double zoomMultiplier = 1.01f;
    
    public Controller(MainView mainView){

        //System.out.println("CONTROLLER MADE");


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


    public void start(MainView mw){
        mainView = mw;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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