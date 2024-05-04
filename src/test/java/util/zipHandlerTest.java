package util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import structures.ZipHandler;

import java.io.File;

public class zipHandlerTest {

    @BeforeEach
    public void setUp(){
        String directoryOSM = System.getProperty("user.dir").toString() + "\\src\\main\\resources\\osmFiles";
        File file = new File(directoryOSM);
        
        if(file.list().length > 0){
            File[] files = file.listFiles();
            for(File f : files){
                f.delete();
            }
        }
    }
    
    
    @Test
    public void testUnzip() {
        ZipHandler zipHandler = new ZipHandler();
        String testZipPath = System.getProperty("user.dir").toString() + "\\src\\test\\java\\org\\test_resources\\map.zip";

        try{
            zipHandler.unzip(testZipPath);
        } catch (Exception e){
            e.printStackTrace();
        }
        
        assertFalse(zipHandler.getDestDir().isEmpty());
    }

}
