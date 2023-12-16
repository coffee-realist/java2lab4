package graphics;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import java.util.Map;

public class Dot extends Drawable {
    private final double x;
    private final double y;
    static {
        bin_shape_map.put("0", (List<Object> list)->{
            double x = (double) list.get(0);
            double y = (double) list.get(1);
            return new Dot(x, y);
        });
    }

    public Dot(double x, double y) {
        info.put("ID", 0);
        info.put("x", x);
        info.put("y", y);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Dot move(double delta_x, double delta_y) {
        return new Dot(x + delta_x, y + delta_y);
    }


    public Dot plus(Dot dot2) {
        return new Dot(x + dot2.getX(), y + dot2.getY());
    }

    public Dot minus(Dot dot2) {
        return new Dot(x - dot2.getX(), y - dot2.getY());
    }

    public double scalarProduct(Dot dot2) {
        return x * dot2.getX() + y * dot2.getY();
    }

    public Dot product(double multiplier) {
        return new Dot(x * multiplier, y * multiplier);
    }

    public double length(Dot dot2) {
        return Math.sqrt(Math.pow(dot2.getX() - x, 2) + Math.pow(dot2.getY() - y, 2));
    }

    @Override
    public String toString() {
        return String.format("Точка (%f; %f).\n", x, y);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillOval(x, y, gc.getLineWidth(), gc.getLineWidth());
    }
    public static Dot createDot(Map<String, Object> keyValueMap) {
        String name = (String) keyValueMap.get("name");
        double x = Double.parseDouble((String) keyValueMap.get("x"));
        double y = Double.parseDouble((String) keyValueMap.get("y"));
        return new Dot(x, y);
    }

}
