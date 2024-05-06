package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import parser.Model;
import parser.XMLReader;
/**
 * FileHandler class is responsible for handling files, such as reading, writing, converting and unzipping files.
 * It is also responsible for loading the files into the program.
 * It is a utility class and should not be instantiated.
 */
public class FileHandler {
    
    //buffer size for reading and writing in kb
    private static final int BUFFER_SIZE = 4096;
    protected String zipPath = null;
    private static final String osmPath = System.getProperty("user.dir").toString() + "\\src\\main\\resources\\files\\osmFile\\";
    private static String savePath = System.getProperty("user.dir").toString() + "\\src\\main\\resources\\files\\savedFile\\";
    public static Object getModel(File file){

        return loadUnknownFile(file);
        //return new Model(new XMLReader(new FileInputStream(file)));
    }

    /**
     * Checks if the file is a .bin file, if not it converts the file to a .bin file
     * @param file the file to load
     * @return the path to the binary file
     */
    private static Model loadUnknownFile(File file) {

        switch (getFileExtension(file)){
            case ".bin":
                break;
                //return loadBin(file);
            case ".zip":
                return Model.updateModelValues(getFileInputStream(unzip(file)));
            case ".osm", ".xml":
                return Model.updateModelValues(getFileInputStream(file));
                //return loadFile(file);
            default:
                System.out.println("File not supported");
        }
        return null;
    }

    /**
     * Loads Object from binary file
     */
    private static Object loadBin(File file) {
        return loadObject(file);
    }

    /**
     * Loads Object from zip file
     * @param file the file to load
     * @return the object loaded from the file
     */
    private static Object loadZip(File file){
        return loadFile(unzip(file));
    }

    /**
     * Takess file, reads it, converts it to binary, returns object loaded from the file
     * @param file the file to load
     * @return the object loaded from the file
     */
    private static Object loadFile(File file) {
        String path = savePath + getBinaryFileName(file);
        File binaryFile = new File(path);
        convertToBinary(file,binaryFile);
        System.out.println(path);
        return loadBin(new File(path));
    }

    /**
     * Gets the fileinputstream for the file
     * @param file the file to get the fileinputstream for
     * @return the fileinputstream for the file
     */
    private static FileInputStream getFileInputStream(File file) {
        try{
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Failed at Fileinputstream, with file" + file + " error: " + e.getMessage());
        }

        return null;
    }

    /**
     * Loads the object from the file using ObjectInputStream
     * @param file the file to load the object from
     * @return the object loaded from the file
     */
    private static Object loadObject(File file) {

        try {
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(getFileInputStream(file)));
            Object object = in.readObject();
            in.close();
            return  object;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed at loadModelStage" + e.getMessage());
        }

        return null;
    }




    /**
     * Takes file, reads it, returns appropriate name for the same file converted to binary
     * @param file the file you want the binary name for
     * @return binary name for the param
     */
    private static String getBinaryFileName(File file) {
        String binFileName = file.getName().substring(0,file.getName().length()-4) + ".bin";

        return binFileName;
    }

    /**
     * Getter for the fileextension of the file
     * @param file
     * @return extension of parameter
     */
    private static String getFileExtension(File file) {
        String fileExtension = file.getName().substring(file.getName().length()-4,file.getName().length());
        return fileExtension;
    }

    /**
     * Saves the XMLReader to a binary file
     * @param
     * @param destDir the directory to save the file
     */
    private static void convertToBinary(File file,File binaryFile) {
        Model.updateModelValues(getFileInputStream(file));
        new Thread(() -> {
            try (var out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(binaryFile)))) {
                long start = System.currentTimeMillis();
                out.writeObject(Model.getInstanceModel());
                System.out.println("Created object " + Model.getInstanceModel() + " in binary");
                long end = System.currentTimeMillis();
                System.out.println("Time to binary - total: " + (end - start) + "ms");

            } catch (Exception e) {
                System.out.println("Failed at converting stage: " + e.getMessage());
            }
        }).start();
    }

    private static void testDir(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
        }
    }

    /**
     * Unzips the file and extracts the .osm files to the osmFiles directory under resources
     * @param file the file to unzip
     * @throws IOException if the file is not found
     */
    private static File unzip(File file) {
        
        testDir(osmPath);

        String filePath = null;

        try {
            ZipInputStream zipIn = new ZipInputStream(getFileInputStream(file));
            ZipEntry entry = zipIn.getNextEntry();

            while(entry != null){
                filePath = osmPath + entry.getName();
                if(!entry.isDirectory() && entry.getName().contains(".osm") || entry.getName().contains(".xml")) { //only extract .osm or .xml files
                    extractFile(zipIn, filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();

                zipIn.close();
            }
        } catch (IOException e) {
            System.out.println("Failed at unzipping. Error: " + e.getMessage());
        }

        return new File(filePath);
    }

   /**
    * Extracts a file from a zip file
    * @param zipIn the zip file
    * @param filePath the path to extract the file to
    * @throws IOException
    */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException{
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while((read = zipIn.read(bytesIn)) != -1){
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    /**
     * Clears all saved files from the directory of where saved files are stored
     */
    private void clearSavedFiles() {
        File file = new File(savePath);
        if(file.isDirectory()){
            for(File f : file.listFiles()){
                f.delete();
            }
        }
    }
    /**
     * Clears all osm files from the directory of where osm files are stored
     */
    private void clearOsmFiles() {
        File file = new File(osmPath);
        System.out.println(file.listFiles());

        for(File f : file.listFiles()){
            f.delete();
        }
    }
}

