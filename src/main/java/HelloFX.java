import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {

        ArrayList<String> cityNames = new ArrayList<>();
        ArrayList<String> streetNames = new ArrayList<>();
        ArrayList<String> postCodes = new ArrayList<>();

        try{

            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/citynames.txt"));
            while (reader.ready()) {
                cityNames.add(reader.readLine());
            }
            reader.close();
            reader = new BufferedReader(new FileReader("src/main/resources/streetnames.txt"));
            while (reader.ready()){
                streetNames.add(reader.readLine());
            }
            reader.close();
            reader = new BufferedReader(new FileReader("src/main/resources/postnumre.txt"));
            while (reader.ready()){
                postCodes.add(reader.readLine());
            }
            reader.close();
        
        } catch(IOException e){
            System.out.println("FILE NOT FOUND");
        }

        


        var input = new TextField();
        var output = new TextArea();
        var pane = new BorderPane();

        pane.setTop(input);
        pane.setCenter(output);

        //Instantiates Address
        input.setOnAction(e->{
            Address a = Address.parse(input.getText());
            output.setText(a.toString());

            if(cityNames.contains(a.city)){
                System.out.println("CITY FOUND: " + a.city);
            } else{System.out.println("This City: " + a.city + " does not exist");}
            
        });

        var scene = new Scene(pane);

        stage.setTitle("Address Parsing");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}