public class Main {

    // ---------------- INTERFACCIA ----------------
    interface Shape {
        String draw();
    }

    // ---------------- CLASSI CHE IMPLEMENTANO SHAPE ----------------
    static class Circle implements Shape {
        @Override
        public String draw() {
            return "Disegno un cerchio";
        }
    }

    static class Rectangle implements Shape {
        @Override
        public String draw() {
            return "Disegno un rettangolo";
        }
    }

    static class Triangle implements Shape {
        @Override
        public String draw() {
            return "Disegno un triangolo";
        }
    }

    // ---------------- ENUM ----------------
    enum ShapeType {
        CIRCLE,
        RECTANGLE,
        TRIANGLE
    }

    // ---------------- FACTORY ----------------
    static class ShapeFactory {
        public static Shape createShape(ShapeType type) {
            return switch (type) {
                case CIRCLE -> new Circle();
                case RECTANGLE -> new Rectangle();
                case TRIANGLE -> new Triangle();
            };
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        Shape cerchio = ShapeFactory.createShape(ShapeType.CIRCLE);
        Shape rettangolo = ShapeFactory.createShape(ShapeType.RECTANGLE);
        Shape triangolo = ShapeFactory.createShape(ShapeType.TRIANGLE);

        System.out.println(cerchio.draw());
        System.out.println(rettangolo.draw());
        System.out.println(triangolo.draw());
    }
}
