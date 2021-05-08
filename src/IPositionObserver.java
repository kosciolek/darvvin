public interface IPositionObserver<T extends Entity> {
    void onPositionChange(T obj, Vector2 from, Vector2 to);
}
