package gui;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.XMLReader;
import util.MathUtil;
import util.MinPQ;;

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

    private List<TagNode> nodes;
    private List<TagWay> ways;
    private List<TagRelation> relations;

    private Color currentColor;

    private GraphicsContext gc;

    private List<TagWay> waysToDrawWithType;
    private List<TagWay> waysToDrawWithoutType;




    public DrawingMap(MainView mainView, XMLReader reader){
        this.mainView = mainView;
        this.reader = reader;
        nodes = XMLReader.getNodes().values().stream().toList();  
        ways = XMLReader.getWays().values().stream().toList();
        relations = XMLReader.getRelations().values().stream().toList();
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


        //Resfreshes the screen
        gc = canvas.getGraphicsContext2D();
        gc.setTransform(new Affine());
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        gc.setTransform(transform);
        currentColor = Color.BLACK;

        waysToDrawWithType = new ArrayList<>();
        waysToDrawWithoutType = new ArrayList<>();

        long time = System.currentTimeMillis();

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

        List<TagNode> nodesRef;

        double[] xPoints;

        double[] yPoints;

        double defaultLineWidth = 1/Math.sqrt(transform.determinant());

        TagNode ref;

        double currentLon;
        double currentLat;
        
        while (!ways.isEmpty()) {
      
            TagWay tagWay = ways.delMin();

            nodesRef = tagWay.getNodes();

            currentColor = tagWay.getType().getColor();
            int counter = 0;
            xPoints = new double[nodesRef.size()];
            yPoints = new double[nodesRef.size()];

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
            gc.moveTo(nodesRef.get(0).getLon(), nodesRef.get(0).getLat());
            
            for (int i = 0; i < nodesRef.size() ; i ++){
                
                ref = nodesRef.get(i);
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
     * 
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
     * 
     * Zoomns in or out on the map dependent on the mouseposition
     * 
     * @param factor - The strength of which the map is zoomed
     * @param dx - Distance to pan on the x-axis
     * @param dy - Distance to pan on the y-axis
     */
    void zoom(double factor, double dx, double dy){
        double zoomLevelNext = zoomLevel * factor;
        System.out.println(zoomLevelNext);
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
        System.out.println(zoomLevel);
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

}