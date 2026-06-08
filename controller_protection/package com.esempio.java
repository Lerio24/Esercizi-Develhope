package com.esempio.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // Inizializzazione Database con Utenti, Ruoli e Salari di prova
    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository, 
            RoleRepository roleRepository, 
            SalaryRepository salaryRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Creazione Ruoli
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            // 2. Creazione Utente Admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole, userRole));
            userRepository.save(admin);

            // 3. Creazione Utente Normale
            User impiegato = new User();
            impiegato.setUsername("user");
            impiegato.setPassword(passwordEncoder.encode("user123"));
            impiegato.setRoles(Set.of(userRole));
            userRepository.save(impiegato);

            // 4. Assegnazione Salario iniziale all'utente normale
            Salary salary = new Salary();
            salary.setAmount(2500.00);
            salary.setUser(impiegato);
            salaryRepository.save(salary);
        };
    }
}

// ==========================================
// 1. ENTITÀ (User, Role, Salary)
// ==========================================
@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Relazione Many-to-Many con i Ruoli
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Relazione One-to-One speculare (opzionale, mappata dal figlio Salary)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Salary salary;

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}

@Entity
@Table(name = "roles")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Deve iniziare con "ROLE_" (es. ROLE_ADMIN)

    public Role() {}
    public Role(String name) { this.name = name; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

@Entity
@Table(name = "salaries")
class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    // Relazione One-to-One con l'utente (Possiede la chiave esterna user_id)
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    public Salary() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    // Evitiamo ricorsioni infinite nel JSON nascondendo i dettagli ciclici dell'utente
    public Long getUserId() { return user != null ? user.getId() : null; }
    public String getUsername() { return user != null ? user.getUsername() : null; }
    public void setUser(User user) { this.user = user; }
}

// ==========================================
// 2. REPOSITORY
// ==========================================
@Repository interface UserRepository extends JpaRepository<User, Long> { User findByUsername(String username); }
@Repository interface RoleRepository extends JpaRepository<Role, Long> { }
@Repository interface SalaryRepository extends JpaRepository<Salary, Long> { Salary findByUserId(Long userId); }

// ==========================================
// 3. SPRING SECURITY CONFIGURATION
// ==========================================
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Abilita l'uso di @PreAuthorize nei controller
class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabilitato per testare agevolmente con Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() // Permetti l'accesso alla console H2 se necessaria
                .anyRequest().authenticated() // Qualsiasi altra richiesta richiede autenticazione
            )
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // Per console H2
            .httpBasic(Customizer.withDefaults()); // Attiva l'autenticazione Basic Auth (username e password nell'header della chiamata)

        return http.build();
    }
}

// Servizio personalizzato per caricare gli utenti dal database durante il login
@Service
class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Utente non trovato: " + username);
        }

        // Mappatura dei ruoli del DB nelle Authorities di Spring Security
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}

// ==========================================
// 4. CONTROLLER UTENTE (CRUD Libero / Autenticato)
// ==========================================
@RestController
@RequestMapping("/users")
class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

// ==========================================
// 5. CONTROLLER SALARY (PROTETTO - SOLO ADMIN)
// ==========================================
@RestController
@RequestMapping("/salaries")
@PreAuthorize("hasRole('ADMIN')") // Blocca l'intero controller: SOLO gli utenti con ruolo ROLE_ADMIN possono entrare
class SalaryController {

    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;

    public SalaryController(SalaryRepository salaryRepository, UserRepository userRepository) {
        this.salaryRepository = salaryRepository;
        this.userRepository = userRepository;
    }

    // 1. GET ALL SALARIES
    @GetMapping
    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }

    // 2. GET USER SPECIFIC SALARY
    @GetMapping("/user/{userId}")
    public ResponseEntity<Salary> getUserSalary(@PathVariable Long userId) {
        Salary salary = salaryRepository.findByUserId(userId);
        if (salary == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(salary);
    }

    // 3. CREATE USER SALARY
    @PostMapping("/user/{userId}")
    public ResponseEntity<Salary> createSalary(@PathVariable Long userId, @RequestBody Salary salaryDetails) {
        return userRepository.findById(userId).map(user -> {
            // Verifica se l'utente ha già un salario associato (essendo One-to-One)
            Salary existingSalary = salaryRepository.findByUserId(userId);
            if (existingSalary != null) {
                existingSalary.setAmount(salaryDetails.getAmount());
                return new ResponseEntity<>(salaryRepository.save(existingSalary), HttpStatus.OK);
            }
            
            salaryDetails.setUser(user);
            Salary savedSalary = salaryRepository.save(salaryDetails);
            return new ResponseEntity<>(savedSalary, HttpStatus.CREATED);
        }).orElse(ResponseEntity.notFound().build());
    }
}