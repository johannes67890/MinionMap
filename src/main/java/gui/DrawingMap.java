package gui;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.HashSet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import parser.Tag;
import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.XMLReader;
import util.MathUtil;
import util.MinPQ;
import util.Tree;;

/**
 * 
 * The class that processes all ways and relations, and draws them using their types.
 * 
 */
public class DrawingMap {


    static Affine transform = new Affine();
    public ResizableCanvas canvas;
    private XMLReader reader;
    private MainView mainView;
    private double zoomLevel = 1;
    private int hierarchyLevel = 9;
    private final double zoomLevelMin = 0.001, zoomLevelMax = 30; // These variables changes how much you can zoom in and out. Min is far out and max is closest in
    private double zoomScalerToMeter; // This is the world meters of how long the scaler in the bottom right corner is. Divide it with the zoomLevel
    private double[] zoomScales = {32, 16, 8, 4, 2, 1, 0.5, 0.1, 0.05, 0.015, 0.0001}; //
    private double screenWidth;

    private List<TagNode> nodes = new ArrayList<>();
    private List<TagWay> ways = new ArrayList<>();
    private List<TagRelation> relations = new ArrayList<>();

    private Color currentColor;

    private GraphicsContext gc;

    private List<TagWay> waysToDrawWithType;
    private List<TagWay> waysToDrawWithoutType;




    public DrawingMap(MainView mainView, XMLReader reader){
        this.mainView = mainView;
        this.reader = reader;
        //nodes = XMLReader.getNodes().values().stream().toList();
        //ways = XMLReader.getWays().values().stream().toList();
        //relations = XMLReader.getRelations().values().stream().toList();
    }

    /**
     * 
     * The first drawing of the map.
     * 
     * @param canvas - the canvas to be drawn.
     */

    public void initialize(ResizableCanvas canvas){

        this.canvas = canvas;

        TagBound bound = reader.getBound();

        double minlon = bound.getMinLon();
        double maxlat = bound.getMaxLat();
        double maxlon = bound.getMaxLon();
        double minlat = bound.getMinLat();


        ArrayList<Tag<?>> tempList = new ArrayList<>(XMLReader.getNodes().values());
        tempList.addAll(XMLReader.getWays().values());
        tempList.addAll(XMLReader.getRelations().values());
        Tree.initialize(tempList);;
        pan(-minlon, minlat);
        zoom(canvas.getWidth() / (maxlon - minlon), 0, 0);
        DrawMap(canvas);
    }

    /**
     * Directly draws the map, starting by filling the canvas with white, followed by drawing lines and polygons
     * @param gc - Graphicscontext, which ensures that the position of the vertices are placed correctly
     * @param canvas - The canvas that get drawn
     */

    public void DrawMap(ResizableCanvas canvas){
        long preTime = System.currentTimeMillis();
        this.canvas = canvas;
        if (!Tree.isLoaded()){
            return;
        }


        //Resfreshes the screen
        gc = canvas.getGraphicsContext2D();
        gc.setTransform(new Affine());
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        gc.setTransform(transform);
        currentColor = Color.BLACK;

        double[] canvasBounds = getScreenBoundsBigger(0.05);
        RectHV rect = new RectHV(canvasBounds[0], canvasBounds[1], canvasBounds[2], canvasBounds[3]);
        
        nodes = new ArrayList<>();
        ways = new ArrayList<>();
        relations = new ArrayList<>();

        HashSet<Tag<?>> tags = Tree.getTagsInBounds(rect);
        
        for(Tag<?> tag : tags){
            if (tag instanceof TagNode){
                nodes.add((TagNode) tag);
            }else if (tag instanceof TagWay){
                TagWay way = (TagWay) tag;
                ways.add(way);
            }else if (tag instanceof TagRelation){
                TagRelation relation = (TagRelation) tag;
                relations.add(relation);
            }
        }
        
        waysToDrawWithType = new ArrayList<>();
        waysToDrawWithoutType = new ArrayList<>();

        handleWays(ways);

        handleRelations();

        MinPQ<TagWay> sortedWaysToDraw = new MinPQ<>(waysToDrawWithType.size());
        
        for (TagWay way : waysToDrawWithType){
            sortedWaysToDraw.insert(way);
        }
 
        drawWays(sortedWaysToDraw);
    }

    /**
     * 
     * Gets all ways in a priorityqueue and draws them based on individual TagWay Types
     * 
     * @param ways - the ways to be drawn
     */
    private void drawWays(MinPQ<TagWay> ways){
        TagNode[] nodesRef;

        double[] xPoints;

        double[] yPoints;

        double defaultLineWidth = 1/Math.sqrt(transform.determinant());

        TagNode ref;

        double currentLon;
        double currentLat;

        int count = 0;

        while (!ways.isEmpty()) {
      
            TagWay tagWay = ways.delMin();

            nodesRef = tagWay.getNodes();

            currentColor = tagWay.getType().getColor();
            int counter = 0;
            xPoints = new double[nodesRef.length];
            yPoints = new double[nodesRef.length];

            double min = tagWay.getType().getMinWidth();
            double max = tagWay.getType().getMaxWidth();
            double lineWidth = MathUtil.clamp(defaultLineWidth * tagWay.getType().getWidth(), min, max);
            gc.setLineWidth(lineWidth);


            if(tagWay.getType().getIsLine()){
               
                gc.setStroke(tagWay.getType().getColor()); 
            } else{
                gc.setStroke(tagWay.getType().getPolyLineColor()); 
            }

            
            gc.beginPath();
            gc.moveTo(nodesRef[0].getLon(), nodesRef[0].getLat());
            
            for (int i = 0; i < nodesRef.length ; i ++){
                
                ref = nodesRef[i];
                currentLat = ref.getLat();
                currentLon = ref.getLon();

                gc.lineTo(currentLon, currentLat);
                xPoints[counter] = currentLon;
                yPoints[counter] = currentLat;
                counter++;
                
            }


            //Fills polygons with color
            if (!tagWay.getType().getIsLine()){
                gc.setFill(currentColor);
                gc.fillPolygon(xPoints, yPoints, counter);
            }
            
            gc.stroke();    
        }

    }

    /**
     * 
     * Handles ways by checking if their connected to a type.
     * If they are not connected, ways will be put into a list of ways without type.
     * 
     */
    public void handleWays(List<TagWay> waysToHandle){

        for (TagWay way : waysToHandle){
            if (way.getType() != null){
                if (way.getType().getThisHierarchy() >= hierarchyLevel){
                    waysToDrawWithType.add(way);
                }
            } else{
                waysToDrawWithoutType.add(way);
            }
        }

    }

    /**
     * Handles relations regarding their inner and outer ways.
     * Outer way's type will be set based on the relation's type.
     */

    public void handleRelations(){
        for (TagRelation relation : relations){

            handleWays(relation.getWays());
            
            for (TagWay way : relation.getHandledOuter()){
                if (!way.loops()){
                    continue;
                }

                if (relation.getType() != null){
                    way.setType(relation.getType());
                    if (way.getType().getThisHierarchy() >= hierarchyLevel){
                        waysToDrawWithType.add(way);
                    }
                } else{
                    waysToDrawWithoutType.add(way);
    
                }
            }
        }
    }


    /**
     * Getter for the zoomlevel in meters
     * @return Returns the distance for the ruler in the bottom right corner
     */
    public double getZoomLevelMeters(){
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



    /**
     * 
     * Zoomns in or out on the map dependent on the mouseposition
     * @param factor - The strength of which the map is zoomed
     * @param dx - Distance to pan on the x-axis
     * @param dy - Distance to pan on the y-axis
     */
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
            
            //Panning the map using desired delta x- and y values
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

        this.screenWidth = canvas.getWidth();
    }

    /**
         
    Calculates the coordinates the screen sees and returns a array of coordinates.
    Index 0: X - Minimum
    Index 1: Y - Minimum
    Index 2: X - Maximum
    Index 3: Y - Maximum
    @return It returns the coordinates of the screen to map coordinates in an array (double[])
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


    /**
     * 
     * Pans the drawing
     * @param dx - Distance to pan on the x-axis
     * @param dy - Distance to pan on the y-axis
     */
    public void pan(double dx, double dy) {

        transform.prependTranslation(dx, dy);
        mainView.draw();
    }

    public void zoombarUpdater(Label label, ImageView imageView){
        label.setText(String.valueOf(getRange()) + "m");
        imageView.setFitWidth(metersToPixels(getRange()));
    }

    /**
     * @param meters the amount of meters you want to know the pixel value of
     * @return the amount of pixels that corresponds to the amount of meters
     */

    public double metersToPixels(int meters){
        double[] bounds = getScreenBounds();
        double widthInMeter = bounds[2] - bounds[0];

        double metersPerPixelRatio = screenWidth / widthInMeter;
        
        return metersPerPixelRatio * meters;
    }

    public int getRange(){
        
        double range = 2.6533*Math.pow(Math.E, 0.6864*hierarchyLevel);
        double add = 0.625*Math.pow(Math.E, 0.6931*hierarchyLevel);
        int temp = 0;
        if(hierarchyLevel <= 3){
            temp = (int) Math.round(((range+add)-1.25)/5)*5;
        } else if(hierarchyLevel < 9){
            temp = (int) Math.round((range+add)/10)*10;
        } else {
            temp = (int) Math.round((range+add)/500)*500;
        }
        return temp;
    }
}