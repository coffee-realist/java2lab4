package graphics;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static graphics.Dot.createDot;

public class JSONParser {
    private static List<Drawable> readShapesFromJSON(InputStream input) throws IOException {
        List<Drawable> shapes = new ArrayList<>();
        Geogroup g = new Geogroup();
        String jsonString = new String(input.readAllBytes());
        jsonString = jsonString.replaceAll("\n", "");
        jsonString = jsonString.substring(jsonString.indexOf("[") + 1, jsonString.lastIndexOf("]"));
        String[] shapeStrings = splitByBrackets(jsonString);
        Map<String, Object> keyValueMap = null;
        for (String shapeString : shapeStrings) {
            if (shapeString.contains("Group")) {
                g.addAll(new ArrayList<>(readShapesFromJSON(new ByteArrayInputStream(shapeString.getBytes()))));
                shapes.add(g);
                continue;
            }
            if (shapeString.charAt(0) == '{') {
                shapeString = shapeString.substring(1);
            }
            if (shapeString.charAt(shapeString.length() - 1) == '}') {
                shapeString = shapeString.substring(0, shapeString.length() - 1);
            }
            shapeString = shapeString.replaceAll("\\[","").replaceAll("]", "");
            keyValueMap = parseKeyValuePairs(shapeString);
            String name = (String) keyValueMap.get("name");
            if (Objects.equals(name, "Dot"))
                shapes.add(createDot(keyValueMap));
            else
                shapes.add(Drawable.create_from_json(name, keyValueMap));
        }
        return shapes;
    }

    public static Map<String, Object> parseKeyValuePairs(String shapeString) {
        Map<String, Object> keyValueMap = new HashMap<>();
        boolean flag = false;
        int corner_count = 10;
        int max_points = 10;
        List<Map<String, Object>> Dots = new ArrayList<>();
        String[] keyValues = shapeString.split(",\\s*(?![^\\{]*\\})");
        for (String keyValue : keyValues) {
            if (corner_count < max_points && corner_count != 0){
                keyValue = keyValue.substring(1, keyValue.length() - 1);
                Dots.add(parseKeyValuePairs(keyValue));
                corner_count-=1;
                keyValueMap.put("dots", Dots);
                continue;
            }
            String[] parts = keyValue.split(":\\s*", 2);
            String key = parts[0].replaceAll("\"", "").trim();
            String value;
            if (key.equals("name"))
                value = parts[1].replaceAll("\"", "").trim();
            else
                value = parts[1].trim();
            if (key.equals("d_count")) {
                max_points = Integer.parseInt(value);
                corner_count = max_points;
            }
            if (value.startsWith("{") && value.endsWith("}")) {
                value = value.substring(1, value.length() - 1);
                if (key.equals("dots")){
                    Dots.add(parseKeyValuePairs(value));
                    corner_count-=1;
                    keyValueMap.put(key, Dots);
                    continue;
                }
                keyValueMap.put(key, parseKeyValuePairs(value));
            } else {
                keyValueMap.put(key, value);
            }
        }
        return keyValueMap;
    }
    public static Geogroup deserializeShapes(InputStream input) throws IOException {
        List<Drawable> figures;
        figures = readShapesFromJSON(input);
        Geogroup g = new Geogroup();
        g.addAll((ArrayList<Drawable>) figures);
        return g;
    }
    public static String[] splitByBrackets(String input) {
        int level = 0;
        int start = 0;
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '{') {
                level++;
            } else if (input.charAt(i) == '}') {
                level--;
                if (level == 0) {
                    parts.add(input.substring(start, i + 1));
                    start = i + 2;
                }
            }
        }
        return parts.toArray(new String[0]);
    }
}
