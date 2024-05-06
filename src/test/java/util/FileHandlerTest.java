package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;

import parser.Model;

/**
 * WARNING: This test has to be run by itself due to the static nature of the Model class
 * This is because the Model class is a singleton and the tests will interfere with each other
 * If run together
 */
public class FileHandlerTest {
    
    @Test
    public void testGetModelFromOsm() {
        FileHandler.getModel(new File(FileDistributer.testMap.getFilePath()));
        assertTrue(FileDistributer.testMap.getFilePath().contains(".osm"));
        assertTrue(Model.getInstanceModel() != null);
    }

    @Test
    public void testGetModelFromZip() {
        FileHandler.clearOsmFiles();
        File file = new File(System.getProperty("user.dir").toString() + "\\src\\main\\resources\\files\\osmFile\\");
        int sizeBefore = file.listFiles().length;

        FileHandler.getModel(new File(FileDistributer.testMapInZip.getFilePath()));
        assertTrue(FileDistributer.testMapInZip.getFilePath().contains(".zip"));
        assertTrue(sizeBefore < file.listFiles().length);
    }

    @Test
    public void testClearOsmFiles() {
        FileHandler.getModel(new File(FileDistributer.testMapInZip.getFilePath()));
        File file = new File(System.getProperty("user.dir").toString() + "\\src\\main\\resources\\files\\osmFile\\");
        FileHandler.clearOsmFiles();
        
        assertEquals(0, file.listFiles().length);
    }
}
