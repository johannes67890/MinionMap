package GUI;

public class Controller {
    double lastX;
    double lastY;

    double zoomMultiplier = 1.05f;
    
    public Controller(MainView mainView){

        if (mainView.canvas != null){
            mainView.canvas.setOnScroll(event -> {

                mainView.getDrawingMap().zoom(Math.pow(zoomMultiplier,event.getDeltaY()), event.getX(), event.getY());
                
            });

        }


       
       

    }
}