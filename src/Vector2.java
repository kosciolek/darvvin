import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Vector2 {
    public int x;
    public int y;

    public final static Vector2 N = new Vector2(0, 1);
    public final static Vector2 NE = new Vector2(1, 1);
    public final static Vector2 E = new Vector2(1, 0);
    public final static Vector2 SE = new Vector2(1, -1);
    public final static Vector2 S = new Vector2(0, -1);
    public final static Vector2 SW = new Vector2(-1, -1);
    public final static Vector2 W = new Vector2(-1, 0);
    public final static Vector2 NW = new Vector2(-1, 1);

    public final static Vector2 IDENTITY = new Vector2(0, 0);


    public final static List<Vector2> directions = Arrays.asList(N, NE, E, SE, S, SW, W, NW);

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 sum(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    public boolean isValid() {
        return !(y < 0 || y >= Settings.height || x < 0 || x >= Settings.width);
    }

    public void boundarize(){
        //todo hard or soft bounds?

        // hard boundaries
        /*x = Math.min(Settings.height - 1, Math.max(0, x));
        y = Math.min(Settings.width - 1, Math.max(0, y));*/

        // Wrapping boundaries
        x = (x + Settings.width) % Settings.width;
        y = (y + Settings.height) % Settings.height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2)) return false;
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public static Vector2 getRandomDirection(){
        return directions.get(Math.abs(new Random().nextInt()) % directions.size());

    }

    public static Vector2 getRandom() {
        var random = new Random();

        var x = (int)Math.floor(Math.abs(random.nextInt())) % Settings.width;
        var y = (int)Math.floor(Math.abs(random.nextInt())) % Settings.height;
        return new Vector2(x, y);
    }

    public void rotate(int steps){
        int index = 0;
        while (!this.equals(directions.get(index))) index++;

        var rotatedIndex = (directions.size() + index + steps) % directions.size();
        var rotatedVec = directions.get(rotatedIndex);
        this.x = rotatedVec.x;
        this.y = rotatedVec.y;
    }


    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
