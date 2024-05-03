package gui;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.GraphicsHandler.GraphicStyle;
import gui.MainView.StageSelect;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import parser.TagNode;
import parser.TagWay;
import structures.KDTree.Point3D;
import structures.KDTree.Tree;
import util.TransportType;

public class Controller implements Initializable, ControllerInterface{
    

    ObservableList<String> style = FXCollections.observableArrayList(
        "default", "dark", "gray scale");

    @FXML private Button menuButton1;
    @FXML private Button menuButton2;
    @FXML private Button menuButton3;
    @FXML private Button layerButton;
    @FXML private Button searchButton;
    @FXML private Button pointButton;
    @FXML private Button routeButton;
    @FXML private Pane leftBurgerMenu;

    @FXML private Pane routeTypeMenu;
    @FXML private Button walkButton;
    @FXML private Button bicycleButton;
    @FXML private Button carButton;

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
    private ObservableList<String> searchList = FXCollections.observableArrayList();

    private List<TagAddress> addresses = new ArrayList<>();

    private TagAddress startAddress = null;
    private TagAddress endAddress = null;
    private Search s;
    private boolean hasSearchedForPath = false;

    TransportType routeType = TransportType.CAR;

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
        s = new Search(mw);
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
                ArrayList<Tag> temp = new ArrayList<>();
                temp.add(pointofInterest);

                mainView.getDrawingMap().setMarkedTag(temp);

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


        setEnableDestinationComboBox(false);
        ComboBoxListViewSkin<String> comboBoxListViewSkinStart = new ComboBoxListViewSkin<String>(searchBarStart);
        ComboBoxListViewSkin<String> comboBoxListViewSkinDestination = new ComboBoxListViewSkin<>(searchBarDestination);


        mainMenuVBox.setVisible(false);
        leftBurgerMenu.setVisible(false);
        graphicVBox.setVisible(false);
        routeTypeMenu.setVisible(false);

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


        routeButton.setOnAction((ActionEvent e) -> {
            
            setEnableDestinationComboBox(!searchBarDestination.isVisible());
            routeTypeMenu.setVisible(!routeTypeMenu.isVisible());

            
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

        walkButton.setOnAction((ActionEvent e) -> {
            routeType = TransportType.FOOT;
        });

        bicycleButton.setOnAction((ActionEvent e) ->{
            routeType = TransportType.BIKE;
        });

        carButton.setOnAction((ActionEvent e) ->{
            routeType = TransportType.CAR;
        });


        comboBoxListViewSkinStart.getPopupContent().addEventFilter(KeyEvent.ANY, (event) -> {
            if( event.getCode() == KeyCode.SPACE ) {
                event.consume();
            }else if (event.getCode() == KeyCode.DOWN){
                event.consume();
            }else if (event.getCode() == KeyCode.UP){
                event.consume();
            }
        });
        comboBoxListViewSkinDestination.getPopupContent().addEventFilter(KeyEvent.ANY, (event) -> {
            if( event.getCode() == KeyCode.SPACE ) {
                event.consume();
            }else if (event.getCode() == KeyCode.DOWN){
                event.consume();
            }else if (event.getCode() == KeyCode.UP){
                event.consume();
            }
        });
        searchBarStart.setSkin(comboBoxListViewSkinStart);
        searchBarDestination.setSkin(comboBoxListViewSkinDestination);

        searchBarStart.setOnAction((ActionEvent e) ->{
            startAddress  = comboBoxAddress(searchBarStart);
            if (startAddress == null){
                return;
            }
            if (endAddress == null){
                showAddress(startAddress);
            }else{
                if (!hasSearchedForPath){
                    hasSearchedForPath = true;
                    s.pathfindBetweenTagAddresses(startAddress, endAddress, routeType);
                }
            }

        });

        searchBarDestination.setOnAction((ActionEvent e) ->{
            
            endAddress = comboBoxAddress(searchBarDestination);
            if (endAddress == null){
                return;
            }
            if (startAddress == null){
                showAddress(endAddress);
            }else{
                if (!hasSearchedForPath){
                    hasSearchedForPath = true;
                    s.pathfindBetweenTagAddresses(startAddress, endAddress, routeType);
                }
            }

        });

        
        searchBarStart.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            showSuggestions(searchBarStart, oldValue, newValue);            
        });

        searchBarDestination.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            showSuggestions(searchBarDestination, oldValue, newValue);
        });
    }

    private void showSuggestions(ComboBox<String> searchBar, String oldValue, String newValue){
        if (searchBar.isFocused()){
            search(newValue, searchBar);
        }

        if (newValue.isEmpty() && !oldValue.isEmpty() && oldValue.length() > 1){
            searchBar.getEditor().textProperty().setValue(oldValue);
            searchBar.hide();
        }
    }

    private void setEnableDestinationComboBox(boolean isEnabled){
        if (isEnabled){
            searchBarDestination.setMaxWidth(1000000);
        }else{
            searchBarDestination.setMaxWidth(0);
        }
        searchBarDestination.setVisible(isEnabled);
    }

    private TagAddress comboBoxAddress(ComboBox<String> searchBar){
        String string = searchBar.getEditor().textProperty().getValue();

            String[] split = string.split(",");

            if (split.length == 3){
                string = split[0] + split[2];

                TagAddress address = s.getAddress(string, split[1]);
                return address;
            }
            return null;
    }

    private void search(String address, ComboBox<String> searchBar) {
        
        if (startAddress != null && searchBar.getEditor().textProperty().getValue().equals(startAddress.toString())) return;
        if (endAddress != null && searchBar.getEditor().textProperty().getValue().equals(endAddress.toString())) return;
        if (!address.isEmpty() && address.charAt(address.length() - 1) != ' ') {
            ArrayList<TagAddress> tagAddresses = s.getSuggestions(address);
            if (searchBar.equals(searchBarStart) && startAddress != null){
                startAddress = null;
            }else if (searchBar.equals(searchBarDestination) && endAddress != null){
                endAddress = null;
            }
            hasSearchedForPath = false;
            // Update UI on JavaFX Application Thread
            Platform.runLater(() -> {
                synchronized (searchList) {
                    if (!searchBar.getItems().isEmpty()) {
                        searchBar.getItems().clear();
                        addresses.clear();
                    }
                    for (TagAddress tagAddress : tagAddresses) {
                        searchList.add(tagAddress.toString());
                        addresses.add(tagAddress);
                    }
                    searchBar.getItems().setAll(searchList);
                    searchList.clear();
                    if (!searchBar.isShowing() && searchBar.getItems().size() > 0){
                        searchBar.show();
                    }
                }
            });
        }
    }

    private void showAddress(TagAddress tagAddress){
        DrawingMap drawingMap = mainView.getDrawingMap();

        float[] bounds = drawingMap.getScreenBounds();
        double x = ((bounds[2] - bounds[0]) / 2) + bounds[0];
        double y = ((bounds[3] - bounds[1]) / 2) + bounds[1];

        //drawingMap.getTransform().determinant()
        Point2D pointCenter = drawingMap.getTransform().transform(x, y);
        Point2D point = drawingMap.getTransform().transform(tagAddress.getLon(), tagAddress.getLat());
        double deltaX = point.getX() - pointCenter.getX();
        double deltaY = point.getY() - pointCenter.getY();
       
        Point3D point2d = new Point3D(tagAddress.getLon(), tagAddress.getLat(), (byte) 8);

        Point3D nearest = Tree.getNearestPoint(point2d);

        ArrayList<Tag> nearestTag = Tree.getTagsFromPoint(nearest);

        Tag tagToDraw = null;
        for (Tag tag : nearestTag){

            if (tag instanceof TagWay && ((TagWay)tag).getType() != null && ((TagWay)tag).getType().equals(util.Type.BUILDING)){
                tagToDraw = tag;
                break;
            }
        }

        ArrayList<Tag> temp = new ArrayList<>();
        temp.add(tagAddress);
        drawingMap.setMarkedTag(temp);

        mainView.getDrawingMap().pan(-deltaX, deltaY);


    }

}
