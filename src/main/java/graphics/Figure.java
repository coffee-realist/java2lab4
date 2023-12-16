package graphics;

public abstract class Figure extends Drawable {
    private final Lazy<Double> square;

    public Figure(Producer<Double> square_function){
        square = new Lazy<>(square_function);
    }

    public double getSquare(){
        return square.get();
    }
    abstract public Figure expandTo(double multiplier);
    abstract public Figure move(double delta_x, double delta_y);
}

