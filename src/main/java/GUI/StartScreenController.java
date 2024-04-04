package gui;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import util.FileDistributer;
import parser.XMLReader;

import gui.MainView.StageSelect;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class StartScreenController implements Initializable, ControllerInterface{

    @FXML private Button submitButton;
    @FXML private Button openFileFinderButton;
    @FXML private HBox contentPane;
    @FXML private Text contentPaneText;

    private static MainView mainView;
    private File droppedFile;
    private ArrayList<String> listOfAcceptedTypes = new ArrayList<>(){{
        add("*.osm");
        add("*.zip");
        add("*.xml");
    }};

    public void start(MainView mw){
        mainView = mw;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        openFileFinderButton.setOnAction((ActionEvent e) -> {
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("osm,xml,zip", listOfAcceptedTypes)
            );
            File file = fileChooser.showOpenDialog(mainView.stage);
            if (file != null) {
                droppedFile = file;
                contentPaneText.setText(droppedFile.getAbsolutePath());
            }
            
        });

        submitButton.setOnAction((ActionEvent e) -> {
            if (droppedFile != null){
                mainView.loadXMLReader(droppedFile.getAbsolutePath());
            }else{
                mainView.loadXMLReader(FileDistributer.input.getFilePath());
            }
            mainView.drawScene(StageSelect.MapView);
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