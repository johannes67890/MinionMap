package GUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.parser.TagBound;
import org.parser.TagNode;
import org.parser.TagWay;
import org.parser.XMLReader;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class DrawingMap {


    static Affine transform = new Affine();
    public ResizableCanvas canvas;
    private XMLReader reader;
    private MainView mainView;


    public DrawingMap(MainView mainView, XMLReader reader){

        this.mainView = mainView;
        this.reader = reader;

        
    }

    public void initialize(ResizableCanvas canvas){

        this.canvas = canvas;

        TagBound bound = reader.getBound();

        double minlon = bound.getMinLon().doubleValue();
        double maxlat = bound.getMaxLat().doubleValue();
        double maxlon = bound.getMaxLon().doubleValue();
        double minlat = bound.getMinLat().doubleValue();

        //System.out.println(canvas.getHeight());

        System.out.println("ZOOM LEVEL " + canvas.getHeight() / (maxlat - minlat));

        pan(-minlon, -minlat);
        zoom(canvas.getWidth() / (maxlon - minlon), 0, 0);

        DrawMap(canvas.getGraphicsContext2D(), canvas);
    }

    public void DrawMap(GraphicsContext gc, ResizableCanvas canvas){

        
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        List<TagNode> nodes = reader.getNodes();
        HashMap<Long, TagNode> nodesMap = reader.getNodesMap();
        ArrayList<TagWay> ways = reader.getWays();
        TagBound bound = reader.getBound();

        Iterator<TagWay> it = ways.iterator();
        gc.setTransform(transform);
        gc.setFill(Color.WHITE);
        gc.setLineWidth(1/Math.sqrt(transform.determinant()));
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        
        while (it.hasNext()) {

            ArrayList<Long> nodesRef =  it.next().getNodes();
            ArrayList<Double> nodesX = new ArrayList<>();
            ArrayList<Double> nodesY = new ArrayList<>();

            gc.beginPath();


            gc.moveTo(nodesMap.get(nodesRef.get(0)).getLonDouble(), nodesMap.get(nodesRef.get(0)).getLatDouble());
            for (Long ref : nodesRef){
                //nodesX.add(nodesMap.get(ref).getLonDouble());
                //nodesY.add(nodesMap.get(ref).getLatDouble());
                
                gc.lineTo(nodesMap.get(ref).getLonDouble(), nodesMap.get(ref).getLatDouble());
            }

            gc.stroke();



            //System.out.println("HELLO " + line.size());




            /*for(int i = 2 ; i < line.size(); i+=2){

                gc.lineTo(line.get(i-1), line.get(i));
                System.out.println(line.get(i));

            }*/

            
                
        }
        
        gc.strokeLine(0, 0, 100, 100);
        gc.strokeLine(0, canvas.getHeight(), canvas.getWidth(), 0);

    }

    void zoom(double factor, double dx, double dy){
        pan(-dx, -dy);
        transform.prependScale(factor, factor);
        //transform.prependTranslation(dx,dy);
        pan(dx, dy);
        mainView.draw();
        //System.out.println("ZOOMING");
        
    }


    public void pan(double dx, double dy) {

        transform.prependTranslation(dx, dy);
        mainView.draw();
    }




}
