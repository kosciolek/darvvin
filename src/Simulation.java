import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Simulation extends Group {

    Text mapStats = new Text("");
    Text animalStats = new Text("");
    WorldCanvas canvas;
    WorldProcessor processor;
    WorldHistory history = new WorldHistory();
    Button stopBtn = new Button("Start");

    Animal tracked;
    boolean isRunning = false;

    Timer timer;

    public Simulation () throws Exception {
        var rootBox = new HBox(16);
        var rightBox = new VBox(16);
        var exportBtn = new Button("Export to JSON");
        var btnBox = new HBox(16, stopBtn, exportBtn);
        rightBox.getChildren().add(btnBox);
        rightBox.getChildren().add(mapStats);
        rightBox.getChildren().add(animalStats);



        this.getChildren().add(rootBox);

        exportBtn.setOnMouseClicked(e -> {
            history.export();
        });

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

        canvas = new WorldCanvas(500, 500, map);

        rootBox.getChildren().add(canvas);
        rootBox.getChildren().add(rightBox);
        processor = new WorldProcessor(map);

        canvas.setOnMouseClicked(e -> {
            int x = Settings.width - (int) e.getX() / (500 / Settings.width) - 1;
            int y = Settings.height - (int) e.getY() / (500 / Settings.height) - 1;
            map.animalLayer.get(new Vector2(x, y)).stream().filter(ani -> !ani.isDead).findFirst().ifPresent(animal -> {
                tracked = animal;
            });
            updateStats();
        });


        stopBtn.setOnMouseClicked(e -> {
            if (isRunning) {
                timer.cancel();
                isRunning = false;
                stopBtn.setText("Start");
            } else {
                timer = new Timer();
                isRunning = true;
                stopBtn.setText("Stop");
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            processor.tick();
                        } catch (InvalidPositionException invalidPositionException) {
                            invalidPositionException.printStackTrace();
                        }
                        updateStats();

                        var map = canvas.map;
                        int aliveAnimalCount = map.getAliveAnimalCount();
                        double averageImmediateChildren = map.averageImmediateChildrenCount();
                        double averageDaysLived = map.averageDaysLived();
                        int plantCount = map.getPlantCount();
                        int deadAnimals = map.getDeadAnimalsCount();
                        double avgEnergy = map.avgEnergy();
                        history.addDay(aliveAnimalCount + deadAnimals, deadAnimals, aliveAnimalCount, plantCount, avgEnergy, averageImmediateChildren, averageDaysLived);

                    }
                }, 0, Settings.tickIntervalMs);
            }
        });

        updateStats();
    }

    private void updateStats() {
        var map = canvas.map;
        var day = map.day;
        int aliveAnimalCount = map.getAliveAnimalCount();
        double averageImmediateChildren = map.averageImmediateChildrenCount();
        double averageDaysLived = map.averageDaysLived();
        int plantCount = map.getPlantCount();
        int deadAnimals = map.getDeadAnimalsCount();
        double avgEnergy = map.avgEnergy();
        mapStats.setText(String.format("MAP STATS\nDay: %d\nAnimals (alive): %d\nAnimals (dead): %d\nPlants: %d\nEnergy (avg): %4.3f\nDays lived (avg | only dead): %4.3f\nImmediate children (avg | only alive): %4.3f", day, aliveAnimalCount, deadAnimals, plantCount, avgEnergy, averageDaysLived, averageImmediateChildren));

        if (tracked != null)
            animalStats.setText(String.format("ANIMAL STATS\nColor: %s\nEnergy: %d\nImmediate children: %d\nAll children: %d\n", tracked.favoriteColor.toString(), tracked.energy, tracked.children.size(), tracked.getAllChildrenCount()));
        else animalStats.setText("No animal selected :(");
        canvas.render();
    }
}
