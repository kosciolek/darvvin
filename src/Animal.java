import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Animal extends Entity {

    List<IPositionObserver<Animal>> positionObservers = new ArrayList<>();

    public Set<Animal> children = new HashSet<>();

    Genome genome = Genome.createRandom();

    public int energy;

    public boolean isDead = false;

    public Vector2 direction = Vector2.N;

    public Animal parentA;
    public Animal parentB;

    public int dayBorn = 0;
    public int dayDied;

    public Color favoriteColor = new Color(Math.random(), Math.random(), Math.random(), 1.0);


    public Animal(){
        super();
    }

    public Animal(Vector2 startPos) throws InvalidPositionException {
        super(startPos);
    }

    public Animal(Vector2 startPos, Genome genome) throws InvalidPositionException {
        super(startPos);
        this.genome = genome;
    }

    public Animal(Vector2 startPos, Genome genome, Vector2 direction) throws InvalidPositionException {
        super(startPos);
        this.genome = genome;
        this.direction = direction;
    }

    void onPositionChange(Vector2 from, Vector2 to) {
        for (var observer : positionObservers) observer.onPositionChange(this, from, to);
    }

    public void move(Vector2 delta) {
        var prevPosition = position;
        var newPos =Vector2.sum(position, delta);
        newPos.boundarize();
        position = newPos;
        onPositionChange(prevPosition, position);
    }

    public void moveByDirection() {
        move(direction);
    }

    public static boolean hasEnoughEnergyToKiss(Animal a1, Animal a2) {

        if (!a1.position.equals(a2.position)) return false;
        if (a1.energy < Math.floor(Settings.startEnergy * 0.5) || a2.energy < Math.floor(Settings.startEnergy * 0.5)) return false;

        return true;
    }

    public void rotateByGenome() {
        var rotation = genome.getRandomFromGenome();
        direction.rotate(rotation);
    }

    public static Animal kiss(Animal a1, Animal a2) throws InvalidPositionException {
        var animal = new Animal();
        animal.genome = Genome.mix(a1.genome, a2.genome);

        int a1Portion = (int)Math.floor(a1.energy * 0.25);
        int a2Portion = (int)Math.floor(a1.energy * 0.25);
        a1.energy -= a1Portion;
        a2.energy -= a2Portion;
        animal.energy = a1Portion + a2Portion;

        return animal;
    }

    public void addPositionObserver(IPositionObserver<Animal> observer) {
        positionObservers.add(observer);
    }

    public void removePositionObserver(IPositionObserver<Animal> observer){
        positionObservers.remove(observer);
    }

}
