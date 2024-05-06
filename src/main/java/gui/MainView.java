


package gui;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The MainView class is the main view of the application
 * It is responsible for drawing the main menu (Lobbyview.java) and the map view (MapView.java)
 * It also initializes the stage and the scene for the application
 */
public class MainView {

    enum StageSelect {

        MainMenu,
        MapView

    }

    private static Stage stage;
    static StageSelect selectedStage = StageSelect.MainMenu;
    static int sizeX = 800;
    static int sizeY = 600;
    static GraphicsContext gc;
    static Scene lastScene;
    static Rectangle2D screenBounds;
    public DrawingMap drawView;

    /**
     * Constructor for the MainView class
     * Initializes stage, screenBounds and draws the main menu
     * @param stage the Stage object
     */
    public MainView(Stage stage){
        MainView.stage = stage;
        screenBounds = Screen.getPrimary().getVisualBounds();
        MainView.stage.setMinWidth(sizeX);
        MainView.stage.setMinHeight(sizeY);
        MainView.stage.setResizable(true);

        drawScene(StageSelect.MainMenu);
    }

    /**
     * Getter for the stage
     * @return the Stage object
     */
    public Stage getStage(){
        return MainView.stage;
    }

    /**
     * Determines which scene to show
     * @param selected the StageSelect object
     */
    protected void drawScene(StageSelect selected){
        selectedStage = selected;
        View view;
        try{
            if (selectedStage == StageSelect.MainMenu){
                view = new LobbyView(this);
                stageInitializer(view.getScene());
            }else if(selectedStage == StageSelect.MapView){ // else its mapView
                view = new MapView(this);
                stageInitializer(view.getScene());
                ((MapView) view).initializeDrawingMap();
            } else {
                System.out.println(selectedStage + " is not a part of the possible stages to show");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initializes the stage with the scene
     * @param scene the Scene object
     */
    protected void stageInitializer(Scene scene){
        stage.setScene(scene);
        stage.show();
    }


}