public class Main {
    public static void main(String[] args) {

        Forma r = new Rettangolo(5, 3);
        Forma t = new Triangolo(4, 6);

        System.out.println("Area del rettangolo: " + r.calcolaArea());
        System.out.println("Area del triangolo: " + t.calcolaArea());
    }
}

// Interfaccia
interface Forma {
    double calcolaArea();
}

// Classe Rettangolo
class Rettangolo implements Forma {
    private double base;
    private double altezza;

    public Rettangolo(double base, double altezza) {
        this.base = base;
        this.altezza = altezza;
    }

    @Override
    public double calcolaArea() {
        return base * altezza;
    }
}

// Classe Triangolo
class Triangolo implements Forma {
    private double base;
    private double altezza;

    public Triangolo(double base, double altezza) {
        this.base = base;
        this.altezza = altezza;
    }

    @Override
    public double calcolaArea() {
        return (base * altezza) / 2;
    }
}
