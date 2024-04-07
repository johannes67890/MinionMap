package gui;
import java.util.*;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Screen;
import parser.*;


public class DrawingMap {


    static Affine transform = new Affine();
    public ResizableCanvas canvas;
    private XMLReader reader;
    private MainView mainView;
    private double zoomLevel = 1;
    private final double zoomLevelMin = 40, zoomLevelMax = 300000; // These variables changes how much you can zoom in and out. Min is far out and max is closest in
    private double zoomScalerToMeter; // This is the world meters of how long the scaler in the bottom right corner is. Divide it with the zoomLevel

    public DrawingMap(MainView mainView, XMLReader reader){
        this.mainView = mainView;
        this.reader = reader;        
    }

    public void initialize(ResizableCanvas canvas){

        this.canvas = canvas;

        TagBound bound = reader.getBound();

        double minlon = bound.getMaxLon();
        double maxlat = bound.getMaxLat();
        double maxlon = bound.getMaxLon();
        double minlat = bound.getMinLat();
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
        TagBound bound = reader.getBound();

        Iterator<TagWay> it = ways.iterator();
        
        gc.setLineWidth(1/Math.sqrt(transform.determinant()));
        System.out.println("CANVAS HEIGHT: " + canvas.getHeight());

        while (it.hasNext()) {

            ArrayList<Long> nodesRef =  it.next().getNodes();

            gc.beginPath();
            gc.moveTo(nodesMap.get(nodesRef.get(0)).getLon(), nodesMap.get(nodesRef.get(0)).getLat());
            for (Long ref : nodesRef){
                
                gc.lineTo(nodesMap.get(ref).getLat(), nodesMap.get(ref).getLat());
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
