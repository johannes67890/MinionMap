package GUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Screen;
import parser.TagBound;
import parser.TagNode;
import parser.TagWay;
import parser.XMLReader;
import util.MathUtil;
import util.MaxPQ;
import util.MinPQ;


public class DrawingMap {


    static Affine transform = new Affine();
    public ResizableCanvas canvas;
    private XMLReader reader;
    private MainView mainView;
    private double zoomLevel = 1;
    private int hierarchyLevel = 9;
    private final double zoomLevelMin = 40, zoomLevelMax = 3000000; // These variables changes how much you can zoom in and out. Min is far out and max is closest in
    private double zoomScalerToMeter; // This is the world meters of how long the scaler in the bottom right corner is. Divide it with the zoomLevel
    private int[] zoomScales = {1000000, 500000, 250000, 125000, 67500, 33750, 16875, 8437, 4218, 2109};

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
        double temp = Screen.getPrimary().getVisualBounds().getWidth() * 0.04;
        zoomScalerToMeter = haversineDist(new Point2D(0, 0), new Point2D(temp,0));

        //pan(-0.56*minlon, maxlat);
        pan(-0.56*minlon, maxlat);
        zoom(canvas.getWidth() / (maxlon - minlon), 0, 0);

        DrawMap(canvas.getGraphicsContext2D(), canvas);
    }


    // This calculates something. I didnt make this and we should try to understand it, if we're going to use it
    private static double haversineDist(double lon1, double lat1, double lon2, double lat2) {

        lat1 = yToLat(lat1); //Transform back using the spherical Mercator projection
        lat2 = yToLat(lat2);

        double latDistance = Math.toRadians(lat2 - lat1); //Δlat = lat2 − lat1
        double lonDistance = Math.toRadians(lon2 - lon1); //Δlon = lon2 − lon1

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) //a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlon/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        return 2 * 6356.752 * Math.asin(Math.sqrt(a)); //distance (km) = 2 * R * arcsin(√a)
    }

    public static double yToLat(double aY) {
        return Math.toDegrees(2 * Math.atan(Math.exp(Math.toRadians(aY))) - Math.PI / 2);
    }

    public static double haversineDist(Point2D coord1, Point2D coord2){
        double x1 = coord1.getX();
        double y1 = coord1.getY();
        double x2 = coord2.getX();
        double y2 = coord2.getY();
        return haversineDist(x1, y1, x2, y2);
    }

    public void DrawMap(GraphicsContext gc, ResizableCanvas canvas){


        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());


        gc.setTransform(transform);
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        List<TagNode> nodes = reader.getNodes();
        HashMap<Long, TagNode> nodesMap = reader.getNodesMap();
        ArrayList<TagWay> ways = reader.getWays();
        ArrayList<TagWay> waysToDrawWithType = new ArrayList<>();
        ArrayList<TagWay> waysToDrawWithoutType = new ArrayList<>();

        for (TagWay way : ways){
            if (way.getType() != null){
                if (way.getType().getThisHierarchy() >= hierarchyLevel){
                    waysToDrawWithType.add(way);
                }
            } else{
                waysToDrawWithoutType.add(way);

            }
            
        }
        MinPQ<TagWay> sortedWaysToDraw = new MinPQ<>(waysToDrawWithType.size());
        
        for (TagWay way : waysToDrawWithType){
            sortedWaysToDraw.insert(way);
        }

        /*while (!sortedWaysToDraw.isEmpty()) {

            System.out.println(sortedWaysToDraw.delMin().getType().getLayer());
            
        }*/

        TagBound bound = reader.getBound();

        //Iterator<TagWay> it = ways.iterator();
        

        double defaultLineWidth = 1/Math.sqrt(transform.determinant());

        Color c;
        
        while (!sortedWaysToDraw.isEmpty()) {

            gc.setLineWidth(defaultLineWidth);
            //System.out.println("DEFAULT LINE WIDTH: " + defaultLineWidth);
            gc.setStroke(Color.BLACK); 

            //System.out.println("HELLO");

      

            TagWay tagWay = sortedWaysToDraw.delMin();

            ArrayList<Long> nodesRef =  tagWay.getNodes();

            c = tagWay.getType().getColor();
            int counter = 0;
            double[] xPoints = new double[nodesRef.size()];
            double[] yPoints = new double[nodesRef.size()];

            double min = tagWay.getType().getMinWidth() * 0.00001;
            double max = tagWay.getType().getMaxWidth() * 0.00001;
            double lineWidth = MathUtil.clamp(defaultLineWidth * tagWay.getType().getWidth(), min, max);
            gc.setLineWidth(lineWidth);


            if(tagWay.getType().getIsLine()){
               
                //System.out.println("CALCULATED LINEWIDTH: " + lineWidth);
                gc.setStroke(tagWay.getType().getColor()); 
            } else{
                gc.setStroke(tagWay.getType().getPolyLineColor()); 
            }

            gc.beginPath();
            gc.moveTo(nodesMap.get(nodesRef.get(0)).getLonDouble(), nodesMap.get(nodesRef.get(0)).getLatDouble());
            for (Long ref : nodesRef){
                
                gc.lineTo(nodesMap.get(ref).getLonDouble(), nodesMap.get(ref).getLatDouble());
                xPoints[counter] = nodesMap.get(ref).getLonDouble();
                yPoints[counter] = nodesMap.get(ref).getLatDouble();
                counter++;
                
            }

            
            if (!tagWay.getType().getIsLine()){
                gc.setFill(c);
                gc.fillPolygon(xPoints, yPoints, xPoints.length);
            }

            gc.stroke();
    
        }

    }

    // Returns the distance for the ruler in the bottom right corner
    public double getZoomLevelMeters(){
        double temp = zoomScalerToMeter / zoomLevel;
        temp = temp * 10000;
        temp = Math.round(temp);
        temp /= 10;
        return temp;
    }

    void zoom(double factor, double dx, double dy){
        double zoomLevelNext = zoomLevel * factor;
        if (zoomLevelNext < zoomLevelMax && zoomLevelNext > zoomLevelMin){
            zoomLevel = zoomLevelNext;


            for (int i = 0; i < zoomScales.length ; i++){
                if (zoomLevel > zoomScales[i]){

                    hierarchyLevel = i;
                    System.out.println(hierarchyLevel);
                    break;
                }
            }
            

            pan(-dx, -dy);
            transform.prependScale(factor, factor);
            pan(dx, dy);
            mainView.draw(); 
        }
          
    }


    public void pan(double dx, double dy) {

        transform.prependTranslation(dx, dy);
        mainView.draw();
    }




}
