package com.esempio.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.*;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // Carica dei dati iniziali nel database all'avvio per facilitare i test
    @Bean
    public CommandLineRunner caricaDatiIniziali(UserRepository repository) {
        return args -> {
            repository.save(new User("Mario Rossi", "mario.rossi@email.com"));
            repository.save(new User("Luigi Bianchi", "luigi.bianchi@email.com"));
            System.out.println(">>> Database inizializzato con 2 utenti di prova <<<");
        };
    }
}

// ==========================================
// 1. ENTITÀ (User)
// ==========================================
@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // Costruttore di default richiesto da JPA
    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

// ==========================================
// 2. REPOSITORY (UserRepository)
// ==========================================
@Repository
interface UserRepository extends JpaRepository<User, Long> {
}

// ==========================================
// 3. CONTROLLER (UserController)
// ==========================================
@RestController
@RequestMapping("/api/users")
class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE: Crea un nuovo utente
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // READ ALL: Ritorna la lista di tutti gli utenti
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ SINGLE: Ritorna un singolo utente tramite ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE: Aggiorna un utente esistente
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok().body(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Elimina un utente tramite ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}