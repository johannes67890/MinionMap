package gui;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

    public MainView(Stage stage){
        MainView.stage = stage;
        screenBounds = Screen.getPrimary().getVisualBounds();
        MainView.stage.setMinWidth(sizeX);
        MainView.stage.setMinHeight(sizeY);
        MainView.stage.setResizable(true);

        drawScene(StageSelect.MainMenu);
    }

    public Stage getStage(){
        return MainView.stage;
    }

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

    protected void stageInitializer(Scene scene){
        stage.setScene(scene);
        stage.show();
    }


}