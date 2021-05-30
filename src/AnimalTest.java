import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {

    private Animal ani;


    @BeforeAll
    public static void beforeAll() throws IOException, ParseException {
        // testy nie powinny zalezec od settings
        Settings.loadFromFile(Paths.get("settings.json"));
    }

    @BeforeEach
    public void setUp() throws InvalidPositionException {
        ani = new Animal(new Vector2(2, 2));
    }

    @Test
    public void move() {
        assertEquals(ani.position, new Vector2(2, 2));
        ani.move(Vector2.E);
        assertEquals(ani.position, new Vector2(3, 2));
    }

    @Test
    public void moveByDirection() {
        ani.direction = Vector2.NE;
        assertEquals(ani.position, new Vector2(2, 2));
        ani.moveByDirection();
        assertEquals(ani.position, new Vector2(3, 3));
    }
}
