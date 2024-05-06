package gui;
import javafx.scene.canvas.Canvas;

/**
 * The ResizableCanvas class is responsible for resizing the canvas
 * It is a subclass of the Canvas class and has a constructor that takes a MapView object as a parameter
 * This subclass makes it possible for the canvas to resize with the window
 * Updated: 06/05/2024
 */

public class ResizableCanvas extends Canvas {
    
    private MapView mapView;

    /**
     * Constructor for the ResizableCanvas class
     * @param mapView the MapView object
     */
    public ResizableCanvas(MapView mapView) {
        this.mapView = mapView;
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }
    
    /**
     * Calls the draw function in the MapView class
     */
    public void draw() {
        this.mapView.draw();
    }

    // The only reason that the canvas can resize with the window, besides the listeners
    @Override
    public boolean isResizable() {
        return true;
    }

    // Overrides the prefWidth function in super class Canvas, makes it possible to dynamically change the width of the canvas
    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    // Overrides the prefHeight function in super class Canvas, makes it possible to dynamically change the height of the canvas
    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    // Overrides the minHeight function in super class Canvas, makes it possible to dynamically change the height of the canvas
    @Override
    public double maxHeight(double width) {
        return Double.POSITIVE_INFINITY;
    }

    // Overrides the maxWidth function in super class Canvas, makes it possible to dynamically change the height of the canvas
    @Override
    public double maxWidth(double height) {
        return Double.POSITIVE_INFINITY;
    }
    
}
