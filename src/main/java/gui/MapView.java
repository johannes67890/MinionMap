package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import parser.Model;
import util.FileDistributer;

import java.net.URL;

public class MapView extends View{

    private GraphicsContext gc;
    private ResizableCanvas c;
    private Pane p;

    public MapView(MainView main) throws Exception {
        super(main, new URL("file:" + FileDistributer.main.getFilePath()));
        setResizeableCanvas();
        setPane(this.c);
        setDrawingMap();
        super.initializeView();
    }

    public void draw(){
        this.gc = getResizeableCanvas().getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.fillRect(0, 0, getResizeableCanvas().getWidth(), getResizeableCanvas().getHeight());
        getDrawingMap().DrawMap(getResizeableCanvas());

    }
    public void initializeDrawingMap(){
        getDrawingMap().initialize(getResizeableCanvas());
    }
    @Override
    public DrawingMap getDrawingMap(){
        return super.getDrawingMap();
    }
    public void setDrawingMap() {
        super.setDrawingMap(new DrawingMap(this, super.getModel()));
    }

    public ResizableCanvas getResizeableCanvas(){
        return this.c;
    }
    public Pane getPane(){
        return this.p;
    }

    public void drawScene(){
        super.drawScene(false);
    }

    private void setPane(ResizableCanvas rC){
        this.p = new Pane(rC);
    }
    private void setResizeableCanvas(){
        c = new ResizableCanvas(this);
    }

}
