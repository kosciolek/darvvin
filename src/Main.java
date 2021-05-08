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
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    WorldProcessor processor1;
    WorldCanvas canvas1;

    boolean isRunning = false;
    Timer timer = new Timer();

    Text text1 = new Text("MAP STATS");
    Text textAnimal1 = new Text("ANIMAL STATS");

    Animal tracked;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Drawing Operations Test");

        var root = new VBox(16);
        var hBox = new HBox(16);
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
        hBox.getChildren().add(textAnimal1);
        processor1 = new WorldProcessor(map);

        canvas1.setOnMouseClicked(e -> {
            int x = Settings.width - (int) e.getX() / (500 / Settings.width) - 1;
            int y = Settings.height - (int) e.getY() / (500 / Settings.height) - 1;
            map.animalLayer.get(new Vector2(x, y)).stream().filter(ani -> !ani.isDead).findFirst().ifPresent(animal -> {
                tracked = animal;
            });
        });


        stop1.setOnMouseClicked(e -> {
            if (isRunning) {
                timer.cancel();
                isRunning = false;
            } else {
                timer = new Timer();
                isRunning = true;
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            processor1.tick();
                        } catch (InvalidPositionException invalidPositionException) {
                            invalidPositionException.printStackTrace();
                        }

                        text1.setText(String.format("MAP STATS\nDay: %d\nAnimals (alive): %d\nAnimals (dead): %d\nPlants: %d\nEnergy (avg): %4.3f\nDays lived (avg | only dead): %4.3f\nImmediate children (avg | only alive): %4.3f", map.day, map.getAliveAnimalCount(), map.getDeadAnimalsCount(), map.getPlantCount(), map.getAverageEnergy(), map.averageDaysLived(), map.averageImmediateChildrenCount()));

                        if (tracked != null)
                            textAnimal1.setText(String.format("ANIMAL STATS\nColor: %s\nEnergy: %d\nImmediate children: %d\nAll children: %d\n", tracked.favoriteColor.toString(), tracked.energy, tracked.children.size(), tracked.getAllChildrenCount()));
                        else textAnimal1.setText("No animal selected :(");
                        canvas1.render();

                    }
                }, 0, Settings.tickIntervalMs);
            }
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
