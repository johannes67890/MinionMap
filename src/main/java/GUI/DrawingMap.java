package GUI;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.util.*;

import javax.swing.text.html.HTML.Tag;

import org.parser.*;

public class DrawingMap {

    public static void DrawMap(ResizableCanvas canvas, XMLReader reader){

        GraphicsContext gc = canvas.getGraphicsContext2D();
        ArrayList<TagNode> nodes = reader.getNodes();
        ArrayList<TagWay> ways = reader.getWays();

        Iterator<TagWay> it = ways.iterator();
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        while (it.hasNext()){

            Long[] line = it.next().getNodes();


            gc.beginPath();
            gc.moveTo();
            gc.lineTo();
            gc.stroke();
            gc.closePath();
        }

    }




}
