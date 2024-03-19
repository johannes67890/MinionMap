package GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Controller {
    double lastX;
    double lastY;
    
    public Controller(DrawingMap drawView, MainView mainView){

        

        mainView.canvas.setOnScroll(event -> {

            drawView.zoom(Math.pow(scrollMultiplier,event.getDeltaY()), event.getX(), event.getY());
            mainView.canvas.draw();
            
        });

    }
}