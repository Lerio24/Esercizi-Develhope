import java.util.HashSet;
import java.util.Iterator;

public class Main {

    // Funzione che restituisce un HashSet riempito
    public static HashSet<String> creaHashSet() {
        HashSet<String> set = new HashSet<>();
        set.add("Andrea");
        set.add("Marco");
        set.add("Luca");
        return set;
    }

    public static void main(String[] args) {

        // Ottengo l'hashset riempito
        HashSet<String> nomi = creaHashSet();

        // Creo un oggetto dello stesso tipo (String)
        String oggettoDaEliminare = "Marco";

        System.out.println("Set iniziale: " + nomi);
        System.out.println("Oggetto da eliminare: " + oggettoDaEliminare);

        // Scorro il set e rimuovo l'elemento uguale all'oggetto creato
        Iterator<String> it = nomi.iterator();
        while (it.hasNext()) {
            String elemento = it.next();
            if (elemento.equals(oggettoDaEliminare)) {
                it.remove(); // rimozione sicura durante l'iterazione
            }
        }

        System.out.println("Set dopo eliminazione: " + nomi);

        // Svuoto l'hashset
        nomi.clear();

        // Verifico se è vuoto
        boolean vuoto = nomi.isEmpty();

        System.out.println("Il set è vuoto? " + vuoto);
        System.out.println("Contenuto finale del set: " + nomi);
    }
}
