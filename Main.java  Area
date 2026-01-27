public class Main {
    public static void main(String[] args) {

        Forma f = new Forma();
        f.calcolaArea();

        Rettangolo r = new Rettangolo(5, 3);
        r.calcolaArea();
    }
}

class Forma {
    public void calcolaArea() {
        System.out.println("Area non definita per la forma generica.");
    }
}

class Rettangolo extends Forma {
    private double base;
    private double altezza;

    public Rettangolo(double base, double altezza) {
        this.base = base;
        this.altezza = altezza;
    }

    @Override
    public void calcolaArea() {
        double area = base * altezza;
        System.out.println("L'area del rettangolo è: " + area);
    }
}
