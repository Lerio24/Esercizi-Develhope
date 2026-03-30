import java.time.OffsetDateTime;
import java.time.DayOfWeek;

public class Main {
    public static void main(String[] args) {

        // Creare un OffsetDateTime dalla stringa
        OffsetDateTime data = OffsetDateTime.parse("2023-03-01T13:00:00Z");

        // Ottenere anno, mese, giorno e giorno della settimana
        int anno = data.getYear();
        int mese = data.getMonthValue();
        int giorno = data.getDayOfMonth();
        DayOfWeek giornoSettimana = data.getDayOfWeek();

        // Stampare i risultati
        System.out.println("Anno: " + anno);
        System.out.println("Mese: " + mese);
        System.out.println("Giorno: " + giorno);
        System.out.println("Giorno della settimana: " + giornoSettimana);
    }
}
