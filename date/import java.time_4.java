import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        // Creare un OffsetDateTime dalla stringa
        OffsetDateTime data = OffsetDateTime.parse("2023-03-01T13:00:00Z");

        // Modifiche richieste
        OffsetDateTime nuovaData = data
                .plusYears(1)
                .minusMonths(1)
                .plusDays(7);

        // Formatter localizzato per l'Italia
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("EEEE d MMMM yyyy, HH:mm:ss Z", Locale.ITALY);

        // Stampa del risultato
        System.out.println("Data originale: " + data);
        System.out.println("Data modificata: " + nuovaData.format(formatter));
    }
}
