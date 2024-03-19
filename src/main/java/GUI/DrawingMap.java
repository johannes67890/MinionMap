package GUI;
import java.util.ArrayList;
import java.util.Iterator;

import org.parser.TagBound;
import org.parser.TagNode;
import org.parser.TagWay;
import org.parser.XMLReader;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class DrawingMap {


    static Affine transform = new Affine();
    public static ResizableCanvas canvas;
    private XMLReader reader;
    private MainView mainView;


    public DrawingMap(MainView mainView, XMLReader reader){

        this.mainView = mainView;
        this.reader = reader;

    }

    public void DrawMap(GraphicsContext gc, ResizableCanvas canvas){

        
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        ArrayList<TagNode> nodes = reader.getNodes();
        ArrayList<TagWay> ways = reader.getWays();
        TagBound bound = reader.getBound();

        Iterator<TagWay> it = ways.iterator();
        gc.setTransform(transform);
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        while (it.hasNext()){

            ArrayList<Long> line =  it.next().getNodes();

            System.out.println("HELLO " + line.size());

            gc.beginPath();

            gc.moveTo(line.get(0), line.get(1));


            for(int i = 2 ; i < line.size(); i+=2){

                gc.lineTo(line.get(i), line.get(i));
                System.out.println(line.get(i));

            }

            gc.stroke();

        }

    }

    void zoom(double factor, double dx, double dy){
        transform.prependTranslation(-dx, -dy);
        transform.prependScale(factor, factor);
        transform.prependTranslation(dx,dy);
        mainView.draw();
    }




}
