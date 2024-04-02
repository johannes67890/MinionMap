package GUI;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;

public class ResizableCanvas extends Canvas {
 
    private MainView mainView;

    public ResizableCanvas(MainView mainView) {
        this.mainView = mainView;
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
        
    }

    public void draw() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        double width = getWidth();
        double height = getHeight() - (screenBounds.getHeight() * 0.05f);

        GraphicsContext gc = getGraphicsContext2D();
        mainView.draw();
        
        //mainView.firstDraw();
        //gc.clearRect(0, 0, width, height);

        /*gc.setStroke(Color.RED);
        gc.strokeLine(0, 0, width, height);
        gc.strokeLine(0, height, width, 0);*/
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
    
}
