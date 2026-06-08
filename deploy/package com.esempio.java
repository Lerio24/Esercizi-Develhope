package com.esempio.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// ==========================================
// CONTROLLER (RandomSumController)
// ==========================================
@RestController
class RandomSumController {

    private final Random random = new Random();

    /**
     * Endpoint: GET http://localhost:[port]/sum
     * Genera due numeri interi casuali e ne restituisce la somma con un messaggio.
     */
    @GetMapping("/sum")
    public String getRandomSum() {
        int num1 = random.nextInt(100); // Numero casuale tra 0 e 99
        int num2 = random.nextInt(100);
        int sum = num1 + num2;
        
        return String.format("Numero 1: %d, Numero 2: %d -> La somma è: %d", num1, num2, sum);
    }
}