package util;

import java.io.*;


import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import parser.Model;
import parser.XMLReader;

public class FileHandler {
    
    //buffer size for reading and writing in kb
    private static final int BUFFER_SIZE = 4096;
    protected String zipPath = null;

    private static final String osmPath = System.getProperty("user.dir").toString() + "\\src\\main\\resources\\files\\osmFile\\";
    private static String savePath = System.getProperty("user.dir").toString() + "\\src\\main\\resources\\files\\savedFile\\";
    private String absoluteBinaryFilePath;

    private static Model model;
    @Serial
    private static final long serialVersionUID = 1L;


    public static Object getModel(File file) throws IOException{
        model = (Model) loadUnknownFile(file);
        return model;
        //return new Model(new XMLReader(new FileInputStream(file)));
    }

    /**
     * Checks if the file is a .bin file, if not it converts the file to a .bin file
     * @param file the file to load
     * @return the path to the binary file
     */
    private static Object loadUnknownFile(File file) {

        switch (getFileExtension(file)){
            case ".bin":
                return loadBin(file);
            case ".zip":
                return loadZip(file);
            case ".osm", ".xml":
                return loadFile(file);
            default:
                System.out.println("File not supported");
        }
        return null;
    }

    private static Object loadBin(File file) {
        return loadObject(file);
    }

    private static Object loadZip(File file){
        return loadFile(unzip(file));
    }


    private static Object loadFile(File file) {
        String path = savePath + getBinaryFileName(file);
        convertToBinary(createModel(file),path);
        System.out.println(path);
        return loadBin(new File(path));
    }

    private static Model createModel(File file){

        createXMLReader(getFileInputStream(file));
        Model model = XMLReader.getModelInstance();
        System.out.println("Created model " + model);
        return model;
    }

    private static XMLReader createXMLReader(FileInputStream fIS){
        XMLReader reader = new XMLReader(fIS);
        System.out.println("Created XMLreader " + reader);
        return reader;
    }

    private static FileInputStream getFileInputStream(File file) {
        try{
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Failed at Fileinputstream, with file" + file + " error: " + e.getMessage());
        }

        return null;
    }

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
    private static void convertToBinary(Serializable objectToBeBinaried, String destDir) {
        try {

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bao);
            FileOutputStream binFile = new FileOutputStream(destDir);

            //bos.close();
            //var out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destDir)));
            out.writeObject(objectToBeBinaried);

            bao.writeTo(binFile);


            out.close();
            System.out.println("Created object " + objectToBeBinaried + " in binary");
        } catch (IOException e) {
            System.out.println("Failed at converting stage: " + e.getMessage());
        }
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

    private void clearSavedFiles() {
        File file = new File(savePath);
        if(file.isDirectory()){
            for(File f : file.listFiles()){
                f.delete();
            }
        }
    }
    private void clearOsmFiles() {
        File file = new File(osmPath);
        System.out.println(file.listFiles());

        for(File f : file.listFiles()){
            f.delete();
        }
    }
}

