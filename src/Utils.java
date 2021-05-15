import java.util.Random;

public class Utils {
    public static Random random = new Random();

    public static int getRandomInt(Random random, int minInclusive, int maxExclusive) {
        return minInclusive + (int)Math.floor(Math.abs(random.nextInt())) % (maxExclusive - minInclusive);
    }

    public static int getRandomInt(int minInclusive, int maxExclusive) {
        return minInclusive + (int)Math.floor(Math.abs(random.nextInt())) % (maxExclusive - minInclusive);
    }
}
