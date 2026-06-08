package com.esempio.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // Questo blocco inserisce un dato di prova nel database H2 all'avvio dell'applicazione
    @Bean
    public CommandLineRunner inserisciDatiIniziali(SalutoRepository repository) {
        return args -> {
            Saluto salutoIniziale = new Saluto();
            salutoIniziale.setTesto("Ciao! Questo messaggio è stato letto con successo dal Database!");
            repository.save(salutoIniziale); // Salva con ID = 1
        };
    }
}

// ==========================================
// 1. CONTROLLER (Gestisce la richiesta REST)
// ==========================================
@RestController
@RequestMapping("/v3")
class SalutoController {

    private final SalutoService salutoService;

    // Dependency Injection del servizio
    public SalutoController(SalutoService salutoService) {
        this.salutoService = salutoService;
    }

    // Endpoint: GET http://localhost:8080/v3/ciao?id=1
    @GetMapping("/ciao")
    public String leggiDalDatabase(@RequestParam(value = "id", defaultValue = "1") Long id) {
        return salutoService.ottieniMessaggioDatoId(id);
    }
}

// ==========================================
// 2. SERVICE (Contiene la logica di business)
// ==========================================
@Service
class SalutoService {

    private final SalutoRepository salutoRepository;

    // Dependency Injection del Repository
    public SalutoService(SalutoRepository salutoRepository) {
        this.salutoRepository = salutoRepository;
    }

    public String ottieniMessaggioDatoId(Long id) {
        Optional<Saluto> salutoInDatabase = salutoRepository.findById(id);
        
        if (salutoInDatabase.isPresent()) {
            return salutoInDatabase.get().getTesto();
        } else {
            return "Nessun messaggio trovato nel database per l'ID: " + id;
        }
    }
}

// ==========================================
// 3. REPOSITORY (Interfaccia Spring Data JPA)
// ==========================================
@Repository
interface SalutoRepository extends JpaRepository<Saluto, Long> {
    // Eredita automaticamente i metodi CRUD come findById()
}

// ==========================================
// 4. ENTITY (Rappresenta la tabella nel DB)
// ==========================================
@Entity
@Table(name = "saluti")
class Saluto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testo;

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTesto() { return testo; }
    public void setTesto(String testo) { this.testo = testo; }
}