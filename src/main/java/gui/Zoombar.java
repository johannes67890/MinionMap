package gui;

public class Zoombar {
    private double zoomLevelMax;
    private double zoomLevelMin;
    private int zoombarIntervals;
    private double[] zoombarScales;
    private int[] zoomLevelMetersArray;
    
    private int range;

    private int zoombarHierarchy; 
    
    public Zoombar(int zoombarIntervals, double zoomLevelMax, double zoomLevelMin) {
        this.zoomLevelMax = zoomLevelMax;
        this.zoomLevelMin = zoomLevelMin;
        this.zoombarIntervals = zoombarIntervals;
        
        setZoombarScales(this.zoombarIntervals);
        setZoombarMetersInterval(this.zoombarIntervals);
    }

    public void setRange(double zoomLevel){
        setZoombarHierarchyLevel(zoomLevel);
        this.range = zoomLevelMetersArray[this.zoombarHierarchy];
    }

    public int getRange(){
        return this.range;
    }

    private void setZoombarScales(int n) {
        
        double[] zoombarScales = new double[n];
        double frequency = (this.zoomLevelMax-this.zoomLevelMin)/n; 

        for (int i = 0; i < n; i++) {
            zoombarScales[i] = hierarchyToZoomscale((i*frequency)+1.2);
        }

        this.zoombarScales = zoombarScales;
    }

    private void setZoombarHierarchyLevel(double zoomLevel) {
        for (int i = 0; i < zoombarIntervals ; i++){
            if(zoomLevel > zoombarScales[i]){
                this.zoombarHierarchy = i;
                break;
            }
        }
    }

    private void setZoombarMetersInterval(int zoombarIntervals){

        int[] zoomLevelMetersArray = new int[zoombarIntervals];

        for (int i = 0; i < zoombarIntervals; i++){
            zoomLevelMetersArray[i] = roundToClosest(hierarchyLevelToMeters(i));
        }

        this.zoomLevelMetersArray = zoomLevelMetersArray;
    }

    private double hierarchyToZoomscale(double d){
        double zoomScale = 48.748*Math.pow(Math.E, -0.348*d);
        return zoomScale;
    }

    private int roundToClosest(double number){
        int closest = numberScale(number);
        return (int) Math.round(number / closest) * closest;
    }

    private int numberScale(double number){
        int digits = countDigits(number);
        int scale = (int) Math.pow(10, digits-1);
        return scale;
    }

    private int countDigits(double number){
        return (int) Math.log10(number) + 1;
    }

    private double hierarchyLevelToMeters(int hierarchyLevel){
        return 4.5925*Math.pow(Math.E, 0.7689*hierarchyLevel);
    }
}