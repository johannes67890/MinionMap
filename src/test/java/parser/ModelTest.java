package parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Tag;

import util.FileDistributer;
import util.FileHandler;


import java.io.File;

import util.FileDistributer;
import util.FileHandler;

public class ModelTest {

    
    @Test
    public void testGetNodes() {

        new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
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
