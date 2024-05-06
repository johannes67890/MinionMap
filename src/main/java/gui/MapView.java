package gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import util.FileDistributer;

import java.net.URL;

public class MapView extends View{
    private GraphicsContext gc;
    private ResizableCanvas c;
    private Pane p;

    /**
     * Constructor for the MapView class
     * @param main the MainView object
     * @throws Exception
     */
    public MapView(MainView main) throws Exception {
        super(main, new URL("file:" + FileDistributer.main.getFilePath()));
        setResizeableCanvas();
        setPane(this.c);
        setDrawingMap();
        super.initializeView();
    }

    /**
     * Draw the Drawingmap
     */
    public void draw(){
        if(!super.getDrawingMap().IsInitialized()) return;
        this.gc = getResizeableCanvas().getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.fillRect(0, 0, getResizeableCanvas().getWidth(), getResizeableCanvas().getHeight());
        getDrawingMap().DrawMap(getResizeableCanvas());
    }

    /**
     * Calls initialize function inside DrawingMap class
     */
    public void initializeDrawingMap(){
        getDrawingMap().initialize(getResizeableCanvas());
    }

    /**
     * Calls getDrawingMap function inside View class
     * @return the DrawingMap object
     */
    @Override
    public DrawingMap getDrawingMap(){
        return super.getDrawingMap();
    }

    /**
     * Calls setDrawingMap function inside View class
     */
    public void setDrawingMap() {
        super.setDrawingMap(new DrawingMap(this, super.getModel()));
    }

    /**
     * Getter for the ResizableCanvas object
     * @return the ResizableCanvas object
     */
    public ResizableCanvas getResizeableCanvas(){
        return this.c;
    }

    /**
     * Getter for the Pane object
     * @return the Pane object
     */
    public Pane getPane(){
        return this.p;
    }

    /**
     * Calls drawScene function inside View class
     * With the parameter boolean set to false
     * The parameter is used to determine which scene to draw
     */
    public void drawScene(){
        super.drawScene(false);
    }

    /**
     * Setter for the Pane object
     * @param rC the ResizableCanvas object used for the Pane object
     */
    private void setPane(ResizableCanvas rC){
        this.p = new Pane(rC);
    }

    /**
     * Setter for the ResizableCanvas
     * Initializes the ResizableCanvas object with the MapView object
     */
    private void setResizeableCanvas(){
        c = new ResizableCanvas(this);
    }

}
