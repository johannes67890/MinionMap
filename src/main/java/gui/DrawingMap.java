package gui;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import gui.GraphicsHandler.GraphicStyle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import parser.Model;
import parser.Tag;
import parser.TagAddress;
import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import util.Type;
import structures.MinPQ;
import structures.Trie;
import structures.KDTree.Rect3D;
import structures.KDTree.Tree;
import util.MathUtil;


/**
 * 
 * The class that processes all ways and relations, and draws them using their types.
 * 
 */
@SuppressWarnings("ALL")
public class DrawingMap {


    static Affine transform = new Affine();

    private final Model model;
    public ResizableCanvas canvas;
    private MapView mapView;
    public double zoomLevel = 1;
    private int hierarchyLevel = 9;
    private int zoombarIntervals = 14;
    private final double zoomLevelMin = 0.0007, zoomLevelMax = 31.8; // These variables changes how much you can zoom in and out. Min is far out and max is closest in
    private double[] zoomScales = {32, 16, 8, 4, 2, 1, 0.25, 0.1, 0.05, 0.015, 0.0001}; // 32, 16, 8, 4, 2, 1, 0.5, 0.1, 0.05, 0.015, 0.0001
    public Zoombar zoombar;
    private Trie trie;
    private List<Tag> markedTag;
    private List<TagNode> nodes;
    private List<TagWay> ways;
    private List<TagRelation> relations;
    private Tag pointOfInterestNode = null;
    private Label zoomLabel;
    private ImageView zoomImage;

    private boolean isInitialized = false;
    private Color currentColor;
    private Color backGroundColor;
    private boolean backGroundSet;

    private GraphicsContext gc;

    private List<TagWay> waysToDrawWithType;
    private List<TagWay> waysToDrawWithoutType;



    public DrawingMap(MapView mapView, Model model){
        this.mapView = mapView;
        this.model = model;
    }

    /**
     * 
     * The first drawing of the map.
     * 
     * @param canvas - the canvas to be drawn.
     */

    public void initialize(ResizableCanvas canvas){

        this.canvas = canvas;
        
        TagBound bound = mapView.getModel().getBound();
        
        double minlon = bound.getMinLon();
        double maxlat = bound.getMaxLat();
        double maxlon = bound.getMaxLon();

        zoombar = new Zoombar(zoombarIntervals, zoomLevelMax, zoomLevelMin);
        setBackGroundColor(Color.web("#F2EFE9"));
        pan(-minlon, maxlat);
        zoom(canvas.getWidth() / (maxlon - minlon), 0, 0);
        isInitialized = true;
        mapView.draw();
    }

    public boolean IsInitialized(){
        return isInitialized;
    }

    public Trie getTrie(){
        return trie;
    }

    public void setBackGroundColor(Color c){
        backGroundColor = c;
    }

    public double getZoomLevelMax(){
        return zoomLevelMax;
    }

    public double getZoomLevelMin(){
        return zoomLevelMin;
    }


    /**
     * Directly draws the map, starting by filling the canvas with white, followed by drawing lines and polygons
     * @param gc - Graphicscontext, which ensures that the position of the vertices are placed correctly
     * @param canvas - The canvas that get drawn
     */

    public void DrawMap(Canvas canvas){
        if (!Tree.isLoaded()){
            return;
        }


        //Resfreshes the screen
        gc = canvas.getGraphicsContext2D();
        gc.setTransform(new Affine());
     
        currentColor = Color.BLACK;

        float[] canvasBounds = getScreenBoundsBigger(0.2);
        Rect3D rect = new Rect3D(canvasBounds[0], canvasBounds[1], hierarchyLevel, canvasBounds[2], canvasBounds[3], 100);
        nodes = new ArrayList<>();
        ways = new ArrayList<>();
        relations = new ArrayList<>();

        HashSet<Tag> tags = Tree.getTagsInBounds(rect);
        backGroundSet = false;
        for(Tag tag : tags){
            if (tag instanceof TagNode){
                nodes.add((TagNode) tag);
            }else if (tag instanceof TagWay){
                TagWay way = (TagWay) tag;
                ways.add(way);
            }else if (tag instanceof TagRelation){
                TagRelation relation = (TagRelation) tag;
                relations.add(relation);
                if (relation.getType() == Type.BORDER || relation.getType() == Type.REGION){
                    setBackGroundColor(Color.web("#AAD3DF"));
                    backGroundSet = true;
                }
            }
        }

        if (!backGroundSet){
            setBackGroundColor(Color.web("#F2EFE9"));
        }

        switch (GraphicsHandler.getGraphicStyle()) {
            case DEFAULT:
                gc.setFill(backGroundColor);
                break;
            case DARKMODE:
                gc.setFill(Color.BLACK);
                break;
            case GRAYSCALE:
                gc.setFill(backGroundColor.grayscale());
                break;
            default:
                break;
        }
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        gc.setTransform(transform);

        waysToDrawWithType = new ArrayList<>();
        waysToDrawWithoutType = new ArrayList<>();

        handleWays(ways);
        handleRelations();

        MinPQ<TagWay> sortedWaysToDraw = new MinPQ<>(waysToDrawWithType.size());
        
        for (TagWay way : waysToDrawWithType){
            sortedWaysToDraw.insert(way);
        }
 
        drawWays(sortedWaysToDraw);

        if (markedTag != null){
            for (Tag tag : markedTag){
                drawMarkedTag(tag);
            }
        }
        if (pointOfInterestNode != null){
            drawMarkedTag(pointOfInterestNode);
        }
    }

    public void setPointOfInterest(Tag node){
            pointOfInterestNode = node;
    }

    public void setMarkedTag(ArrayList<Tag> tag){
        markedTag = tag;
        mapView.draw();
    }

    /**
     * Draws a specified tag with a different color to mark it on the map
     * @param tag the tag to be marked
     */
    private void drawMarkedTag(Tag tag){
        gc.setFill(Color.PINK.interpolate(Color.RED, 0.5));
        gc.setStroke(Color.RED);
        if (tag instanceof TagRelation){
            drawRelation((TagRelation)tag);
        }else if(tag instanceof TagWay){
            drawWay((TagWay) tag, true);
        }else if(tag instanceof TagNode || tag instanceof TagAddress){
            drawPoint(tag);
        }
    }

    private void drawInnerWays(){


        waysToDrawWithType = new ArrayList<>();

        ways = new ArrayList<>();

        for (TagRelation relation : relations){

            if (relation.getId() == 12332811){
                System.out.println(relation.getActualInner().get(0).getType());
            }

            handleWays(relation.getActualInner());
        }

        MinPQ<TagWay> sortedWaysToDraw = new MinPQ<>(waysToDrawWithType.size());


        for (TagWay way : waysToDrawWithType){
            sortedWaysToDraw.insert(way);
        }

        drawWays(sortedWaysToDraw);

    }

    public void drawPoint(Tag node){
        double radius = 10 * 1/Math.sqrt(transform.determinant());
        gc.fillOval(node.getLon() - radius / 2, -(node.getLat()) - radius / 2, radius, radius);
    }

    private void drawRelation(TagRelation relation){
        boolean isStarted = false;
        for (TagWay way : relation.getWays()){
            if (!isStarted){
                drawWay(way, true);
                isStarted = true;
            }else{
                drawWay(way, false);
            }
        }
    }
    
    private void drawWay(TagWay way, boolean starting){
        double[] xPoints;
        double[] yPoints;

        double defaultLineWidth = 1/Math.sqrt(transform.determinant());
        int counter = 0;
        xPoints = new double[way.getRefNodes().size()];
        yPoints = new double[way.getRefNodes().size()];
        
        if(way.getType() == null){
            way.setType(Type.UNKNOWN);
        }

        double min = way.getType().getMinWidth();
        double max = way.getType().getMaxWidth();
        double lineWidth = MathUtil.clamp(defaultLineWidth * way.getType().getWidth(), min, max);
        gc.setLineWidth(lineWidth);
        
        
        gc.beginPath();
        gc.moveTo(way.getRefNodes().getFirst().getLon(), -way.getRefNodes().getFirst().getLat());
    
        gc.setStroke(Color.RED);    
        for (TagNode n : way.getRefNodes()) {
            gc.lineTo(n.getLon(), -n.getLat());
            xPoints[counter] = n.getLon();
            yPoints[counter] = -n.getLat();
            counter++;
            
            if(n.getNext() == null) break;
        }
        gc.stroke();    
    }

    

    /**
     * 
     * Gets all ways in a priorityqueue and draws them based on individual TagWay Types
     * 
     * @param ways - the ways to be drawn
     */
    private void drawWays(MinPQ<TagWay> ways){
        double[] xPoints;
        double[] yPoints;

        double defaultLineWidth = 1/Math.sqrt(transform.determinant());
        
        while (!ways.isEmpty()) {
      
            TagWay tagWay = ways.delMin();

            currentColor = tagWay.getType().getColor();
            int counter = 0;
            xPoints = new double[tagWay.getRefNodes().size()];
            yPoints = new double[tagWay.getRefNodes().size()];

            double min = tagWay.getType().getMinWidth();
            double max = tagWay.getType().getMaxWidth();
            double lineWidth = MathUtil.clamp(defaultLineWidth * tagWay.getType().getWidth(), min, max);
            if (GraphicsHandler.getGraphicStyle() == GraphicStyle.DARKMODE){
                if (!tagWay.getType().getIsLine()){
                    lineWidth = lineWidth * 2;
                }
                gc.setStroke(Color.GRAY.interpolate(Color.LIGHTGRAY, 0.5));
            }
            else{

                if(tagWay.getType().getIsLine()){
                    gc.setStroke(tagWay.getType().getColor()); 
                } else{
                    gc.setStroke(tagWay.getType().getPolyLineColor()); 
                }
            }
           
            gc.setLineWidth(lineWidth);

            gc.beginPath();
            gc.moveTo(tagWay.getRefNodes().getFirst().getLon(), -tagWay.getRefNodes().getFirst().getLat());
            
                for (TagNode n : tagWay.getRefNodes()) {
                    gc.lineTo(n.getLon(), -n.getLat());
                    xPoints[counter] = n.getLon();
                    yPoints[counter] = -n.getLat();
                    counter++;
                    
                    if(n.getNext() == null) break;
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
     * Handles ways by checking if they are connected to a type.
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
     * Calculates the coordinates the screen sees and returns a array of coordinates.
     * A bit weird. If you draw with this calculation it draws perfect, but if you need the nodes within the screen the Y value needs to be negated
     * This method is primarily made for the KdTree
     * <p>
     * Index 0: X - Minimum
     * <p>
     * Index 1: Y - Minimum
     * <p>
     * Index 2: X - Maximum
     * <p>
     * Index 3: Y - Maximum
     * @return It returns the coordinates of the screen to map coordinates in an array (float[])
     */
    public float[] getScreenBounds(){
        
        float[] bounds = new float[4]; // x_min ; y_min ; x_max ; y_max
        double width = ((canvas.getWidth()) / zoomLevel);
        double height = ((canvas.getHeight()) / zoomLevel);
        bounds[0] = (float) -(transform.getTx() / Math.sqrt(transform.determinant()));
        bounds[1] = (float) ((transform.getTy()) / Math.sqrt(transform.determinant()) - height);
        bounds[2] = (float) width + bounds[0];
        bounds[3] = (float) height + bounds[1];
        return bounds;
    }

    /**
     * Like the getScreenBounds() method, but returns a bigger area so
     * the screen can be panned without the map disappearing
     * @param multiplier - The multiplier of the screen bounds
     * @return It returns the coordinates of the screen to map coordinates in an array (float[])
     * @see getScreenBounds()
     */
    public float[] getScreenBoundsBigger(double multiplier){
        float[] bounds = getScreenBounds();
        double width = bounds[2] - bounds[0];
        double height = bounds[3] - bounds[1];
        bounds[0] -= (width * (1.0 + multiplier));
        bounds[1] -= (height * (1.0 + multiplier));
        bounds[2] += (width * (1.0 + multiplier));
        bounds[3] += (height * (1.0 + multiplier));
        return bounds;
    }

    // getZoomLevelMeters() removed in zoombarfix branch 2/5-2024



    /**
     * 
     * Zoomns in or out on the map dependent on the mouseposition
     * 
     * @param factor - The strength of which the map is zoomed
     * @param dx - Distance to pan on the x-axis
     * @param dy - Distance to pan on the y-axis
     */
    public void zoom(double factor, double dx, double dy){
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
        else if(zoomLevelNext > zoomLevelMax){
            zoomLevel = zoomLevelMax - 0.000001;
        }
        else if (zoomLevelNext < zoomLevelMin){
            zoomLevel = zoomLevelMin + 0.000001;
        }

        zoombar.setRange(zoomLevel);
        zoombarUpdater(zoomLabel, zoomImage);
    }

    public Affine getTransform(){
        return transform;
    }

    public double getZoomLevel(){
        return zoomLevel;
    }


    /**
     * 
     * Pans the drawing
     * @param dx - Distance to pan on the x-axis
     * @param dy - Distance to pan on the y-axis
     */
    public void pan(double dx, double dy) {
        transform.prependTranslation(dx, dy);
        mapView.draw();
    }

    /**
     * Updates the zoombar
     * @param label - The label that shows the range of the zoombar in meters
     * @param imageView - The image of the zoombar that has its width changed
     */
    public void zoombarUpdater(Label label, ImageView imageView) {
        label.setText(String.valueOf(zoombar.getRange()) + "m");
        imageView.setFitWidth(metersToPixels(zoombar.getRange()));
    }

    /**
     * @param meters the amount of meters you want to know the pixel value of
     * @return the amount of pixels that corresponds to the amount of meters
     */

     private double metersToPixels(int meters){
        float[] bounds = getScreenBounds();
        TagNode left = new TagNode(0, bounds[1]);
        TagNode right = new TagNode(0, bounds[3]);
        double widthInMeter =  left.distance(right);

        
        double metersPerPixelRatio = canvas.getWidth() / widthInMeter; //Divided by 2 because the width is from the center of the screen

        return metersPerPixelRatio * meters;
    }
    public void append(double dx, double dy) {
        transform.appendTranslation(dx, dy);
        mapView.draw();
    }

    public void setZoomLabel(Label label){
        this.zoomLabel = label;
    }

    public void setZoomImage(ImageView imageView){
        this.zoomImage = imageView;
    }

}