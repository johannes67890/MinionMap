package util;

import java.net.URISyntaxException;
import java.net.URL;

public enum FileDistributer {
    // Views
    main("view/main.fxml"),
    start_screen("view/start.fxml"),
    copenhagenZip("files/defaultFile/Copenhagen.zip"),
    bornholmZip("files/defaultFile/map.zip"),
    // Test Files
    testMap("testMap.osm"),
    testMapInZip("testMap.zip"),
    RebakSopark("testRebakSopark.osm");



    private String filePath;

    /**
     * Constructor for the FileDistributer class
     * @param fileName the name of the file
     */
    FileDistributer(String fileName) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource(fileName);
            if (resource == null) {
                throw new IllegalArgumentException("file not found! " + fileName);
            } else {
                this.filePath = resource.toURI().getPath();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public String getFilePath() {
        return filePath.toString();
    }
}
