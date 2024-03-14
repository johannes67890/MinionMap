

public class Controller {
    double lastX;
    double lastY;
    
    public Controller(MainView view){

        MainView.stage.widthProperty().addListener(observable -> MainView.redraw());
        MainView.stage.heightProperty().addListener(observable -> MainView.redraw());

    }
}


/*
    public Controller(Model model, View view) {
        view.canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });
        view.canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
                Point2D lastmodel = view.mousetoModel(lastX, lastY);
                Point2D newmodel = view.mousetoModel(e.getX(), e.getY());
                model.add(lastmodel, newmodel);
                view.redraw();
            } else if (e.isSecondaryButtonDown()){
                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);
            }

            lastX = e.getX();
            lastY = e.getY();
        });
        view.canvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
        });
    }
    */