package com.esempio.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// ==========================================
// CONTROLLER (CustomController)
// ==========================================
@RestController
class CustomController {

    /**
     * Endpoint: GET http://localhost:8080/custom
     * * L'annotazione @CrossOrigin permette esplicitamente al server locale di React 
     * (porta 3000 o 5173 a seconda se usi Create-React-App o Vite) di leggere la risposta
     * senza incappare nel blocco di sicurezza CORS del browser.
     */
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
    @GetMapping("/custom")
    public String getCustomMessage() {
        return "Benvenuto! La connessione tra React e Spring Boot funziona perfettamente!";
    }
}