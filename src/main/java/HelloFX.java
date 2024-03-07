import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        var input = new TextField();
        var output = new TextArea();
        var pane = new BorderPane();

        pane.setTop(input);
        pane.setCenter(output);

        input.setOnAction(e->{
            var a = Address.parse(input.getText());
            output.setText(a.toString());
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