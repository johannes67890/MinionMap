package parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import util.FileDistributer;
import util.FileHandler;

public class ModelTest {

    @BeforeEach
    void setUp() {
        Model.updateModelValues(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
        assertNotNull(Model.getInstanceModel());
        assertDoesNotThrow(() -> Model.getInstanceModel());
    }
}
