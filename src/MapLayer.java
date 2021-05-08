import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapLayer<T extends Entity> {

    private Map<Vector2, T> map = new HashMap<>();

    public void add(Vector2 pos, T entity) {
        map.put(pos, entity);
    }

    public T get(Vector2 pos) {
        return map.get(pos);
    }

    public void remove(Vector2 pos) {
        map.put(pos, null);
    }

    public List<T> getAll() {
        return new ArrayList<>(map.values());
    }


}
