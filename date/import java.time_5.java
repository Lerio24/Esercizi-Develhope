import java.time.OffsetDateTime;

public class Main {
    public static void main(String[] args) {

        // Creare i due oggetti data
        OffsetDateTime data1 = OffsetDateTime.parse("2023-03-01T13:00:00Z");
        OffsetDateTime data2 = OffsetDateTime.parse("2024-03-01T13:00:00Z");

        // Verifiche richieste
        boolean primaPrecedente = data1.isBefore(data2);
        boolean secondaSuccessiva = data2.isAfter(data1);

        // Verifica se le date sono uguali all'ora attuale
        OffsetDateTime adesso = OffsetDateTime.now();
        boolean data1UgualeAdesso = data1.isEqual(adesso);
        boolean data2UgualeAdesso = data2.isEqual(adesso);

        // Stampa dei risultati
        System.out.println("Data 1: " + data1);
        System.out.println("Data 2: " + data2);
        System.out.println();

        System.out.println("La prima data è precedente alla seconda: " + primaPrecedente);
        System.out.println("La seconda data è successiva alla prima: " + secondaSuccessiva);
        System.out.println();

        System.out.println("La prima data è uguale ad ora: " + data1UgualeAdesso);
        System.out.println("La seconda data è uguale ad ora: " + data2UgualeAdesso);
    }
}
