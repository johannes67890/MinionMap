package GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Controller {
    double lastX;
    double lastY;

    double zoomMultiplier = 1.05f;
    
    public Controller(MainView mainView){

        

        mainView.canvas.setOnScroll(event -> {

            mainView.getDrawingMap().zoom(Math.pow(zoomMultiplier,event.getDeltaY()), event.getX(), event.getY());
            
        });

    }
}