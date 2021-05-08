import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Main extends Application {

    WorldProcessor processor1;
    WorldCanvas canvas1;


    Text text1 = new Text("Here we will show statistics for the first map.");

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Drawing Operations Test");

        var root = new VBox();
        var hBox = new HBox();
        root.getChildren().add(hBox);
        var stop1 = new Button("Stop MAP1");
        var stop2 = new Button("Stop MAP2");
        var controlsContainer = new HBox(stop1, stop2);
        root.getChildren().add(controlsContainer);
        var scene = new Scene(root);
        primaryStage.setScene(scene);



        var map = WorldMap.createRandom();
        for (int i = 0; i < Settings.height * Settings.width / 10; i++) {
            var field = map.fieldLayer.get(Vector2.getRandom());
            field.hasPlant = true;
        }

        for (int i = 0; i < Settings.startingAnimals; i++) {
            var animal = new Animal(Vector2.getRandom(), Genome.createRandom(), Vector2.N);
            animal.energy = Settings.startEnergy;
            map.addAnimal(animal);
        }

        canvas1 = new WorldCanvas(500, 500, map);
        hBox.getChildren().add(canvas1);
        hBox.getChildren().add(text1);
        processor1 = new WorldProcessor(map);

        canvas1.setOnMouseClicked(e -> {
            try {
                processor1.tick();
            } catch (InvalidPositionException invalidPositionException) {
                invalidPositionException.printStackTrace();
            }
            String str = "";
            for (var animal : map.animalLayer.getAll().stream().filter(animal -> !animal.isDead).collect(Collectors.toList())){
                str += animal.energy + "\n";
            }

            text1.setText(str);
            canvas1.render();
            System.out.println("clicked");
        });


        var map2 = WorldMap.createRandom();
        map2.addAnimal(new Animal(Vector2.getRandom()));
        var canvas2 = new WorldCanvas(500, 500, map2);
        hBox.getChildren().add(canvas2);
        hBox.getChildren().add(new Text("Here we will show statistics for the second map."));

        primaryStage.show();

    }

    public static void main(String[] args) throws InvalidPositionException, IOException, ParseException {
        Settings.loadFromFile(Paths.get("settings.json"));

        launch(args);

    }
}
