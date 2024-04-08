package gui;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import parser.XMLReader;
import util.FileDistributer;
import util.ZipHandler;
public class MainView {

    enum StageSelect {

        MainMenu,
        MapView

    }

    public static Stage stage;
    static StageSelect selectedStage = StageSelect.MapView;
    static int sizeX = 800;
    static int sizeY = 600;
    public static ResizableCanvas canvas;
    static GraphicsContext gc;
    static Scene lastScene;
    static Rectangle2D screenBounds;
    public XMLReader xmlReader;
    public DrawingMap drawView;
    private Text zoomLevelText;
    private File inputFile;


    public MainView(Stage stage){

        MainView.stage = stage;
        screenBounds = Screen.getPrimary().getVisualBounds();
        MainView.stage.setMinWidth(sizeX);
        MainView.stage.setMinHeight(sizeY);
        MainView.stage.setResizable(true);

        dragAndDropStage(stage);
    }

    public void draw(){
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        zoomLevelText.setText("" + Math.round(drawView.getZoomLevelMeters()) + "m");;
        drawView.DrawMap(gc, canvas);
    }



    public void dragAndDropStage(Stage stage){

        Label title = new Label("Welcome to our Map of Denmark!");
        //Label title = new Label(finalPath);
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        Label description = new Label("Drag and drop a .osm file here (If no file is dropped when submitting then it loads default map)");
        description.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        description.setWrapText(true);
        description.setTextAlignment(TextAlignment.CENTER);
        

        Text text = new Text("No File Selected");
        Hyperlink link = new Hyperlink("Select File");
        TextFlow textflow = new TextFlow(text, link);
        textflow.setTextAlignment(TextAlignment.CENTER);

        HBox contentPane = new HBox(textflow);
        contentPane.setAlignment(Pos.CENTER);
        contentPane.setMinSize(sizeX / 5, sizeY / 5);
        contentPane.setMaxWidth(sizeX / 3);
        contentPane.setStyle("-fx-background-color: #b3b3b3; -fx-background-radius: 18 18 18 18;");

        Button submitButton = new Button("Submit");
        submitButton.setMinSize(sizeX / 10, sizeY / 20);
        VBox outerBox = new VBox();
        outerBox.setSpacing(10);
        outerBox.setAlignment(Pos.CENTER);
        outerBox.getChildren().addAll(title, description, contentPane, submitButton);

        link.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                text.setText(pathFindFile());
                link.setText("Klik her for at vælge en anden fil");
            }
        });

        contentPane.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != contentPane
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        contentPane.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    inputFile = db.getFiles().get(0);
                    text.setText(db.getFiles().toString());
                    link.setText("");
                    success = true;
                }

                /* let the source know whether the string was successfully 
                 * transferred and used */

                event.setDropCompleted(success);

                event.consume();
            }
        });


        BorderPane bp = new BorderPane(outerBox);
        Scene scene = new Scene(bp, sizeX, sizeY);
        stage.setTitle("Danmarks Kortet");
        stage.setScene(scene);
        stage.show();

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                selectedStage = StageSelect.MapView;
                if (inputFile != null){
                    xmlReader = new XMLReader(inputFile.getAbsolutePath());
                }else{
                    xmlReader = new XMLReader(FileDistributer.input.getFilePath());
                }
                drawView = new DrawingMap(MainView.this, xmlReader);
                redraw();
            }
        });

    }

    public void mapStageNew(Stage stage){

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Creating the image used for the hamburger menu button
        ImageView menuButtonImage = new ImageView("file:src/main/resources/visuals/hamburber.png");
        menuButtonImage.setFitHeight(screenBounds.getHeight() * 0.03f);
        menuButtonImage.setPreserveRatio(true);

        // Creating the image used for the search button
        ImageView searchButtonImage = new ImageView("file:src/main/resources/visuals/oompaloop.png");
        searchButtonImage.setFitHeight(screenBounds.getHeight() * 0.02f);
        searchButtonImage.setPreserveRatio(true);

        // Instantiation of basic objects within the menu bar at the top of the screen
        Button menuButton = new Button("", menuButtonImage);
        Button searchButton = new Button("Search", searchButtonImage);
        TextField searchBar = new TextField();
        BorderPane topBar = new BorderPane();

        // Manually setting the size of the objects so that they align
        searchButton.setPrefHeight(screenBounds.getHeight()*0.03f);
        searchBar.setPrefHeight(screenBounds.getHeight()*0.03f);
        
        topBar.setLeft(menuButton);
        topBar.setMargin(menuButton, new Insets(5));
        topBar.setCenter(searchBar);
        topBar.setMargin(searchBar, new Insets(5));
        topBar.setRight(searchButton);
        topBar.setMargin(searchButton, new Insets(5));
        topBar.setPrefHeight(screenBounds.getHeight() * 0.05f);
        
        topBar.setAlignment(searchButton, Pos.CENTER);
        topBar.setAlignment(menuButton, Pos.CENTER);
        topBar.setStyle("-fx-background-color: #adadad");

        // Instantiate the main components of the stage
        canvas = new ResizableCanvas(this); // This is a custom canvas that can resize and is used to draw the map
        gc = canvas.getGraphicsContext2D();
        VBox burgerMenu = burgerMenuInstantiation(); // This is the menu that sits on the left side of the screen when menu button is pressed
        StackPane mainPane = new StackPane(); // This is the stackpane where the burgermenu and map is overlayed on top of eachother
        BorderPane bp = new BorderPane(); // This is the main part of the scene where the map is drawn and the top menu bar is
        BorderPane unitScale = zoomLevelInstantiation(); // This is the zoom level that shows the user what the distance is

        // Setting up the borderpane by adding topbar to the top of the pane and mapCanvas to the center
        bp.setTop(topBar);
        bp.setCenter(canvas);

        // Setting up additional settings for the borderpane
        bp.setMargin(topBar, new Insets(5));

        // I dont know why it needs the padding, but if it isnt there the bp is going out of the window at the top
        bp.setPadding(new Insets(55,-5,0,-5));

        mainPane.getChildren().addAll(bp, burgerMenu, unitScale);
        burgerMenu.setVisible(false);
        mainPane.setAlignment(burgerMenu, Pos.CENTER_LEFT);
        mainPane.setAlignment(unitScale, Pos.BOTTOM_RIGHT);

        canvas.heightProperty().bind(mainPane.heightProperty());
        canvas.widthProperty().bind(mainPane.widthProperty());
        Scene scene = new Scene(mainPane);

        stage.setTitle("Map of Denmark");
        stage.setScene(scene);
        stage.show();
        drawView.initialize(canvas);
        Controller controller = new Controller(this);


        // An eventhandler for the burgermenu button. Maybe this should be moved to the controller class
        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                burgerMenu.setVisible(true);
            }
        });


        // An eventhandler for the search button and search bar. See search() method for more information
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                // TODO: Unfinished
                //search(searchBar);
            }
        });

        searchBar.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                // TODO: Unfinished
                //search(searchBar);
            }
        });

    }

    public BorderPane zoomLevelInstantiation(){

        zoomLevelText = new Text("500m");
        zoomLevelText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, screenBounds.getHeight() * 0.01f));;
        ImageView unitScalerImage = new ImageView("file:src/main/resources/visuals/UnitRuler2.png");
        unitScalerImage.setFitHeight(screenBounds.getHeight() * 0.02f);
        unitScalerImage.setFitWidth(screenBounds.getWidth() * 0.04f);

        BorderPane outputPane = new BorderPane();
        outputPane.setMaxWidth(screenBounds.getWidth() * 0.04f);
        outputPane.setMaxHeight(screenBounds.getHeight() * 0.04f);
        outputPane.setTop(zoomLevelText);
        outputPane.setCenter(unitScalerImage);
        
        outputPane.setAlignment(zoomLevelText, Pos.CENTER);
        outputPane.setAlignment(unitScalerImage, Pos.CENTER);

        return outputPane;
    }

    public VBox burgerMenuInstantiation(){

        // Main object that is going to be returned
        VBox burgerMenu = new VBox(10);

        // Getting the screen resolution
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // The objects within the menu
        Label testText = new Label("Test");
        ImageView menuButtonImage = new ImageView("file:src/main/resources/visuals/hamburber.png");
        Button menuButton = new Button("", menuButtonImage);
        Button backButton = new Button("Upload new file");
        menuButtonImage.setFitHeight(screenBounds.getHeight() * 0.03f);
        menuButtonImage.setPreserveRatio(true);

        // Adding all objects to the burgerMenu
        burgerMenu.getChildren().addAll(menuButton, testText, backButton);

        // Setting additional settings
        burgerMenu.setMaxWidth(screenBounds.getWidth() * 0.2f);
        burgerMenu.setStyle("-fx-background-color: #9cc2ff;");
        burgerMenu.setPadding(new Insets(10));
        menuButton.setAlignment(Pos.CENTER_LEFT);

        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                burgerMenu.setVisible(false);
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                selectedStage = StageSelect.MainMenu;
                redraw();
            }
        });

        return burgerMenu;

    }


    /**
     * Function for finding a file path for chosen file
     * via a filechooser dialog window and unzipping to a give directory if the file is a zip file
     * @return String path to the file chosen
     */
    public static String pathFindFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Ressource File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("OSM Files", "*.osm"),
            new FileChooser.ExtensionFilter("ZIP", "*.zip")
            );
            fileChooser.setInitialDirectory(new java.io.File("C:\\Users\\"));
            java.io.File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
            if(file.toString().contains(".zip")) {
            ZipHandler zip = new ZipHandler();
                try {
                    zip.unzip(file.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.toString();
    }

    // A function for redrawing the stage when changing scenes
    public void redraw(){

        if (selectedStage == StageSelect.MainMenu){
            dragAndDropStage(stage);
        }else if (selectedStage == StageSelect.MapView){
            mapStageNew(stage);
            
        }
    }

    public DrawingMap getDrawingMap() {
        return drawView;
    }

    // A function for searching for an address. Called when the search button is pressed 
    // or when the enter key is pressed in the search bar.
    // The function makes a new AddressSearchPage object which takes the addresses from the XMLReader
    // and then uses the searchForAdress method to search for the address
    // See AddressSearchPage for more information

    // public void search(TextField searchBar){
    //     // TODO: Unfinished
    //     Search search = new Search(xmlReader.getAddresses());
    //     String text = searchBar.getText();
    //     search.searchForAdress(text);
    // }
    
}