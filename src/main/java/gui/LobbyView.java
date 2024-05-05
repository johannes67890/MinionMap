package gui;

import javafx.stage.Stage;
import parser.Model;
import util.FileDistributer;
import util.FileHandler;

import java.io.File;
import java.net.URL;

public class LobbyView extends View{

    public LobbyView(MainView main) throws Exception {
        super(main, new URL("file:" + FileDistributer.start_screen.getFilePath()));
        super.initializeView();
    }


    public Stage getStage() {
        return super.getStage();
    }

    protected void initializeMap(File file) {
        loadModel(file);

        drawScene();
    }

    public void loadModel(File file) {
        try {
            super.setModel((Model) FileHandler.getModel(file));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void drawScene(){
        super.drawScene(true);
    }


}
