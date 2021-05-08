import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class WorldMap implements IPositionObserver<Animal> {

    public final int width = Settings.width;
    public final int height = Settings.height;

    final MapLayer<Field> fieldLayer = new MapLayer<>();
    final MapMultiLayer<Animal> animalLayer = new MapMultiLayer<>();

    int day = 0;

    public WorldMap() {

    }

    public boolean hasPlant(Vector2 pos) {
        return fieldLayer.get(pos) != null && fieldLayer.get(pos).hasPlant;
    }

    public boolean hasAnimal(Vector2 pos) {
        return !animalLayer.isEmpty(pos);
    }

    public void onPositionChange(Animal animal, Vector2 from, Vector2 to) {
        animalLayer.remove(from, animal);
        animalLayer.add(to, animal);
    }

    public Animal addAnimal(Animal animal) {
        animalLayer.add(animal.position, animal);
        animal.addPositionObserver(this);
        return animal;
    }

    public Animal[] addAnimals(Animal ...animals) {
        for(var animal : animals) {
            animalLayer.add(animal.position, animal);
            animal.addPositionObserver(this);
        }
        return animals;
    }

    public Field addField(Field field) {
        fieldLayer.add(field.position, field);
        return field;
    }

    public List<Animal> getAllAnimals(){
        return animalLayer.getAll();
    }
    public List<Animal> getAllAliveAnimals() {
        return animalLayer.getAll().stream().filter(ani -> !ani.isDead).collect(Collectors.toList());
    }

    public List<Field> getAllFields(){
        return fieldLayer.getAll();
    }

    public Set<Animal> getAllAnimalsAt (Vector2 pos) {
        return animalLayer.get(pos);
    }
    public Set<Animal> getAllAliveAnimalsAt (Vector2 pos) {
        return animalLayer.get(pos).stream().filter(ani -> !ani.isDead).collect(Collectors.toSet());
    }

    public List<Vector2> getAllPositions() {
        var list = new ArrayList<Vector2>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                list.add(new Vector2(j, i));
            }
        }
        return list;
    }

    public static WorldMap createRandom() throws InvalidPositionException {
        var map = new WorldMap();
        for (int i = 0; i < Settings.width; i++) {
            for (int j = 0; j < Settings.height; j++) {
                map.addField(new Field(new Vector2(i, j), Math.random() > Settings.jungleRatio ? FieldType.STEPPE : FieldType.JUNGLE));
            }
        }
        return map;
    }

    public Vector2 getNeighboringUnoccupied(Vector2 center){
        for (var direction : Vector2.directions) {
            var shifted = Vector2.sum(center, direction);
            shifted.boundarize();

            if (animalLayer.isEmpty(shifted)) return shifted;
        }

        return center;
    }
}

