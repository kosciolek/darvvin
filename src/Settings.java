import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Settings {

    public static Integer width;
    public static Integer height;
    public static Integer startEnergy;
    public static Integer moveEnergy;
    public static Integer plantEnergy;
    public static Double jungleRatio;
    public static Integer startingAnimals;
    public static Integer safeEnergyThreshold;
    public static Integer tickIntervalMs;
    public static Boolean wrapBounds;



    private Settings() { }


    public static void loadFromString(String string) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(string);

        width = ((Long)jsonObject.get("width")).intValue();
        height = ((Long)jsonObject.get("height")).intValue();
        startEnergy = ((Long)jsonObject.get("startEnergy")).intValue();
        moveEnergy = ((Long)jsonObject.get("moveEnergy")).intValue();
        plantEnergy = ((Long)jsonObject.get("plantEnergy")).intValue();
        jungleRatio = ((double)jsonObject.get("jungleRatio"));
        startingAnimals = ((Long)jsonObject.get("startingAnimals")).intValue();
        safeEnergyThreshold = ((Long)jsonObject.get("safeEnergyThreshold")).intValue();
        tickIntervalMs = ((Long)jsonObject.get("tickIntervalMs")).intValue();
        wrapBounds = ((boolean)jsonObject.get("wrapBounds"));

    }

    public static void loadFromFile(Path path) throws IOException, ParseException {
        loadFromString(Files.readString(path));
    }

}
