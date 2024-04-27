package gui;
import java.net.URL;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gui.GraphicsHandler.GraphicStyle;
import gui.MainView.StageSelect;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import parser.Tag;
import parser.TagAddress;
import parser.TagNode;
import parser.TagWay;
import parser.TagAddress.SearchAddress;
import util.MecatorProjection;
import util.Tree;

public class Controller implements Initializable, ControllerInterface{
    

    ObservableList<String> style = FXCollections.observableArrayList(
        "default", "dark", "gray scale");

    @FXML private Button menuButton1;
    @FXML private Button menuButton2;
    @FXML private Button menuButton3;
    @FXML private Button layerButton;
    @FXML private Button searchButton;
    @FXML private Button pointButton;
    @FXML private Pane leftBurgerMenu;
    @FXML private ComboBox<String> searchBarStart;
    @FXML private ComboBox<String> searchBarDestination;
    @FXML private Button mainMenuButton;
    @FXML private VBox mainMenuVBox;
    @FXML private VBox graphicVBox;
    @FXML private HBox mainUIHBox;
    @FXML private BorderPane mainBorderPane;
    @FXML private ChoiceBox<String> styleChoiceBox;
    @FXML private ImageView pointImage;


    //private Image activePoint = new Image(getClass().getResourceAsStream("gui/resources/visual/pointofinterest.png"));

    //System.out.println("HELO")
    //src\main\java\gui\pointofinterestoff.png


    private boolean pointofInterestState = false;
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

        mainView.canvas.setOnMouseClicked(e -> {
            if (pointofInterestState){


               DrawingMap drawingMap = mainView.getDrawingMap();

               float currentY =  (float) e.getY() ;
               float currentX =  (float) e.getX() ;

               double[] bounds = drawingMap.getScreenBounds();
              
               double x = bounds[0] + e.getX() / drawingMap.getZoomLevel();
               double y = bounds[1] - e.getY() / drawingMap.getZoomLevel();
       

                Point2D clickedPoint = mainView.getDrawingMap().getTransform().transform(currentX, currentY);

                TagNode pointofInterest = new TagNode((float) y, (float) x);

                mainView.getDrawingMap().setMarkedTag(pointofInterest);

                mainView.draw();
            }
            
        
        });
        
        mainView.canvas.setOnScroll(event -> {

            if (System.currentTimeMillis() - timer > 500){
                timer = System.currentTimeMillis();
            }

            mainView.getDrawingMap().zoom(Math.pow(zoomMultiplier,event.getDeltaY()), event.getX(), event.getY());

            
            
        });

        mainView.canvas.setOnMouseDragged(e -> {

            if (!pointofInterestState){
                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                mainView.getDrawingMap().pan(dx, dy);
    
                lastX = e.getX();
                lastY = e.getY();
            }

           
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
        pointButton.setOnAction((ActionEvent e) ->{
            pointofInterestState = !pointofInterestState;

            File filePassive = new File(System.getProperty("user.dir").toString() + "\\src\\main\\resources\\visuals\\pointofinterestpassive.png");
            File fileActive = new File(System.getProperty("user.dir").toString() + "\\src\\main\\resources\\visuals\\pointofinterest.png");


            Image imagePassive = new Image(filePassive.toURI().toString());
            Image imageActive = new Image(fileActive.toURI().toString());
            //Image otherImage = new Image(getClass().getResourceAsStream(selectedEndItem));
            if (pointofInterestState){
                pointImage.setImage(imageActive);
            } else{
                pointImage.setImage(imagePassive);
            }
        });

        // TODO: remake this function so it registers everytime the value is changed!
        
        searchBarStart.valueProperty().addListener((observable, oldValue, newValue) -> {
            search(newValue, true);
        });

        searchBarStart.setOnAction((ActionEvent e) -> {
            //System.out.println("selected: " + searchBarStart.getSelectionModel().getSelectedItem());
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
        //System.out.println("addressObj: " + addressObj.toString());
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
        DrawingMap drawingMap = mainView.getDrawingMap();

        double[] bounds = drawingMap.getScreenBounds();
        double x = ((bounds[2] - bounds[0]) / 2) + bounds[0];
        double y = ((bounds[1] - bounds[3]) / 2) + bounds[1];

        //drawingMap.getTransform().determinant()
        Point2D pointCenter = drawingMap.getTransform().transform(x, y);
        TagAddress tagAddress = s.getTagAddressByAddress(addressObj);
        System.out.println(tagAddress.getMunicipality() + " " + tagAddress.getCity() + " " + tagAddress.getStreet() + " " + tagAddress.getHouseNumber());
        Point2D point = drawingMap.getTransform().transform(tagAddress.getLon(), tagAddress.getLat());
        double deltaX = point.getX() - pointCenter.getX();
        double deltaY = point.getY() - pointCenter.getY();
        /*System.out.println( "X: CENTER: " + x + ", TAGADRESS: " + tagAddress.getLon());
        System.out.println( "Y: CENTER: " + y + ", TAGADRESS: " + tagAddress.getLat());
        System.out.println( "POINT: X: " + point.getX() + ", Y: " + point.getY());
        System.out.println( "CENTERPOINT: X: " + pointCenter.getX() + ", Y: " + pointCenter.getY());
        System.out.println( "TRANSFORM: TX: " + drawingMap.getTransform().getTx() + ", TY: " + drawingMap.getTransform().getTy());


        System.out.println( "DELTA: X: " + deltaX + ", Y: " + deltaY);*/



        edu.princeton.cs.algs4.Point2D point2d = new edu.princeton.cs.algs4.Point2D(tagAddress.getLon(), tagAddress.getLat());


        edu.princeton.cs.algs4.Point2D nearest = Tree.getNearestPoint(point2d);

        ArrayList<Tag> nearestTag = Tree.getTagsFromPoint(nearest);

        //System.out.println(nearestTag.get(0).getId());

        Tag tag = nearestTag.get(0);

        if(tag instanceof TagWay && ((TagWay)tag).getType() != null && ((TagWay)tag).getType().equals(parser.Type.BUILDING)){

            drawingMap.setMarkedTag(nearestTag.get(0));
        } else{
            drawingMap.setMarkedTag(tagAddress);
        }

        mainView.getDrawingMap().pan(-deltaX, deltaY);


    }

}
