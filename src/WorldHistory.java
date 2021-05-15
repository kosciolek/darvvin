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
                       double avgDaysLivedDeadOnly
                       ) {

        var obj = new JSONObject();

        obj.put("animalCount", animalCount);
        obj.put("deadAnimals", deadAnimals);
        obj.put("aliveAnimals", aliveAnimals);
        obj.put("plantCount", plantCount);
        obj.put("avgEnergy", avgEnergy);
        obj.put("avgImmediateChildren", avgImmediateChildren);
        //obj.put("avgChildrenTotal", avgChildrenTotal);
        obj.put("avgDaysLivedDeadOnly", avgDaysLivedDeadOnly);

        days.add(obj);
    }

    public JSONArray getHistory() {
        return this.days;
    }


    public void export() {

        try {
            Files.writeString(Paths.get("exported.json"), days.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
