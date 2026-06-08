package com.esempio.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// ==========================================
// 1. ENUMERATIVI (Type e Color)
// ==========================================
enum CarType {
    SUV, SEDAN, COUPE, HATCHBACK, ELECTRIC
}

enum CarColor {
    RED, BLUE, BLACK, WHITE, GRAY
}

// ==========================================
// 2. ENTITÀ (Car)
// ==========================================
@Entity
@Table(name = "cars")
class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome del modello è obbligatorio")
    @Column(nullable = false)
    private String modelName;

    @NotNull(message = "Il tipo è obbligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarType type;

    @NotNull(message = "Il colore è obbligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarColor color;

    private String description; // Non obbligatorio

    // Costruttori, Getter e Setter
    public Car() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public CarType getType() { return type; }
    public void setType(CarType type) { this.type = type; }

    public CarColor getColor() { return color; }
    public void setColor(CarColor color) { this.color = color; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

// ==========================================
// 3. REPOSITORY (CarRepository)
// ==========================================
@Repository
interface CarRepository extends JpaRepository<Car, Long> {
}

// ==========================================
// 4. CONTROLLER (CarController)
// ==========================================
@RestController
@RequestMapping("/cars")
class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // 1. CREA NUOVA CAR
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car savedCar = carRepository.save(car);
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }

    // 2. RESTITUISCE TUTTE LE CARS
    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // 3. RESTITUISCE UNA SINGOLA CAR DA ID (o 404 se non esiste)
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        if (!carRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Car car = carRepository.findById(id).orElse(null);
        return ResponseEntity.ok(car);
    }

    // 4. AGGIORNA TYPE DELLA CAR SPECIFICA TRAMITE QUERY PARAM (o 404 se non esiste)
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCarType(
            @PathVariable Long id, 
            @RequestParam CarType type) {
        
        if (!carRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Car car = carRepository.findById(id).orElse(null);
        if (car != null) {
            car.setType(type);
            carRepository.save(car);
        }
        return ResponseEntity.ok(car);
    }

    // 5. CANCELLA LA CAR SPECIFICA (o 404 se non esiste)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarById(@PathVariable Long id) {
        if (!carRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        carRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 6. CANCELLA TUTTE LE CARS IN DB
    @DeleteMapping
    public ResponseEntity<Void> deleteAllCars() {
        carRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}