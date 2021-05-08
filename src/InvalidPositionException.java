public class InvalidPositionException extends Exception{
    InvalidPositionException(Vector2 vec) {
        super("Position is invalid: " + vec.x + " | " + vec.y);
    }
}
