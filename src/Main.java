import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Darvvin");


        var simulation = new Simulation();
        var simulation2 = new Simulation();
        var box = new VBox(16);
        box.getChildren().add(simulation);
        box.getChildren().add(simulation2);
        var scene = new Scene(box);
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    public static void main(String[] args) throws IOException, ParseException {
        Settings.loadFromFile(Paths.get("settings.json"));

        launch(args);

    }
}

