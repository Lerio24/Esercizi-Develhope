import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// Per poter eseguire i test nello stesso file (struttura tipica da ambiente di esercitazione)
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EsercizioData {

    // 1. LOGICA DELL'ESERCIZIO
    public static String elaboraEFormattaData(String stringaIso) {
        // Crea un oggetto OffsetDateTime dalla stringa (Java 8+)
        OffsetDateTime data = OffsetDateTime.parse(stringaIso);

        // Definisce il formattatore per ottenere "gg mese aaaa" localizzato in italiano
        DateTimeFormatter formattatore = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ITALIAN);

        // Formatta la data
        String dataFormattata = data.format(formattatore);

        // Stampa sulla console
        System.out.println("Data formattata: " + dataFormattata);

        return dataFormattata;
    }

    // Metodo main per l'esecuzione diretta dell'esercizio
    public static void main(String[] args) {
        String stringaIniziale = "2023-03-01T13:00:00Z";
        elaboraEFormattaData(stringaIniziale);
    }

    // ============================================================================
    // 2. TEST PER L'ESERCIZIO (JUnit 5)
    // ============================================================================
    @Test
    public void testElaborazioneData() {
        String input = "2023-03-01T13:00:00Z";
        String atteso = "01 marzo 2023";
        
        String risultato = elaboraEFormattaData(input);
        
        // Verifica che il risultato sia esattamente quello richiesto
        assertEquals(atteso, risultato, "La data formattata non corrisponde a quella attesa");