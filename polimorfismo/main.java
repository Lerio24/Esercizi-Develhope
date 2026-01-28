import java.util.Scanner;

public class Main {

    // Classe Rettangolo
    static class Rettangolo {
        private double base;
        private double altezza;

        public Rettangolo(double base, double altezza) {
            this.base = base;
            this.altezza = altezza;
        }

        public double calcolaArea() {
            return base * altezza;
        }
    }

    // Classe Triangolo
    static class Triangolo {
        private double base;
        private double altezza;

        public Triangolo(double base, double altezza) {
            this.base = base;
            this.altezza = altezza;
        }

        public double calcolaArea() {
            return (base * altezza) / 2;
        }
    }

    // MAIN
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("Calcolo area delle figure geometriche");
        System.out.println("1) Rettangolo");
        System.out.println("2) Triangolo");
        System.out.print("Scegli una figura: ");
        int scelta = input.nextInt();

        switch (scelta) {
            case 1:
                System.out.print("Inserisci base: ");
                double baseR = input.nextDouble();
                System.out.print("Inserisci altezza: ");
                double altezzaR = input.nextDouble();

                Rettangolo r = new Rettangolo(baseR, altezzaR);
                System.out.println("Area del rettangolo: " + r.calcolaArea());
                break;

            case 2:
                System.out.print("Inserisci base: ");
                double baseT = input.nextDouble();
                System.out.print("Inserisci altezza: ");
                double altezzaT = input.nextDouble();

                Triangolo t = new Triangolo(baseT, altezzaT);
                System.out.println("Area del triangolo: " + t.calcolaArea());
                break;

            default:
                System.out.println("Scelta non valida");
        }

        input.close();
    }
}
