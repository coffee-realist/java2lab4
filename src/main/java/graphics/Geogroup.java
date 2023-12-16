package graphics;

import javafx.scene.canvas.GraphicsContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Geogroup extends Drawable {
    private final ArrayList<Drawable> list;

    public Geogroup(Drawable... drawables) {
        this.list = new ArrayList<>();
        this.list.addAll(Arrays.asList(drawables));
    }

    public double getSquare() {
        double new_square = 0;
        for (Drawable drawable : list)
            if (drawable instanceof Figure)
                new_square += ((Figure) drawable).getSquare();
        return new_square;
    }

    public List<Drawable> getList() {
        return Collections.unmodifiableList(list);
    }

    public Drawable get(int index) {
        return list.get(index);
    }

    public void add(Drawable drawable) {
        list.add(drawable);
    }

    public void add(Drawable... drawables) {
        Collections.addAll(list, drawables);
    }

    public void add(Geogroup geogroup) {
        list.addAll(geogroup.getList());
    }
    public void addAll(ArrayList<Drawable> shapes) {
        list.addAll(shapes);
    }


    public void remove(Drawable drawable) {
        list.remove(drawable);
    }

    public void remove(int index) {
        list.remove(index);
    }

    @Override
    public void draw(GraphicsContext gc) {
        for (Drawable drawable: list)
            drawable.draw(gc);
    }

    @Override
    public Geogroup move(double delta_x, double delta_y) {
        Geogroup moved_geogroup = new Geogroup();
        for (Drawable drawable : list)
            moved_geogroup.add(drawable.move(delta_x, delta_y));
        return moved_geogroup;
    }

    public Geogroup expandTo(double multiplier) {
        Geogroup expanded = new Geogroup();
        for (Drawable drawable : list)
            if (drawable instanceof Figure)
                expanded.add(((Figure) drawable).expandTo(multiplier));
        return expanded;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < list.size(); i++)
            out.append(String.format("(Элемент группы №%d).\n", i + 1)).append(list.get(i).toString());
        return "[‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾Группа фигур‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾]\n" + out + "\n[________________Конец группы________________]\n";
    }

    public boolean equals(Geogroup g) {
        if (this == g) return true;
        if (g == null) return false;
        if (list.size() != g.list.size()) return false;
        for (int i = 0; i < list.size(); i++)
            if (!list.get(i).equals(g.list.get(i))) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Drawable s : list)
            hash += s.hashCode();
        return hash;

    }
    public void toBinary(FileOutputStream fos) throws IOException {
        fos.write(ByteBuffer.allocate(8).putDouble(1).array());
        fos.write(ByteBuffer.allocate(8).putDouble(4).array());
        fos.write(ByteBuffer.allocate(8).putDouble(list.size()).array());
        for (Drawable s: list)
            s.toBinary(fos);
    }

    public void toJSON(Writer writer) throws IOException {
        writer.write("{\n\"name\":\"Group\",");
        writer.write("\n\"shapes\":\n[\n");
        for (Drawable s: list) {
            s.toJSON(writer);
            if (list.indexOf(s) + 1 != list.size())
                writer.write(",\n");
        }
        writer.write("\n]\n}");
    }



}
