// package util;

// import static org.junit.jupiter.api.Assertions.*;

// import java.io.FileInputStream;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import gui.DrawingMap;
// import gui.MainView;
// import gui.MapView;
// import javafx.scene.canvas.Canvas;
// import parser.TagBound;
// import parser.XMLReader;

// public class DrawingMapTest {

//     XMLReader reader;

//     Canvas canvas;

//     DrawingMap drawingMap;
    
//     @BeforeEach
//     void testXMLReaderStartup() {
//         try{

//             this.reader = new XMLReader(new FileInputStream("src/main/ressources/testMap.osm"));
//         }
//         catch(Exception e){
            
//         }
//         assertNotNull(reader);
//         assertDoesNotThrow(() -> this.reader);
//         MainView mainView = new MainView();

//         drawingMap = ((MapView ) mainView.getView()).getDrawingMap();
//     }


//     @Test
//     void getScreenBounds() {

//         float[] bounds = drawingMap.getScreenBounds();

//         TagBound bound = XMLReader.getBound();



//         assertEquals(bound.getMinLon(), bounds[0]);
//         assertEquals(bound.getMaxLon(), bounds[2]);
//     }

//     @Test
//     void getScreenBoundsBigger() {
//         drawingMap.initialize(canvas);

//         float[] bounds = drawingMap.getScreenBounds();


//         float[] boundsBigger = drawingMap.getScreenBoundsBigger(0.2);

//         TagBound bound = XMLReader.getBound();

//         assertEquals(bounds[0] - ((bounds[2] - bounds[0]) * 1.2), boundsBigger[0]);
//         assertEquals(bound.getMaxLon(), bounds[2]);
//     }

//     @Test
//     void zoom() {
//         drawingMap.initialize(canvas);

//         TagBound bound = XMLReader.getBound();

//         double zoomlevel = drawingMap.getZoomLevel();

//         assertEquals(drawingMap.getCanvas().getWidth() / (bound.getMaxLon() - bound.getMinLon()), zoomlevel);

//         drawingMap.zoom(1000000000, 0, 0);

//         zoomlevel = drawingMap.getZoomLevel();

//         assertEquals(drawingMap.getZoomLevelMax() -0.000001, zoomlevel);

//         drawingMap.zoom(0.0000000000001, 0, 0);

//         zoomlevel = drawingMap.getZoomLevel();

//         assertEquals(drawingMap.getZoomLevelMin() + 0.000001, zoomlevel);
//     }

//     @Test
//     void pan() {
//         drawingMap.initialize(canvas);

//         TagBound bound = XMLReader.getBound();

//         double zoomlevel = drawingMap.getZoomLevel();

//         assertEquals(drawingMap.getCanvas().getWidth() / (bound.getMaxLon() - bound.getMinLon()), zoomlevel);

//         drawingMap.zoom(1000000000, 0, 0);

//         zoomlevel = drawingMap.getZoomLevel();

//         assertEquals(drawingMap.getZoomLevelMax() -0.000001, zoomlevel);

//         drawingMap.zoom(0.0000000000001, 0, 0);

//         zoomlevel = drawingMap.getZoomLevel();

//         assertEquals(drawingMap.getZoomLevelMin() + 0.000001, zoomlevel);
//     }






    
// }
