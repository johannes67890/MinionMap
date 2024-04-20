package gui;

public final class GraphicsHandler {

    public enum GraphicStyle{

        DEFAULT,
        DARKMODE,
        GRAYSCALE,

    }

    private static GraphicStyle currentStyle = GraphicStyle.DEFAULT;

    public static GraphicStyle getGraphicStyle(){
        return currentStyle;
    }

    public static void setGraphicsStyle(GraphicStyle g){

        currentStyle = g;
    }

    
}
