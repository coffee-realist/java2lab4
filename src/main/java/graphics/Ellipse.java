package graphics;

import java.util.List;
import java.util.Map;

import static graphics.Dot.createDot;

public class Ellipse extends Figure implements RoundAboutAvailable {
    private final double small_radius;
    private final double big_radius;
    private final double rotation;
    private final Dot center;
    static {
        json_shape_map.put("Ellipse", (Map<String, Object> map)->{
            Dot center = createDot((Map<String, Object>) map.get("center"));
            double a = Double.parseDouble((String) map.get("big_radius"));
            double b = Double.parseDouble((String) map.get("small_radius"));
            double rotation = Double.parseDouble((String) map.get("rotation"));

            return new Ellipse(center, a, b, rotation);
        });
        bin_shape_map.put("2", (List<Object> list)->{
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
    public void draw() {
    }
}

