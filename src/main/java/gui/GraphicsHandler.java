package gui;
/*
 * The GraphicsHandler class is responsible for handling the graphics style of the application
 * The available styles are defined in the GraphicStyle enum
 * The current style is stored in the currentStyle variable
 * The getGraphicStyle function returns the current style
 * The setGraphicsStyle function sets the current style
 * The style can be set to DEFAULT, DARKMODE or GRAYSCALE Updated: 06/05/2024
 */
public final class GraphicsHandler {

    /**
     * The GraphicStyle enum defines the available styles
     */
    public enum GraphicStyle{

        DEFAULT,
        DARKMODE,
        GRAYSCALE,

    }

    /**
     * The currentStyle variable stores the current style
     */
    private static GraphicStyle currentStyle = GraphicStyle.DEFAULT;

    /**
     * The getGraphicStyle function returns the current style
     * @return the current style
     */
    public static GraphicStyle getGraphicStyle(){
        return currentStyle;
    }

    /**
     * The setGraphicsStyle function sets the current style
     * @param g the GraphicStyle object
     */
    public static void setGraphicsStyle(GraphicStyle g){
        currentStyle = g;
    }
}
