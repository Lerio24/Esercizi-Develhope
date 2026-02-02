public class Main {
    public static void main(String[] args) {

        Forma r = new Rettangolo(5, 3);
        Forma t = new Triangolo(4, 6);

        r.calcolaArea();
        t.calcolaArea();
    }
}

// ENUM per identificare la forma
enum TipoForma {
    TRIANGOLO,
    RETTANGOLO
}

// Classe base
class Forma {
    protected TipoForma tipo;

    public Forma(TipoForma tipo) {
        this.tipo = tipo;
    }

    public void calcolaArea() {
        System.out.println("Calcolo area generica...");
    }
}

// Sottoclasse Rettangolo
class Rettangolo extends Forma {
    private double base;
    private double altezza;

    public Rettangolo(double base, double altezza) {
        super(TipoForma.RETTANGOLO);
        this.base = base;
        this.altezza = altezza;
    }

    @Override
    public void calcolaArea() {
        double area = base * altezza;
        System.out.println("[" + tipo + "] Area rettangolo = " + area);
    }
}

// Sottoclasse Triangolo
class Triangolo extends Forma {
    private double base;
    private double altezza;

    public Triangolo(double base, double altezza) {
        super(TipoForma.TRIANGOLO);
        this.base = base;
        this.altezza = altezza;
    }

    @Override
    public void calcolaArea() {
        double area = (base * altezza) / 2;
        System.out.println("[" + tipo + "] Area triangolo = " + area);
    }
}
