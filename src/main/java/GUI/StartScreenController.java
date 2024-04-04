package GUI;
import java.net.URL;
import java.util.ResourceBundle;

import GUI.MainView.StageSelect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class StartScreenController implements Initializable, ControllerInterface{

    @FXML private Button submitButton;
    @FXML private HBox contentPane;
    @FXML private Text contentPaneText;

    private static MainView mainView;

    public void start(MainView mw){
        mainView = mw;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        submitButton.setOnAction((ActionEvent e) -> {
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