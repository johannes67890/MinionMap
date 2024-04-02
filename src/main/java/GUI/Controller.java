package gui;

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

<<<<<<< HEAD
=======

            //System.out.println("ZOOMING IN CONTROLLER");

>>>>>>> 72c9a6e5d383d6c40a5502023a4873bef52eac75
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
