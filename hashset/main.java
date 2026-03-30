import java.util.HashSet;

public class Main {

    // Funzione che restituisce un HashSet riempito
    public static HashSet<String> creaHashSet() {
        HashSet<String> set = new HashSet<>();
        set.add("Andrea");
        set.add("Marco");
        set.add("Giulia");
        return set;
    }

    public static void main(String[] args) {

        // Ottengo l'hashSet riempito
        HashSet<String> nomi = creaHashSet();

        // Creo un oggetto dello stesso tipo (String)
        String nuovoNome = "Marco";

        // Verifico se è presente nel Set
        boolean presente = nomi.contains(nuovoNome);

        // Stampo il risultato
        System.out.println("Elemento da cercare: " + nuovoNome);
        System.out.println("È presente nel Set? " + presente);

        // Stampo anche il contenuto del Set
        System.out.println("Contenuto HashSet: " + nomi);
    }
}
