import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

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
            var writer = new FileWriter("exported.json");
            days.writeJSONString(writer);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
