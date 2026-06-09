import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EsercizioSempliceTest {

    // 1. LA FUNZIONE DA TESTARE (Molto semplice)
    public String formattaData(String stringaIso) {
        OffsetDateTime data = OffsetDateTime.parse(stringaIso);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ITALIAN);
        
        String risultato = data.format(formatter);
        System.out.println(risultato); // Stampa sulla console
        
        return risultato;
    }

    // 2. LA FUNZIONE DI TEST JUNIT (Verifica che tutto funzioni senza errori)
    @Test
    public void testFormattaDataSuccesso() {
        String input = "2023-03-01T13:00:00Z";
        String valoreAtteso = "01 marzo 2023";
        
        String valoreOttenuto = formattaData(input);
        
        // Questo controllo passerà con successo (semaforo verde)
        assertEquals(valoreAtteso, valoreOttenuto);
    }
}