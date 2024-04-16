package gui;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.princeton.cs.algs4.RectHV;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Screen;
import parser.Tag;
import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.Type;
import parser.XMLReader;
import parser.XMLWriter;
import util.MathUtil;
import util.MinPQ;
import util.Tree;;


public class DrawingMap {


    static Affine transform = new Affine();
    public ResizableCanvas canvas;
    private XMLReader reader;
    private MainView mainView;
    private Tree kdtree;
    private double zoomLevel = 1;
    private int hierarchyLevel = 9;
    private double[] transformOffset = {0,0};
    private final double zoomLevelMin = 40, zoomLevelMax = 3000000; // These variables changes how much you can zoom in and out. Min is far out and max is closest in
    private double zoomScalerToMeter; // This is the world meters of how many meters there are from one side to the other
    private int[] zoomScales = {1000000, 500000, 250000, 125000, 67500, 33750, 16875, 8437, 4218, 2109};
    private double screenWidth; //ScreenWidth in pixels
    
    private boolean test;
    private double[] test1;

    public DrawingMap(MainView mainView, XMLReader reader){
        this.mainView = mainView;
        this.reader = reader; 
    }

    public void initialize(ResizableCanvas canvas){

        this.canvas = canvas;

        TagBound bound = reader.getBound();

        double minlon = bound.getMinLon();
        double maxlat = bound.getMaxLat();
        double maxlon = bound.getMaxLon();
        double minlat = bound.getMinLat();

        //temp is the width of the screen
        screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

        //zoomScalerToMeter is the distance from one side of the screen to the other in meters
        zoomScalerToMeter = haversineDist(new Point2D(0, 0), new Point2D(screenWidth,0));


        
        ArrayList<Tag<?>> tempList = new ArrayList<>(XMLReader.getNodes().values());
        tempList.addAll(XMLReader.getWays().values());
        tempList.addAll(XMLReader.getRelations().values());
        kdtree = new Tree(tempList);
        
        pan(-0.56*minlon, maxlat);
        zoom(canvas.getWidth() / (maxlon - minlon), 0, 0);

        DrawMap(canvas.getGraphicsContext2D(), canvas);
    }

    

    public static double haversineDist(Point2D coord1, Point2D coord2){
        double x1 = coord1.getX();
        double y1 = coord1.getY();
        double x2 = coord2.getX();
        double y2 = coord2.getY();
        return haversineDist(x1, y1, x2, y2);
    }

    public void DrawMap(GraphicsContext gc, ResizableCanvas canvas){
        long preTime = System.currentTimeMillis();

        if (kdtree == null){
            return;
        }

        gc = canvas.getGraphicsContext2D();
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        gc.setTransform(transform);

        double[] canvasBounds = getScreenBoundsBigger(0.05);

        RectHV rect = new RectHV(canvasBounds[0], canvasBounds[1], canvasBounds[2], canvasBounds[3]);

        if (!test){
            test = true;
            test1 = getScreenBoundsBigger(0.05);
        }
        //gc.setFill(Color.BLACK);
        //gc.fillRect(test1[0], test1[1], test1[2], test1[3]);
        //System.out.println("xmin: " + test1[0] + " | ymin: " + test1[1] + " | xmax: " + test1[2] + " | ymax: " + test1[3]);
        //System.out.println(zoomLevel);


        ArrayList<TagWay> waysToDrawWithType = new ArrayList<>();
        ArrayList<TagWay> waysToDrawWithoutType = new ArrayList<>();

        List<TagNode> nodes = new ArrayList<>();
        List<TagWay> ways = new ArrayList<>();
        List<TagRelation> relations = new ArrayList<>();
        List<TagWay> splitWayInRelation;
        HashSet<Tag<?>> tags = kdtree.getTagsInBounds(rect);
        for(Tag<?> tag : tags){
            if (tag instanceof TagNode){
                nodes.add((TagNode) tag);
            }else if (tag instanceof TagWay){
                ways.add((TagWay) tag);
            }else if (tag instanceof TagRelation){
                relations.add((TagRelation) tag);
            }
        }
        System.out.println("Antal tags: " + tags.size());


        for (TagWay way : ways){
            if (way.getType() != null){
                if (way.getType().getThisHierarchy() >= hierarchyLevel){
                    waysToDrawWithType.add(way);
                }
            } else{
                waysToDrawWithoutType.add(way);

            }
            
        }

        for (TagRelation relation : relations){
            
            splitWayInRelation = new ArrayList<>();

            for (TagWay way : relation.getWays()){
                if (way.getType() != null){
                    if (way.getType().getThisHierarchy() >= hierarchyLevel){
                        waysToDrawWithType.add(way);
                    }
                } else{
                    waysToDrawWithoutType.add(way);
    
                }
            }
            

            

            for (TagWay way : relation.getActualOuter()){

                //System.out.println(way.loops());

                /* 

                if (!way.loops()){
                    splitWayInRelation.add(way);
                    continue;
                }*/

                if (relation.getType() != null){
                    way.setType(relation.getType());
                    

                    if (way.getType().getThisHierarchy() >= hierarchyLevel){
                        waysToDrawWithType.add(way);
                    }
                } else{
                    waysToDrawWithoutType.add(way);
    
                }
            }
            /*

            if (!splitWayInRelation.isEmpty()){

                TagWay tw = new TagWay(null);

                for (TagWay way :splitWayInRelation){

                }


            }*/


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

            ArrayList<TagNode> nodesRef =  tagWay.getNodes();

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
            gc.moveTo(nodesRef.get(0).getLon(), nodesRef.get(0).getLat());

            for (int i = 0; i < nodesRef.size() ; i++){
                
                TagNode ref = nodesRef.get(i);

                gc.lineTo(ref.getLon(), ref.getLat());
                xPoints[counter] = ref.getLon();
                yPoints[counter] = ref.getLat();
                counter++;
                
            }

            //System.out.println(tagWay.getType());


            //gc.lineTo(nodesMap.get(nodesRef.get(0)).getLonDouble(), nodesMap.get(nodesRef.get(0)).getLatDouble());

            
            if (!tagWay.getType().getIsLine()){
                gc.setFill(c);
                gc.fillPolygon(xPoints, yPoints, counter);
            }

            gc.stroke();

    
        }

        //System.out.println("AFTER RENDERING: " + (System.currentTimeMillis() - preTime));


    }

    void zoom(double factor, double dx, double dy){
        double zoomLevelNext = zoomLevel * factor;
        if (zoomLevelNext < zoomLevelMax && zoomLevelNext > zoomLevelMin){
            zoomLevel = zoomLevelNext;

            for (int i = 0; i < zoomScales.length ; i++){
                if (zoomLevel > zoomScales[i]){

                    hierarchyLevel = i;
                    break;
                }
            }
            
            pan(-dx, -dy);
            transform.prependScale(factor, factor);
            pan(dx, dy);
        }
        else if(zoomLevel > zoomLevelMax){
            zoomLevel = zoomLevelMax - 1;
        }
        else if (zoomLevel < zoomLevelMin){
            zoomLevel = zoomLevelMin + 1;
        }
          
    }


    public void pan(double dx, double dy) {
        transformOffset[0] += dx;
        transformOffset[1] += dy;
        transform.prependTranslation(dx, dy);
        mainView.draw();
    }

    public static double yToLat(double aY) {
        return Math.toDegrees(2 * Math.atan(Math.exp(Math.toRadians(aY))) - Math.PI / 2);
    }

    public double metersToPixels(int meters){
        
        double metersPerPixelRatio = screenWidth / zoomScalerToMeter;
        System.out.println(metersPerPixelRatio);
        System.out.println(meters);
        
        return metersPerPixelRatio * meters;
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

    public double getZoomLevelMeters() {
        double temp = zoomScalerToMeter / zoomLevel;
        temp = temp * 10000;
        temp = Math.round(temp);
        temp /= 10;
        return temp;
    }

    /**
     * Calculates the coordinates the screen sees and returns a array of coordinates.
     * Index 0: X - Minimum
     * Index 1: Y - Minimum
     * Index 2: X - Maximum
     * Index 3: Y - Maximum
     * @return It returns the coordinates of the screen to map coordinates in an array (double[])
     */
    public double[] getScreenBounds(){
        double[] bounds = new double[4]; // x_min ; y_min ; x_max ; y_max
        bounds[0] = -(transform.getTx() / Math.sqrt(transform.determinant()));
        bounds[1] = (-transform.getTy()) / Math.sqrt(transform.determinant());
        bounds[2] = ((canvas.getWidth()) / zoomLevel) + bounds[0];
        bounds[3] = ((canvas.getHeight()) / zoomLevel) + bounds[1];
        return bounds;
    }

    public double[] getScreenBoundsBigger(double multiplier){
        double[] bounds = getScreenBounds();
        double width = bounds[2] - bounds[0];
        double height = bounds[3] - bounds[1];
        bounds[0] -= (width * (1.0 - multiplier));
        bounds[1] -= (height * (1.0 - multiplier));
        bounds[2] += (width * (1.0 + multiplier));
        bounds[3] += (height * (1.0 + multiplier));
        return bounds;
    }
}