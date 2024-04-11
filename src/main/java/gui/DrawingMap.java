package gui;
import java.util.ArrayList;
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
    private double zoomScalerToMeter; // This is the world meters of how long the scaler in the bottom right corner is. Divide it with the zoomLevel
    private int[] zoomScales = {1000000, 500000, 250000, 125000, 67500, 33750, 16875, 8437, 4218, 2109};

    public DrawingMap(MainView mainView, XMLReader reader){
        this.mainView = mainView;
        this.reader = reader; 
    }

    public void initialize(ResizableCanvas canvas){

        this.canvas = canvas;

        TagBound bound = reader.getBound();

        //System.out.println("BOUNDS: " + bound.getMaxLon());

        double minlon = bound.getMinLon();
        double maxlat = bound.getMaxLat();
        double maxlon = bound.getMaxLon();
        double minlat = bound.getMinLat();
        double temp = Screen.getPrimary().getVisualBounds().getWidth() * 0.04;
        zoomScalerToMeter = haversineDist(new Point2D(0, 0), new Point2D(temp,0));
        ArrayList<Tag<?>> tempList = new ArrayList<>(XMLReader.getNodes().values());
        tempList.addAll(XMLReader.getWays().values());
        tempList.addAll(XMLReader.getRelations().values());
        kdtree = new Tree(tempList);
        
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
        long preTime = System.currentTimeMillis();

        if (kdtree == null){
            return;
        }

        gc = canvas.getGraphicsContext2D();
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        gc.setTransform(transform);

        //RectHV rect = new RectHV(XMLReader.getBound().getMinLon() * 0.56, -XMLReader.getBound().getMaxLat(), XMLReader.getBound().getMaxLon() * 0.56, -XMLReader.getBound().getMinLat());

        double x1 = transform.getTx() / Math.sqrt(transform.determinant());
        double y1 = (-transform.getTy()) / Math.sqrt(transform.determinant());
        double x2 = canvas.getWidth() - x1;
        double y2 = canvas.getHeight() - y1;

        RectHV rect = new RectHV(x1, y1, x2, y2);

        ArrayList<TagWay> waysToDrawWithType = new ArrayList<>();
        ArrayList<TagWay> waysToDrawWithoutType = new ArrayList<>();

        
        //List<TagNode> nodes = XMLReader.getNodes().values().stream().toList();
        List<TagNode> nodes = new ArrayList<>();
        //List<TagWay> ways = XMLReader.getWays().values().stream().toList();
        List<TagWay> ways = new ArrayList<>();
        //List<TagRelation> relations = XMLReader.getRelations().values().stream().toList();
        List<TagRelation> relations = new ArrayList<>();
        List<TagWay> splitWayInRelation;
        List<Tag<?>> tags = kdtree.getTagsInBounds(rect);
        System.out.println("Antal tags: " + tags.size());
        for(Tag<?> tag : tags){
            if (tag instanceof TagNode){
                nodes.add((TagNode) tag);
            }else if (tag instanceof TagWay){
                ways.add((TagWay) tag);
            }else if (tag instanceof TagRelation){
                relations.add((TagRelation) tag);
            }
        }
        

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




}
