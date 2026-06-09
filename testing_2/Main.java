import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EsercizioFormatiData {

    // 1. LOGICA DELL'ESERCIZIO
    public static Map<String, String> formattaNelleVarieVersioni(String stringaIso) {
        // Crea un oggetto OffsetDateTime (Java 8+)
        OffsetDateTime data = OffsetDateTime.parse(stringaIso);
        
        Map<String, String> formati = new HashMap<>();

        // Crea i formattatori specificando lo stile della DATA (FormatStyle) e il Locale italiano
        DateTimeFormatter formatterFull = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.ITALIAN);
        DateTimeFormatter formatterMedium = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.ITALIAN);
        DateTimeFormatter formatterShort = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.ITALIAN);

        // Formatta la data nelle tre versioni
        formati.put("FULL", data.format(formatterFull));
        formati.put("MEDIUM", data.format(formatterMedium));
        formati.put("SHORT", data.format(formatterShort));

        // Stampa sulla console
        System.out.println("FULL:   " + formati.get("FULL"));
        System.out.println("MEDIUM: " + formati.get("MEDIUM"));
        System.out.println("SHORT:  " + formati.get("SHORT"));

        return formati;
    }

    // Metodo main per l'esecuzione diretta
    public static void main(String[] args) {
        String stringaIniziale = "2002-03-01T13:00:00Z";
        formattaNelleVarieVersioni(stringaIniziale);
    }

    // ============================================================================
    // 2. TEST PER L'ESERCIZIO (JUnit 5)
    // ============================================================================
    @Test
    public void testFormatiData() {
        String input = "2002-03-01T13:00:00Z";
        Map<String, String> risultati = formattaNelleVarieVersioni(input);

        // Nota: i risultati attesi riflettono lo standard di formattazione della lingua italiana
        assertEquals("venerdì 1 marzo 2002", risultati.get("FULL