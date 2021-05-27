import java.util.*;

public class MapMultiLayer<T extends Entity> {

    private final Map<Vector2, Set<T>> map = new HashMap<>();

    public void add(Vector2 pos, T entity) {
        map.computeIfAbsent(pos, k -> new HashSet<>());
        map.get(pos).add(entity);
    }

    public void remove(Vector2 pos, T entity) {
        map.computeIfAbsent(pos, k -> new HashSet<>());
        map.get(pos).remove(entity);
    }

    public Set<T> get(Vector2 pos) {
        map.computeIfAbsent(pos, k -> new HashSet<>());
        return map.get(pos);
    }

    public List<T> getAll() {
        var list = new ArrayList<T>();

        for (var set: map.values()) list.addAll(set);

        return list;
    }

    public boolean isEmpty(Vector2 pos) {
        return map.get(pos) == null || map.get(pos).isEmpty();
    }


}
