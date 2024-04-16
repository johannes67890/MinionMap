package gui;
import javafx.scene.canvas.Canvas;

public class ResizableCanvas extends Canvas {
 
    private MainView mainView;

    public ResizableCanvas(MainView mainView) {
        this.mainView = mainView;
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }
    
    public void draw() {
        mainView.draw();
    }

    // The only reason that the canvas can resize with the window, besides the listeners
    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    @Override
    public double maxHeight(double width) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double maxWidth(double height) {
        return Double.POSITIVE_INFINITY;
    }
    
}
