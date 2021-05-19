import java.util.stream.Collectors;

public class WorldProcessor {

    WorldMap map;

    public WorldProcessor(WorldMap map) {
        this.map = map;
    }

    /* Marks starved animals as dead, subtracts energy. */
    private void tickFood() {
        for (var ani : map.getAllAnimals()) {
            if (ani.isDead) continue;
            ani.energy -= Settings.moveEnergy;

            if (ani.energy < 0) {
                ani.isDead = true;
                ani.dayDied = map.day;
                continue;
            }
            ;

            ani.rotateByGenome();
            ani.moveByDirection();
        }
    }

    /* Makes animals eat */
    private void tickEat() {
        for (var field : map.getAllFields().stream().filter(field -> field.hasPlant).collect(Collectors.toList())) {
            var pos = field.position;
            var animalsAt = map.getAllAliveAnimalsAt(pos);
            if (animalsAt.isEmpty()) continue;

            var sorted = animalsAt.stream().sorted((a1, a2) -> a1.energy > a2.energy ? 1 : -1).collect(Collectors.toList());
            var max = sorted.get(0).energy;
            var willEat = sorted.stream().filter(ani -> ani.energy == max).collect(Collectors.toList());
            field.hasPlant = false;
            for (var ani : willEat) ani.energy += Settings.plantEnergy / willEat.size();

        }
    }

    private void tickKiss() throws InvalidPositionException {
        for (var pos : map.getAllPositions()) {
            var anis = map.animalLayer.get(pos);
            if (anis.size() < 2) continue;

            var sorted = anis.stream().sorted((a1, a2) -> a1.energy > a2.energy ? 1 : -1).collect(Collectors.toList());
            var a1 = sorted.get(0);
            var a2 = sorted.get(1);

            if (Animal.hasEnoughEnergyToKiss(a1, a2)) {
                var child = Animal.kiss(a1, a2);
                child.position = map.getNeighboringUnoccupied(a1.position);
                map.addAnimal(child);

                a1.children.add(child);
                a2.children.add(child);

                child.parentA = a1;
                child.parentB = a2;

                child.dayBorn = map.day;
            }
        }

    }

    private void tickGrowPlants() {
        //todo performance
        var junglePlantsWhole = Math.floor(Settings.junglePlantGrowth);
        var junglePlantsFract = map.day % (1 / Settings.junglePlantGrowth - Math.floor(Settings.junglePlantGrowth)) < 1 ? 1 : 0;

        for (int i = 0; i < junglePlantsWhole + junglePlantsFract; i++) {
            var jungles = map.getAllFields().stream().filter(field -> field.type == FieldType.JUNGLE && !field.hasPlant).collect(Collectors.toList());
            if (jungles.isEmpty()) break;
            jungles.get(Utils.getRandomInt(0, jungles.size())).hasPlant = true;
        }

        var steppePlantsWhole = Math.floor(Settings.steppePlantGrowth);
        var steppePlantsFract = map.day % (1 / Settings.steppePlantGrowth - Math.floor(Settings.steppePlantGrowth)) < 1 ? 1 : 0;

        for (int i = 0; i < steppePlantsWhole + steppePlantsFract; i++) {
            var steppes = map.getAllFields().stream().filter(field -> field.type == FieldType.STEPPE && !field.hasPlant).collect(Collectors.toList());
            if (steppes.isEmpty()) break;
            steppes.get(Utils.getRandomInt(0, steppes.size())).hasPlant = true;
        }

    }

    public void tick() throws InvalidPositionException {
        map.day += 1;
        tickFood();
        tickEat();
        tickKiss();
        tickGrowPlants();

    }
}
