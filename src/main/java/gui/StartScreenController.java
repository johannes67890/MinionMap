package gui;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import util.FileDistributer;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
/**
 * The StartScreenController class is responsible for handling user input on the start screen
 * It is a subclass of the ControllerInterface class
 */
public class StartScreenController implements Initializable, ControllerInterface{

    @FXML private Button submitButton;
    @FXML private Button openFileFinderButton;
    @FXML private HBox contentPane;
    @FXML private Text contentPaneText;
    @FXML private Button welcomeCopenhagen;
    @FXML private Button welcomeBornholm;

    private LobbyView lobbyView;
    private File droppedFile;
    // List of accepted file types
    private ArrayList<String> listOfAcceptedTypes = new ArrayList<>(){{
        add("*.osm");
        add("*.zip");
        add("*.xml");
        add("*.bin");
    }};
    /**
     * Starts the controller
     * @param view - the view object to be associated with the controller
     */
    public void start(View view){
        this.lobbyView = (LobbyView) view;
    }

    /**
     * Initializes the controller
     * @param location the URL location
     * @param resources the ResourceBundle resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        openFileFinderButton.setOnAction((ActionEvent e) -> {
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("osm,xml,zip", listOfAcceptedTypes)
            );
            File file = fileChooser.showOpenDialog(lobbyView.getStage());
            if (file != null) {
                droppedFile = file;
                contentPaneText.setText(droppedFile.getAbsolutePath());
            }

        });

        submitButton.setOnAction((ActionEvent e) -> {
            File finalFile;
            if (droppedFile != null){
                finalFile = droppedFile;
            }else{
                finalFile = new File(FileDistributer.testMap.getFilePath());
            }
            lobbyView.initializeMap(finalFile);
        });

        welcomeCopenhagen.setOnAction((ActionEvent e) -> {
            File finalFile = new File(FileDistributer.copenhagenZip.getFilePath());
            lobbyView.initializeMap(finalFile);
        });

        welcomeBornholm.setOnAction((ActionEvent e) -> {
            File finalFile = new File(FileDistributer.bornholmZip.getFilePath());
            lobbyView.initializeMap(finalFile);
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
                    contentPaneText.setText(db.getFiles().toString());
                    droppedFile = db.getFiles().get(0);
                    success = true;
                }

                /* let the source know whether the string was successfully 
                 * transferred and used */

                event.setDropCompleted(success);

                event.consume();
            }
        });

    }
}