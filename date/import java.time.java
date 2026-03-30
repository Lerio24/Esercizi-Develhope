import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        // Creare un OffsetDateTime dalla stringa
        OffsetDateTime data = OffsetDateTime.parse("2002-03-01T13:00:00Z");

        // Formatter FULL, MEDIUM e SHORT
        DateTimeFormatter fullFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.FULL)
                .withLocale(Locale.ITALY);

        DateTimeFormatter mediumFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.ITALY);

        DateTimeFormatter shortFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.ITALY);

        // Stampare le varie versioni
        System.out.println("FULL:   " + data.format(fullFormatter));
        System.out.println("MEDIUM: " + data.format(mediumFormatter));
        System.out.println("SHORT:  " + data.format(shortFormatter));
    }
}
