package structures;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipHandler {

    //buffer size for reading and writing in mb
    private static final int BUFFER_SIZE = 4096;
    protected String zipPath = null;
    protected String destDir = null;


    /**
     * Unzips the file and extracts the .osm files to the osmFiles directory under resources
     * @param zipPath path to the zip file
     * @throws IOException if the file is not found
     */
    public void unzip(String zipPath) throws IOException{

        this.destDir = System.getProperty("user.dir").toString() + "\\src\\main\\resources\\osmFiles\\";

        File file = new File(destDir);
        if(!file.exists()){
            file.mkdir();
        }
        
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipPath));
        ZipEntry entry = zipIn.getNextEntry();

        while(entry != null){
            String filePath = destDir + entry.getName();

            if(!entry.isDirectory() && entry.getName().contains(".osm")){ //only extract .osm files
                extractFile(zipIn, filePath);
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    /**
     * Extracts the file from the zip
     * @param zipIn the zip input stream
     * @param filePath 
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException{
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while((read = zipIn.read(bytesIn)) != -1){
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public String getDestDir(){
        return this.destDir;
    }
}
