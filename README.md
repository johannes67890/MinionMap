<p align="center">
<img src="https://github.com/johannes67890/MapOfDenmark/tree/main/src/main/resources/visuals/minionEyes.png" alt="Minion Eyes" width="200" height="200">
</p>

# Minion Maps

Minion Map is a [Geographic information system](https://en.wikipedia.org/wiki/Geographic_information_system)  with the capablebility of displaying data from [OpenStreetMap](https://www.openstreetmap.org/) in the formats ZIP and OSM.

## Preview

<img src="https://github.com/johannes67890/MapOfDenmark/tree/main/src/main/resources/visuals/MinionMap.png" alt="Minion Map preview">

## Running the application
Download the [latest release]() as a .jar file.

To run the application, this file should be executed by opening it directly.

It is also possible to run it .jar file from the command line like so:

```
java -jar MapOfDenmark.jar
```

Keep in mind that the application depends on JavaFX, and you will have to have JavaFX SDK installed on your machine.
Here you will need to specify the path to the JavaFX SDK when you run the .jar file.
Do it like so:
    
```
java --module-path "path/to/javafx/sdk" --add-modules javafx.controls,javafx.fxml -jar MapOfDenmark.jar
```

### Running the application directly from source files

With the source files downloaded, run the following command in the root of the project.

```
./gradlew run
```

### Building an executable jar from source

```
./gradlew jar
```

### Running the generated executable `.jar`
Assuming an executable `.jar` file is located at the default location, run the following command at the project root:

```
java -jar build/libs/MapOfDenmark.jar
```

### Running test suite

To run the test suite, run the following command:
```
./gradlew test
```

To generate a test coverage report using [JaCoCo](https://www.eclemma.org/jacoco/), run `gradlew test` and then run the following command:

```
./gradlew jacocoTestReport
```

The report is viewed by opening the generated `build\reports\jacoco\test\html\index.html` HTML file in your browser.
