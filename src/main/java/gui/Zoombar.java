package gui;

public class Zoombar {
    private double zoomLevelMax;
    private double zoomLevelMin;
    private int zoombarIntervals;
    private double[] zoombarScales;
    private int[] zoomLevelMetersArray;
    
    private int range;

    private int zoombarHierarchy; 
    
    /**
     * Constructor for the Zoombar class
     * @param zoombarIntervals the number of intervals/levels in the zoombar
     * @param zoomLevelMax the maximum zoom level 
     * @param zoomLevelMin the minimum zoom level
     */
    public Zoombar(int zoombarIntervals, double zoomLevelMax, double zoomLevelMin) {
        this.zoomLevelMax = zoomLevelMax;
        this.zoomLevelMin = zoomLevelMin;
        this.zoombarIntervals = zoombarIntervals;
        
        setZoombarScales(this.zoombarIntervals);
        setZoombarMetersInterval(this.zoombarIntervals);
    }

    /**
     * Set the range of the zoombar
     * @param zoomLevel the zoom level of the zoombar
     */
    public void setRange(double zoomLevel){
        setZoombarHierarchyLevel(zoomLevel);
        this.range = zoomLevelMetersArray[this.zoombarHierarchy];
    }

    /**
     * Get the range of the zoombar
     * @return the range of the zoombar
     */
    public int getRange(){
        return this.range;
    }

    /**
     * Calculates and instantiates a double[] containing the zoombar scales
     * The zoombar scales are calculated based on the number of intervals and the zoom level range
     * They determine when the zoom level changes
     * @param n the number of intervals in the zoombar
     */
    private void setZoombarScales(int n) {
        
        double[] zoombarScales = new double[n];
        double frequency = (this.zoomLevelMax-this.zoomLevelMin)/n; 

        for (int i = 0; i < n; i++) {
            zoombarScales[i] = hierarchyToZoomscale((i*frequency)+1.2);
        }

        this.zoombarScales = zoombarScales;
    }

    /**
     * Sets the zoombar hierarchy level based on the zoom level
     * @param zoomLevel the current amount of zoom in the map
     */
    private void setZoombarHierarchyLevel(double zoomLevel) {
        for (int i = 0; i < zoombarIntervals ; i++){
            if(zoomLevel > zoombarScales[i]){
                this.zoombarHierarchy = i;
                break;
            }
        }
    }

   /**
    * Sets the current meter interval for the zoombar to display
    * @param zoombarIntervals the number of intervals in the zoombar
    */
    private void setZoombarMetersInterval(int zoombarIntervals){

        int[] zoomLevelMetersArray = new int[zoombarIntervals];

        for (int i = 0; i < zoombarIntervals; i++){
            zoomLevelMetersArray[i] = roundToClosest(hierarchyLevelToMeters(i));
        }

        this.zoomLevelMetersArray = zoomLevelMetersArray;
    }

    /**
     * Converts a hierarchy level to a zoom scale by using a exponential function
     * @param d the hierarchy level
     * @return the zoom scale
     */
    private double hierarchyToZoomscale(double d){
        double zoomScale = 48.748*Math.pow(Math.E, -0.348*d);
        return zoomScale;
    }

    /**
     * Rounds a number to the closest number in the scale
     * @param number the number to round
     * @return the rounded number
     */
    private int roundToClosest(double number){
        int closest = numberScale(number);
        return (int) Math.round(number / closest) * closest;
    }
    
    /**
     * Returns the scale of the number for rounding
     * @param number the number to get the scale of
     * @return the scale of the number
     */
    private int numberScale(double number){
        int digits = countDigits(number);
        int scale = (int) Math.pow(10, digits-1);
        return scale;
    }

    /**
     * Counts the number of digits in a number used for numberscale
     * @param number the number to count the digits of
     * @return the number of digits in the number
     */
    private int countDigits(double number){
        return (int) Math.log10(number) + 1;
    }

    /**
     * Converts a hierarchy level to meters
     * @param hierarchyLevel the hierarchy level
     * @return the meters
     */
    private double hierarchyLevelToMeters(int hierarchyLevel){
        return 4.5925*Math.pow(Math.E, 0.7689*hierarchyLevel);
    }
}