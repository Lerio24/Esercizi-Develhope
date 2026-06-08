package com.esempio.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// ==========================================
// 1. ENUM (FlightStatus)
// ==========================================
enum FlightStatus {
    ONTIME,
    DELAYED,
    CANCELLED
}

// ==========================================
// 2. ENTITY (Flight)
// ==========================================
@Entity
@Table(name = "flights")
class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String description;
    private String fromAirport;
    private String toAirport;

    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    public Flight() {}

    public Flight(String description, String fromAirport, String toAirport, FlightStatus status) {
        this.description = description;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.status = status;
    }

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFromAirport() { return fromAirport; }
    public void setFromAirport(String fromAirport) { this.fromAirport = fromAirport; }
    public String getToAirport() { return toAirport; }
    public void setToAirport(String toAirport) { this.toAirport = toAirport; }
    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }
}

// ==========================================
// 3. REPOSITORY (FlightRepository)
// ==========================================
@Repository
interface FlightRepository extends JpaRepository<Flight, Long> {
}

// ==========================================
// 4. CONTROLLER (FlightController)
// ==========================================
@RestController
@RequestMapping("/flights")
class FlightController {

    private final FlightRepository flightRepository;
    private final Random random = new Random();

    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    /**
     * Helper Method per generare stringhe casuali alfanumeriche usando random.ints().
     * Converte un flusso di int (punti di codice dei caratteri) direttamente in una stringa.
     */
    private String generateRandomString(int length) {
        int leftLimit = 97; // Lettera 'a'
        int rightLimit = 122; // Lettera 'z'
        
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * POST http://localhost:8080/flights/provision
     * Genera e salva nel database 50 voli con stringhe casuali e status predefinito ONTIME.
     */
    @PostMapping("/provision")
    public ResponseEntity<List<Flight>> provisionFlights() {
        List<Flight> provisionalFlights = new ArrayList<>();
        
        for (int i = 0; i < 50; i++) {
            String description = "Flight-" + generateRandomString(5).toUpperCase();
            String fromAirport = generateRandomString(3).toUpperCase(); // Es. FCO, MXP
            String toAirport = generateRandomString(3).toUpperCase();
            
            // Impostiamo lo stato predefinito su ONTIME come richiesto
            Flight flight = new Flight(description, fromAirport, toAirport, FlightStatus.ONTIME);
            provisionalFlights.add(flight);
        }
        
        List<Flight> savedFlights = flightRepository.saveAll(provisionalFlights);
        return new ResponseEntity<>(savedFlights, HttpStatus.CREATED);
    }

    /**
     * GET http://localhost:8080/flights
     * Recupera tutti i voli presenti nel database.
     */
    @GetMapping
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
}