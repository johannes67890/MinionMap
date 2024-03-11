import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;

import java.io.File;
import java.util.stream.Collectors;

import javafx.event.*;

public class MainView {

    enum StageSelect {

        MainMenu,
        MapView

    }

    public static Stage stage;
    static StageSelect selectedStage = StageSelect.MapView;
    static int sizeX = 800;
    static int sizeY = 600;
    public static Canvas canvas = new Canvas(sizeX, sizeY);
    static GraphicsContext gc = canvas.getGraphicsContext2D();


    public MainView(Stage stage){

        MainView.stage = stage;

        MainView.stage.setMinWidth(sizeX);
        MainView.stage.setMinHeight(sizeY);

        mapStage(stage);
        /*
        if (selectedStage == StageSelect.MainMenu){
            dragAndDropStage(stage);
        }else if (selectedStage == StageSelect.MapView){
            mapStage(stage);
        }
        */
        
    }


    public static void dragAndDropStage(Stage stage){

        Label title = new Label("Welcome to Map of Denmark!");
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        Label description = new Label("Drag and drop a .osm file here (If no file is dropped when submitting then it loads default map)");
        description.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        description.setWrapText(true);
        description.setTextAlignment(TextAlignment.CENTER);
        
        Text text = new Text("NameOFFiel");
        Pane contentPane = new Pane(text);
        contentPane.setMinSize(sizeX / 5, sizeY / 5);
        contentPane.setMaxWidth(sizeX / 3);
        contentPane.setStyle("-fx-background-color: #FF0000");

        Button submitButton = new Button("Submit");
        submitButton.setMinSize(sizeX / 10, sizeY / 20);
        VBox outerBox = new VBox();
        outerBox.setSpacing(10);
        outerBox.setAlignment(Pos.CENTER);
        outerBox.getChildren().addAll(title, description, contentPane, submitButton);
        
        

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
                    text.setText(db.getFiles().toString());
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
                System.out.println(selectedStage);
            }
        });

    }

    public static void mapStage(Stage stage){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        GridPane mainGrid = new GridPane();
        GridPane rightGrid = new GridPane();
        Scene scene = new Scene(mainGrid, screenBounds.getWidth(), screenBounds.getHeight());
        

        Button menuButton = new Button("Menu");
        Button searchButton = new Button("Search");
        TextField searchBar = new TextField();
        HBox topBar = new HBox(10);
        topBar.getChildren().addAll(menuButton, searchBar, searchButton);
        topBar.setPrefSize(scene.getWidth(), screenBounds.getHeight() * 0.05f);
        searchBar.setMinWidth(sizeX * 0.8f);
        topBar.setStyle("-fx-background-color: #00FFFF;");
        topBar.setAlignment(Pos.CENTER);
        rightGrid.setPrefSize(scene.getWidth(), scene.getHeight());
        mainGrid.add(rightGrid, 1,0);
        rightGrid.add(topBar, 0,0);
        rightGrid.add(canvas, 0,1);
        rightGrid.setVgap(10);

        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, sizeX, sizeY);

        gc.beginPath();
        gc.moveTo(0, 0);
        gc.lineTo(sizeY, sizeX);
        gc.stroke();
        gc.closePath();

        

        
        
        //Button 0,0

        //Bjælke ned fra knap samme farve som #button

        //Searchbar textfield ish, bare print 



        stage.setTitle("Danmarks Kortet Uden malmø");
        stage.setScene(scene);
        stage.show();
    }


    public static void redraw(){

        if (selectedStage == StageSelect.MainMenu){
            dragAndDropStage(stage);
        }else if (selectedStage == StageSelect.MapView){
            mapStage(stage);
        }
    }
}