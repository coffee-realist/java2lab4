package graphics;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import java.util.Map;

import static graphics.Dot.createDot;

public class Ellipse extends Figure implements RoundAboutAvailable {
    private final double small_radius;
    private final double big_radius;
    private final double rotation;
    private final Dot center;

    static {
        json_shape_map.put("Ellipse", (Map<String, Object> map) -> {
            Dot center = createDot((Map<String, Object>) map.get("center"));
            double a = Double.parseDouble((String) map.get("big_radius"));
            double b = Double.parseDouble((String) map.get("small_radius"));
            double rotation = Double.parseDouble((String) map.get("rotation"));

            return new Ellipse(center, a, b, rotation);
        });
        bin_shape_map.put("2", (List<Object> list) -> {
            double x = (double) list.get(0);
            double y = (double) list.get(1);
            double b = (double) list.get(2);
            double a = (double) list.get(3);
            double rotation = (double) list.get(4);

            return new Ellipse(new Dot(x, y), a, b, rotation);
        });
    }


    public Ellipse(Dot center, double small_radius, double big_radius, double rotation) {
        super(() -> Math.PI * small_radius * big_radius);
        check(small_radius, big_radius);
        info.put("ID", 2);
        info.put("center", center);
        info.put("small_radius", small_radius);
        info.put("big_radius", big_radius);
        info.put("rotation", rotation);
        this.small_radius = small_radius;
        this.big_radius = big_radius;
        this.center = center;
        if (rotation < 0)
            rotation += 360;
        this.rotation = rotation;
    }

    @Override
    public Ellipse expandTo(double multiplier) {
        return new Ellipse(center, small_radius * multiplier, big_radius * multiplier, rotation);
    }

    @Override
    public Ellipse move(double delta_x, double delta_y) {
        return new Ellipse(center.move(delta_x, delta_y), small_radius, big_radius, rotation);
    }

    private void check(double r1, double r2) {
        if (r1 < 0 || r2 < 0)
            throw new RuntimeException("Полуоси не могут быть отрицательными. " +
                    "Пожалуйста задайте другие значения полуосей");
    }

    public Ellipse getRoundAbout() {
        return new Ellipse(center, big_radius, big_radius, rotation);
    }

    @Override
    public String toString() {
        return String.format("""
                Эллипс.
                Координаты центра: %sМалая полуось: %f.
                Большая полуось: %f.
                Угол поворота: %f,
                Площадь: %f
                                
                """, center, small_radius, big_radius, rotation, getSquare());
    }

    @Override
    public void draw(GraphicsContext gc) {
        double center_x = center.getX();
        double center_y = center.getY();
        double radius = gc.getLineWidth() / 2;
        double max_axis = Math.max(small_radius, big_radius) / 2;
        for (double x = center_x - max_axis; x <= center_x + max_axis; x += 0.2) {
            for (double y = center_y; y <= center_y + max_axis; y += 0.2) {
                if (Math.abs(Math.pow((x - center_x) * Math.cos(rotation) + (y - center_y) * Math.sin(rotation), 2) / Math.pow(big_radius / 2, 2) +
                        Math.pow((x - center_x) * Math.sin(rotation) - (y - center_y) * Math.cos(rotation), 2) / Math.pow(small_radius / 2, 2) - 1) < 0.01) {
                    gc.setFill(gc.getStroke());
                    gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                    double rot_x = center_x + (x - center_x) * Math.cos(Math.PI) - (y - center_y) * Math.sin(Math.PI);
                    double rot_y = center_y + (x - center_x) * Math.sin(Math.PI) + (y - center_y) * Math.cos(Math.PI);
                    gc.fillOval(rot_x - radius, rot_y - radius, radius * 2, radius * 2);
                }
            }
        }

    }
}

