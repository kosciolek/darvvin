import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulation extends Group {

    Text mapStats = new Text("");
    Text animalStats = new Text("");
    WorldCanvas canvas;
    WorldProcessor processor;
    WorldHistory history = new WorldHistory();
    Button stopBtn = new Button("Start");
    Button highlightDominantBtn = new Button("Highlight dominant genomes");

    Animal tracked;
    boolean isRunning = false;

    Timer timer;

    public Simulation() throws Exception {
        var rootBox = new HBox(16);
        var rightBox = new VBox(16);
        var exportBtn = new Button("Export to JSON");
        var btnBox = new HBox(16, stopBtn, exportBtn, highlightDominantBtn);
        rightBox.getChildren().add(btnBox);
        rightBox.getChildren().add(mapStats);
        rightBox.getChildren().add(animalStats);


        highlightDominantBtn.setOnMouseClicked(e -> {
            canvas.highlightDominantGenomes();
        });

        this.getChildren().add(rootBox);

        exportBtn.setOnMouseClicked(e -> {

            try {
                history.export();

                System.out.println("Exported to exported.json");
                var alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Exported to exported.json");
                alert.show();
            } catch (IOException ioException) {
                System.out.println("An error has occurred while exporting.");
                var alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("An error has occurred while exporting.");
                alert.show();
            }
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

        ExecutorService pool = Executors.newCachedThreadPool();

        processor.addTickListener((worldMap) -> {
            updateStats();

            pool.execute(() -> {
                var map1 = canvas.map;
                int aliveAnimalCount = map1.getAliveAnimalCount();
                double averageImmediateChildren = map1.averageImmediateChildrenCount();
                double averageDaysLived = map1.averageDaysLived();
                int plantCount = map1.getPlantCount();
                int deadAnimals = map1.getDeadAnimalsCount();
                double avgEnergy = map1.avgEnergy();
                String dominantGenome = map1.getDominantGenome();
                if (dominantGenome == null) dominantGenome = "NO GENOME";
                history.addDay(aliveAnimalCount + deadAnimals, deadAnimals, aliveAnimalCount, plantCount, avgEnergy, averageImmediateChildren, averageDaysLived, dominantGenome);
            });
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

                        processor.tick();
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
        String dominantGenome = map.getDominantGenome();
        if (dominantGenome == null) {
            dominantGenome = "NO DOMINANT GENOME";
        }

        mapStats.setText(String.format("MAP STATS\nDay: %d\nAnimals (alive): %d\nAnimals (dead): %d\nPlants: %d\nEnergy (avg): %4.3f\nDays lived (avg | only dead): %4.3f\nImmediate children (avg | only alive): %4.3f\nDominant genome (only alive): %s", day, aliveAnimalCount, deadAnimals, plantCount, avgEnergy, averageDaysLived, averageImmediateChildren, dominantGenome));

        if (tracked != null)
            animalStats.setText(String.format("ANIMAL STATS\nColor: %s\nEnergy: %d\nImmediate children: %d\nAll children: %d\n", tracked.favoriteColor.toString(), tracked.energy, tracked.children.size(), tracked.getAllChildrenCount()));
        else animalStats.setText("No animal selected :(");
        canvas.render();
    }
}
