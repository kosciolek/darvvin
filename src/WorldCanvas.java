import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class WorldCanvas extends Canvas {
    enum TileVariant {
        NORMAL,
        SMALL_CENTER,
        SMALL_TOP
    }


    WorldMap map;

    public WorldCanvas(double width, double height, WorldMap worldMap) {
        super(width, height);
        this.map = worldMap;
        render();
    }


    private void paintTile(Vector2 pos, Color color, TileVariant tileVariant) {
        var gc = getGraphicsContext2D();
        var tileWidth = getWidth() / Settings.width;
        var tileHeight = getHeight() / Settings.height;
        var x = getWidth() - tileWidth - tileWidth * pos.x;
        var y = getHeight() - tileHeight - tileHeight * pos.y;

        if (tileVariant == TileVariant.SMALL_CENTER) {
            tileWidth /= 3;
            tileHeight /= 3;

            x += tileWidth;
            y += tileHeight;
        } else if (tileVariant == TileVariant.SMALL_TOP) {
            tileWidth /= 4;
            tileHeight /= 4;

            x += tileWidth * 3;

        }

        gc.setFill(color);
        gc.fillRect(x, y, tileWidth, tileHeight);
    }

    private void paintProgressBar(Vector2 tilePos, double current, double max) {
        var gc = getGraphicsContext2D();
        var tileWidth = getWidth() / Settings.width;
        var tileHeight = getHeight() / Settings.height;
        var x = getWidth() - tileWidth - tileWidth * tilePos.x;
        var y = getHeight() - tileHeight - tileHeight * tilePos.y;

        tileHeight /= 4;
        y += tileHeight * 3;

        var percent = Math.max(0, Math.min(1, current / max));
        var color = new Color(percent, percent, percent, 1.0);

        tileWidth *= percent;

        gc.setFill(color);
        gc.fillRect(x, y, tileWidth, tileHeight);
    }

    public void highlightDominantGenomes() {
        var dominantGenome = map.getDominantGenome();
        if (dominantGenome == null) return;

        for (var animal : map.getAllAliveAnimals().stream().filter(ani -> ani.genome.toString().equals(dominantGenome)).collect(Collectors.toList())) {
            paintTile(animal.position, Palette.DOMINANT_GENOME_HIGHLIGHT, TileVariant.SMALL_CENTER);
        }
    }

    public void render() {

        /* Reset */
        for (int i = 0; i < Settings.width; i++) {
            for (int j = 0; j < Settings.height; j++) {
                paintTile(new Vector2(i, j), Palette.EMPTY, TileVariant.NORMAL);
            }
        }

        for (var field : map.getAllFields()) {
            paintTile(field.position, field.type == FieldType.JUNGLE ? Palette.JUNGLE : Palette.STEPPE, TileVariant.NORMAL);
        }

        for (var animal : map.getAllAliveAnimals()) {
            paintTile(animal.position, Palette.ANIMAL, TileVariant.NORMAL);
        }

        for (var animal : map.animalLayer.getAll().stream().filter(ani -> ani.dayBorn == map.day).collect(Collectors.toList())) {
            paintTile(animal.position, Palette.ANIMAL_NEWBORN, TileVariant.NORMAL);
        }

        for (var animal : map.animalLayer.getAll().stream().filter(ani -> ani.isDead && ani.dayDied == map.day).collect(Collectors.toList())) {
            paintTile(animal.position, Palette.ANIMAL_DEAD, TileVariant.NORMAL);
        }

        for (var animal : map.getAllAliveAnimals()) {
            paintTile(animal.position, animal.favoriteColor, TileVariant.SMALL_TOP);
        }

        for (var animal : map.getAllAliveAnimals()) {
            paintProgressBar(animal.position, animal.energy, 50);
        }

        for (var field : map.getAllFields().stream().filter(field -> field.hasPlant).collect(Collectors.toList())) {
            paintTile(field.position, Palette.PLANT, TileVariant.SMALL_CENTER);
        }
    }


}
