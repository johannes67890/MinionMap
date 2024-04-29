package gui;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gui.GraphicsHandler.GraphicStyle;
import gui.MainView.StageSelect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import parser.Tag;
import parser.TagAddress;
import parser.TagAddress.SearchAddress;
import parser.TagNode;
import parser.TagWay;
import util.Point3D;
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
    @FXML private Label zoomLevelText;
    @FXML private ImageView zoomLevelImage;
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

                float[] bounds = drawingMap.getScreenBounds();
              
                double x = bounds[0] + e.getX() / drawingMap.getZoomLevel();
                double y = bounds[3] - e.getY() / drawingMap.getZoomLevel();
       

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

            mainView.getDrawingMap().zoombarUpdater(zoomLevelText, zoomLevelImage);
            
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



        ComboBoxListViewSkin<String> comboBoxListViewSkin = new ComboBoxListViewSkin<String>(searchBarStart);


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

        comboBoxListViewSkin.getPopupContent().addEventFilter(KeyEvent.ANY, (event) -> {
            if( event.getCode() == KeyCode.SPACE ) {
                event.consume();
            }
        });
        searchBarStart.setSkin(comboBoxListViewSkin);

        
        searchBarStart.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

            if (searchBarStart.isFocused()){
                search(newValue, true);
            }

            if (newValue.isEmpty() && !oldValue.isEmpty() && oldValue.length() > 1){
                searchBarStart.getEditor().textProperty().setValue(oldValue);
                searchBarStart.hide();
            }
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

    private void chooseDestination(String text, boolean isLeft){
        if (isLeft){
            showAddress(text);
        }else{
        }
    }


    private void search(String address, boolean isStart, boolean test){
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

    private void search(String address, boolean isStart){

        if (!address.isEmpty() && address.charAt(address.length() - 1) != ' '){
            ArrayList<TagAddress> tagAddresses = s.getSuggestions(address);
            if (!searchBarStart.getItems().isEmpty()){
                searchBarStart.getItems().clear();
            }
            for (TagAddress tagAddress : tagAddresses){
                searchBarStart.getItems().add(tagAddress.toString());
            }
            searchBarStart.show();
        }
    }

    private void showAddress(String address){
        SearchAddress addressObj = s.searchForAddress(address);
        DrawingMap drawingMap = mainView.getDrawingMap();

        float[] bounds = drawingMap.getScreenBounds();
        double x = ((bounds[2] - bounds[0]) / 2) + bounds[0];
        double y = ((bounds[1] - bounds[3]) / 2) + bounds[1];

        //drawingMap.getTransform().determinant()
        Point2D pointCenter = drawingMap.getTransform().transform(x, y);
        TagAddress tagAddress = s.getTagAddressByAddress(addressObj);
        System.out.println(tagAddress.getMunicipality() + " " + tagAddress.getCity() + " " + tagAddress.getStreet() + " " + tagAddress.getHouseNumber());
        Point2D point = drawingMap.getTransform().transform(tagAddress.getLon(), tagAddress.getLat());
        double deltaX = point.getX() - pointCenter.getX();
        double deltaY = point.getY() - pointCenter.getY();
       
        Point3D point2d = new Point3D(tagAddress.getLon(), tagAddress.getLat(), (byte) 0);


        Point3D nearest = Tree.getNearestPoint(point2d);

        ArrayList<Tag> nearestTag = Tree.getTagsFromPoint(nearest);


        if(nearestTag.get(0) instanceof TagWay && ((TagWay)nearestTag.get(0)).getType() != null && ((TagWay)nearestTag.get(0)).getType().equals(parser.Type.BUILDING)){

            drawingMap.setMarkedTag(nearestTag.get(0));
        } else{
            drawingMap.setMarkedTag(tagAddress);
        }

        mainView.getDrawingMap().pan(-deltaX, deltaY);


    }

}
