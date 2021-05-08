public class Field extends Entity {
    public boolean hasPlant = false;
    public FieldType type = FieldType.STEPPE;


    public Field(Vector2 pos) throws InvalidPositionException {
        super(pos);
        this.type = FieldType.STEPPE;
    }

    public Field(Vector2 pos, FieldType type) throws InvalidPositionException {
        super(pos);
        this.type = type;
    }
}
