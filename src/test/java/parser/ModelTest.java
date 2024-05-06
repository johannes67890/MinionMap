package parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.FileDistributer;
import util.FileHandler;

import java.io.File;

public class ModelTest {

    @BeforeEach
    void setUp() {
        synchronized(this) {
            new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
        }
    }
    
    @Test
    public void testGetNodes() {

        int size = XMLReader.getNodes().valueCollection().size();

        Model.updateModelValues(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
        assertNotNull(Model.getInstanceModel());
        assertDoesNotThrow(() -> Model.getInstanceModel());

        assertEquals(size, Model.getInstanceModel().getNodes().valueCollection().size());
    }

    @Test
    public void testGetBounds(){
        new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
        TagBound bound = XMLReader.getBound();

        Model.updateModelValues(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
        assertNotNull(Model.getInstanceModel());
        assertDoesNotThrow(() -> Model.getInstanceModel());
        
        assertTrue(bound.toString().contains(Model.getInstanceModel().getBound().toString()));
    }


}
