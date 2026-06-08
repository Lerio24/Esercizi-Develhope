package com.esempio.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// ==========================================
// 1. COMPONENT (MyComponent)
// ==========================================
@Component
class MyComponent {
    
    private final String myComponentName;

    public MyComponent() {
        // Log richiesto per il costruttore
        System.out.println("MyComponent constructor has been called");
        this.myComponentName = "Giuseppe"; // Sostituisci pure con il tuo nome reale
    }

    public String getMyComponentName() {
        // Log richiesto per il metodo
        System.out.println("MyComponent.getMyComponentName() has been called");
        return this.myComponentName;
    }
}

// ==========================================
// 2. SERVICE (MyService)
// ==========================================
@Service
class MyService {

    private final MyComponent myComponent;

    // Constructor-based Dependency Injection
    public MyService(MyComponent myComponent) {
        System.out.println("MyService constructor has been called");
        this.myComponent = myComponent;
    }

    public String getName() {
        System.out.println("MyService.getName() has been called");
        return myComponent.getMyComponentName();
    }
}

// ==========================================
// 3. CONTROLLER (MyController)
// ==========================================
@RestController
class MyController {

    private final MyService myService;

    // Costruttore per la Dependency Injection di MyService
    public MyController(MyService myService) {
        System.out.println("MyController constructor has been called");
        this.myService = myService;
    }

    // Mapping sulla root principale (/) per stampare il messaggio di benvenuto
    @GetMapping("/")
    public String welcomeMessage() {
        return "Benvenuto nell'applicazione Spring Boot!";
    }

    // Mapping su /getName per restituire il nome
    @GetMapping("/getName")
    public String getName() {
        return myService.getName();
    }
}