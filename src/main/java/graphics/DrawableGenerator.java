package graphics;


public class DrawableGenerator {
    static public Geogroup dotGroup() {
        Dot dot1 = new Dot(2, 0);
        Dot dot2 = new Dot(2, 2);
        Dot dot3 = new Dot(1, 1);
        return new Geogroup(dot1, dot2, dot3);
    }

    static public Geogroup triangleGroup() {
        Dot dot1 = new Dot(0, 0);
        Dot dot2 = new Dot(1, 0);
        Dot dot3 = new Dot(1, 1);
        Dot dot4 = new Dot(2, 4);
        Dot dot5 = new Dot(3, 5);
        Dot dot6 = new Dot(2, 3);
        Dot dot7 = new Dot(0, 2);
        Dot dot8 = new Dot(3, 1);
        Dot dot9 = new Dot(3, 5);
        Triangle triangle1 = new Triangle(dot1, dot2, dot3);
        Triangle triangle2 = new Triangle(dot4, dot5, dot6);
        Triangle triangle3 = new Triangle(dot7, dot8, dot9);
        return new Geogroup(triangle1, triangle2, triangle3);
    }

    static public Geogroup rectangleGroup() {
        Dot dot1 = new Dot(0, 0);
        Dot dot2 = new Dot(1, 0);
        Dot dot3 = new Dot(1, 1);
        Dot dot4 = new Dot(0, 1);
        Dot dot5 = new Dot(2, 2);
        Dot dot6 = new Dot(3, 3);
        Dot dot7 = new Dot(2, 4);
        Dot dot8 = new Dot(1, 3);
        Dot dot9 = new Dot(4, 4);
        Dot dot10 = new Dot(8, 4);
        Dot dot11 = new Dot(8, 6);
        Dot dot12 = new Dot(4, 6);
        Rectangle rectangle1 = new Rectangle(dot1, dot2, dot3, dot4);
        Rectangle rectangle2 = new Rectangle(dot5, dot6, dot7, dot8);
        Rectangle rectangle3 = new Rectangle(dot9, dot10, dot11, dot12);
        return new Geogroup(rectangle1, rectangle2, rectangle3);
    }

    static public Geogroup ellipseGroup() {
        Dot dot1 = new Dot(4, 4);
        Dot dot2 = new Dot(8, 3);
        Dot dot3 = new Dot(4, 8);
        Ellipse ellipse1 = new Ellipse(dot1, 4, 4, 0);
        Ellipse ellipse2 = new Ellipse(dot2, 1, 3, 0);
        Ellipse ellipse3 = new Ellipse(dot3, 2, 4, 30);
        return new Geogroup(ellipse1, ellipse2, ellipse3);
    }

    static public Geogroup mixedGroup() {
        Geogroup mixed = new Geogroup();
        mixed.add(dotGroup());
        mixed.add(triangleGroup());
        mixed.add(rectangleGroup());
        mixed.add(ellipseGroup());
        return mixed;
    }

    static public Geogroup nestedGroup() {
        Dot dot1 = new Dot(3, 3);
        Dot dot2 = new Dot(5, 3);
        Dot dot3 = new Dot(5, 7);
        Dot dot4 = new Dot(3, 7);
        Dot dot5 = new Dot(2, 8);
        Rectangle rectangle1 = new Rectangle(dot1, dot2, dot3, dot4);
        Ellipse ellipse1 = new Ellipse(dot5, 4, 6, 0);
        return new Geogroup(rectangle1, ellipse1, triangleGroup(), dot5);
    }
}
