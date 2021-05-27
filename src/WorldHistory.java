import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WorldHistory {
    JSONArray days = new JSONArray();


    public void addDay(int animalCount,
                       int deadAnimals,
                       int aliveAnimals,
                       int plantCount,
                       double avgEnergy,
                       double avgImmediateChildren,
                       double avgDaysLivedDeadOnly,
                       String dominantGenome
    ) {

        var obj = new JSONObject();

        obj.put("animal_count", animalCount);
        obj.put("dead_animals", deadAnimals);
        obj.put("alive_animals", aliveAnimals);
        obj.put("plant_count", plantCount);
        obj.put("avg_energy", avgEnergy);
        obj.put("avg_immediate_children", avgImmediateChildren);
        obj.put("avg_days_lived_dead_only", avgDaysLivedDeadOnly);
        obj.put("dominant_genome", dominantGenome);
        days.add(obj);
    }

    public JSONArray getHistory() {
        return this.days;
    }


    public void export() throws IOException {
        Files.writeString(Paths.get("exported.json"), days.toJSONString());
    }


}
