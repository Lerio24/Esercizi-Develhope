public class Animale {
    public void faiIlVerso() {
        System.out.println("L'animale fa un verso generico.");
    }
}

class Gatto extends Animale {
    @Override
    public void faiIlVerso() {
        System.out.println("Miao!");
    }
}

class Main {
    public static void main(String[] args) {

        Animale a = new Animale();
        Gatto g = new Gatto();

        a.faiIlVerso();
        g.faiIlVerso();
    }

    }
