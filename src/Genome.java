import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Genome {
    private List<Integer> genome;

    public Genome(List<Integer> genome){
        this.genome = genome;
    }

    public static Genome createRandom() {
        var list = new ArrayList<Integer>();
        var random = new Random();
        for (int i = 0; i < 32; i++) {
            list.add(Utils.getRandomInt(random, 0, 8));
        }

        return new Genome(list);
    }

    public static int generateRandomGene() {
        var random = new Random();
        return Utils.getRandomInt(random, 0, 8);
    }

    public int getRandomFromGenome() {
        var random = new Random();
        return genome.get(Utils.getRandomInt(random, 0, genome.size()));
    }

    public static Genome mix(Genome g1, Genome g2){
        var random = new Random();
        var i1 = Math.abs(random.nextInt()) % 32;
        var i2 = Math.abs(random.nextInt()) % 32;
        while (i1 == i2) i2 = Math.abs(random.nextInt()) % 32;

        var l = Math.min(i1, i2);
        var r = Math.max(i1, i2);

        var p1 = g1.genome.subList(0, l);
        var p2 = g1.genome.subList(i1, r);

        var newGenome = new ArrayList<Integer>();
        newGenome.addAll(p1);
        newGenome.addAll(p2);

        while (newGenome.size() != 32) {
            newGenome.add(generateRandomGene());
        }
        //todo assure there's at least 1 unit of every gene
        return new Genome(newGenome);
    }

    @Override
    public String toString() {
        return "[" + genome.stream().map(String::valueOf).collect(Collectors.joining(",")) + "]";
    }
}
