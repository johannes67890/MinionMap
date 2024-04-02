package GUI;

public class Controller {
    double lastX;
    double lastY;

    double zoomMultiplier = 1.01f;
    
    public Controller(MainView mainView){

        //System.out.println("CONTROLLER MADE");

        mainView.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });
        
        mainView.canvas.setOnScroll(event -> {

            //System.out.println("ZOOMING IN CONTROLLER");

            mainView.getDrawingMap().zoom(Math.pow(zoomMultiplier,event.getDeltaY()), event.getX(), event.getY());
            
        });

        mainView.canvas.setOnMouseDragged(e -> {

            double dx = e.getX() - lastX;
            double dy = e.getY() - lastY;
            mainView.getDrawingMap().pan(dx, dy);

            lastX = e.getX();
            lastY = e.getY();
        });
    }
}
