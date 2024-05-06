package gui;

import javafx.stage.Stage;
import parser.Model;
import util.FileDistributer;
import util.FileHandler;

import java.io.File;
import java.net.URL;

/**
 * The LobbyView class is responsible for drawing the main menu of the application
 * It is a subclass of the View class
 */

public class LobbyView extends View{
    /**
     * Constructor for the LobbyView class
     * @param main the MainView object
     * @throws Exception if the FXML file is not found
     */
    public LobbyView(MainView main) throws Exception {
        super(main, new URL("file:" + FileDistributer.start_screen.getFilePath()));
        super.initializeView();
    }

    /**
     * Getter for the stage
     * Calls the getStage function from the View class
     * @return the Stage object
     */
    public Stage getStage() {
        return super.getStage();
    }

    /**
     * Initializes the map
     * Calls the loadModel function from the View class
     * @param file the File object
     */
    protected void initializeMap(File file) {
        loadModel(file);
        drawScene();
    }

    /**
     * Loads the model object from a file
     * Calls the setModel function from the View class
     * @param file the File object
     */
    public void loadModel(File file) {
        super.setModel((Model) FileHandler.getModel(file));
    }

    /**
     * Calls the drawScene function inside the View class
     * Which draws the scene
     */
    private void drawScene(){
        super.drawScene(true);
    }
}
