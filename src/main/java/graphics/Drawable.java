package graphics;

import javafx.scene.canvas.GraphicsContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Function;

abstract public class Drawable {
    protected static Map<String, Function<Map<String, Object>, Drawable>> json_shape_map = new HashMap<>();
    protected static Map<String, Function<List<Object>, Drawable>> bin_shape_map = new HashMap<>();
    public static Drawable create_from_json(String type, Map<String, Object> map) {
        return json_shape_map.get(type).apply(map);
    }
    public static Drawable create_from_bin(String type, List<Object> list) {
        return bin_shape_map.get(type).apply(list);
    }

    abstract public void draw(GraphicsContext gc);
    protected Map<String, Object> info = new LinkedHashMap<>();
    abstract public Drawable move(double delta_x, double delta_y);
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.info.equals(((Drawable) o).info);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Map.Entry<String, Object> map: info.entrySet())
            hash += map.getValue().hashCode();
        return hash;
    }
    public void toBinary(FileOutputStream fos) throws IOException {
        int size = info.size();
        for (Map.Entry<String, Object> map: info.entrySet()) {
            if (map.getValue() instanceof Dot[])
                size += (((Dot[]) map.getValue()).length) * 4;
            else if (map.getValue() instanceof Dot)
                size+=3;
        }
        fos.write(ByteBuffer.allocate(8).putDouble(size).array());
        for (Map.Entry<String, Object> map: info.entrySet()) {
            if (map.getValue() instanceof Number)
                fos.write(ByteBuffer.allocate(8).putDouble(((Number) map.getValue()).doubleValue()).array());
            else if (map.getValue() instanceof Drawable[]) {
                fos.write(ByteBuffer.allocate(8).putDouble(((Drawable[]) map.getValue()).length).array());
                for (Drawable drawable : (Drawable[]) map.getValue()) {
                    drawable.toBinary(fos);
                }
            } else if (map.getValue() instanceof Drawable) {
                ((Drawable)map.getValue()).toBinary(fos);
            }
        }
    }
    public static Drawable readBinary(InputStream input) throws IOException {
        List<Object> list = new ArrayList<>();
        int info_size = (int) readFromFIS(input);
        for (int i = 0; i < info_size; i++)
            list.add(readFromFIS(input));
        if ((double) list.get(0) == 4d)
            return readBinaryGroup(input);
        return create_from_bin(list.get(0).toString().substring(0, 1), list.subList(1, list.size()));
    }
    private static Geogroup readBinaryGroup(InputStream input) throws IOException {
        Geogroup group = new Geogroup();
        int group_size = (int) readFromFIS(input);
        for (int i = 0; i < group_size; i++)
            group.add(readBinary(input));
        return group;
    }
    private static double readFromFIS(InputStream fis) throws IOException {
        byte[] bytebuf;
        bytebuf = fis.readNBytes(8);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytebuf);
        buffer.rewind();
        return buffer.getDouble();
    }
    public void toJSON(Writer writer) throws IOException {
        writer.write(String.format("{\n\"name\":\"%s\",\n", this.getClass().getCanonicalName()));
        int j = 0;
        for (Map.Entry<String, Object> map: info.entrySet()) {
            if (map.getValue() instanceof String)
                writer.write(String.format("\"%s\":\"%s\"", map.getKey(), map.getValue()));
            else if (map.getValue() instanceof Double)
                writer.write(String.format(Locale.US, "\"%s\":%.5f", map.getKey(), map.getValue()));
            else if (map.getValue() instanceof Drawable[]) {
                int i = 0;
                writer.write("\"d_count\":" + ((Dot[]) map.getValue()).length + ",\n");
                writer.write(String.format("\"%s\":[\n", map.getKey()));
                for (Drawable o : (Drawable[]) map.getValue()) {
                    o.toJSON(writer);
                    if (i < ((Dot[]) map.getValue()).length - 1)
                        writer.write(",\n");
                    i++;
                }
                writer.write("]");
            } else if (map.getValue() instanceof Dot) {
                writer.write(String.format("\"%s\":\n", map.getKey()));
                ((Dot) map.getValue()).toJSON(writer);
            }
            else
                writer.write(String.format(Locale.US, "\"%s\":%d", map.getKey(), (int) map.getValue()));
            if (j < info.size() - 1)
                writer.write(",\n");
            j++;
        }
        writer.write("\n}");
    }
}
