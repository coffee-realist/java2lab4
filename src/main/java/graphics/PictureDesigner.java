package graphics;

import java.io.*;

public class PictureDesigner {
    public static void house() throws IOException {
        Geogroup g = new Geogroup();
        File file_json = new File("house.JSON");
        File file_bin = new File("house.bin");
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_json)));
        Dot a = new Dot(300, 400);
        Dot b = new Dot(200, 300);
        Dot c = new Dot(300, 200);
        Dot d = new Dot(400, 300);
        Triangle roof = new Triangle(b, c, d);
        Ellipse window = new Ellipse(a, 20, 20, 0);
        Ellipse cloud = new Ellipse(c, 150, 50, 1);
        g.add(roof);
        g.add(window);
        g.add(cloud);
        g.toJSON(writer);
        writer.close();
        try (FileOutputStream fos = new FileOutputStream(file_bin)) {
            g.toBinary(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
