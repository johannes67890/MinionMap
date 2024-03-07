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

    Stage stage;
    static StageSelect selectedStage = StageSelect.MapView;
    static int sizeX = 800;
    static int sizeY = 800;
    static Canvas canvas = new Canvas(800, 800);
    static GraphicsContext gc = canvas.getGraphicsContext2D();


    public MainView(Stage stage){

        this.stage = stage;


        if (selectedStage == StageSelect.MainMenu){
            dragAndDropStage(stage);
        }else if (selectedStage == StageSelect.MapView){
            mapStage(stage);
        }
        
    }

    public static void testStage(Stage stage){

        BorderPane pane = new BorderPane(canvas);
        TextArea inputField = new TextArea();
        Text inputFieldTitle = new Text();
        inputFieldTitle.setText("Adress:");
        Button searchButton = new Button();
        searchButton.setText("Search!");

        GridPane topgp = new GridPane();
        topgp.setPadding(new Insets(25,25,25,25));
        GridPane gp = new GridPane();

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        topgp.setMinHeight(screenBounds.getHeight() * 0.05f);
        topgp.setMaxHeight(screenBounds.getHeight() * 0.05f);
        
        topgp.add(inputFieldTitle, 0, 0);
        topgp.add(inputField, 1,0);
        topgp.add(searchButton, 2,0);

        gp.add(topgp, 0, 0);
        gp.add(pane, 0, 1);
        
        
        Scene scene = new Scene(gp);
        stage.setTitle("Kinky Fætter");
        stage.setScene(scene);
        stage.show();

        gc.setTransform(new Affine());
        gc.setFill(Color.RED);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

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
        GridPane mainGrid = new GridPane();

        GridPane leftGrid = new GridPane();

        Scene scene = new Scene(mainGrid, sizeX, sizeY);

        Button menuButton = new Button("Kliktest");
        Button searchButton = new Button("Search");
        TextField searchBar = new TextField();

        mainGrid.add(leftGrid, 0,1);

        leftGrid.add(menuButton,0,0);

        leftGrid.add(searchBar,0,1);

        leftGrid.add(searchButton,0,2);

        
        
        //Button 0,0

        //Bjælke ned fra knap samme farve som #button

        //Searchbar textfield ish, bare print 



        stage.setTitle("Danmarks Kortet Uden malmø");
        stage.setScene(scene);
        stage.show();
    }
}