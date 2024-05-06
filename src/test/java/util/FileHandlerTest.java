package util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Tag;

import parser.Model;

public class FileHandlerTest {
    
    @BeforeEach
    public void setUp() {
        
    }

    @Test
    public void testClearOsmFiles() {
        File testFile = new File(System.getProperty("user.dir").toString() + "\\src\\main\\resources\\files\\osmFile\\testMap.osm");
        File file = new File(System.getProperty("user.dir").toString() + "\\src\\main\\resources\\files\\osmFile\\");
        
        assertTrue(testFile.exists());
        assertTrue(file.listFiles().length > 0);

        FileHandler.clearOsmFiles();
        
        assertEquals(0, file.listFiles().length);
    }

    @Test
    public void testGetModelFromOsm() {
        FileHandler.getModel(new File(FileDistributer.testMap.getFilePath()));
        assertTrue(FileDistributer.testMap.getFilePath().contains(".osm"));
        assertTrue(Model.getInstanceModel().getNodes().valueCollection().size() > 0);
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
}
