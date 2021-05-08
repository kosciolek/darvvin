public abstract class Entity {
    public Vector2 position;

    public Entity(){}

    public Entity (Vector2 pos) throws InvalidPositionException {
        if (!pos.isValid()) throw new InvalidPositionException(pos);
        position = pos;
    }


}
