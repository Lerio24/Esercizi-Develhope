package com.esempio.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
}

// ==========================================
// 1. ENTITY (Car)
// ==========================================
@Entity
@Table(name = "cars")
class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modelName;
    private String type;

    // Costruttore di default
    public Car() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

// ==========================================
// 2. REPOSITORY (CarRepository)
// ==========================================
@Repository
interface CarRepository extends JpaRepository<Car, Long> {
}

// ==========================================
// 3. CONTROLLER (CarController)
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

    // 3. RESTITUISCE UNA SINGOLA CAR (restituisce Car vuota se l'id non esiste)
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        if (!carRepository.existsById(id)) {
            return ResponseEntity.ok(new Car()); // Restituisce un oggetto Car vuoto con 200 OK
        }
        Car car = carRepository.findById(id).orElse(new Car());
        return ResponseEntity.ok(car);
    }

    // 4. AGGIORNA IL TYPE VIA QUERY PARAM (restituisce Car vuota se l'id non esiste)
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCarType(
            @PathVariable Long id, 
            @RequestParam String type) {
        
        if (!carRepository.existsById(id)) {
            return ResponseEntity.ok(new Car()); // Restituisce un oggetto Car vuoto con 200 OK
        }
        
        Car car = carRepository.findById(id).orElse(null);
        if (car != null) {
            car.setType(type);
            carRepository.save(car);
        }
        return ResponseEntity.ok(car);
    }

    // 5. CANCELLA LA CAR SPECIFICA (restituisce 409 Conflict se assente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarById(@PathVariable Long id) {
        if (!carRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }
        carRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 6. CANCELLA TUTTE LE CARS NEL DB
    @DeleteMapping
    public ResponseEntity<Void> deleteAllCars() {
        carRepository.deleteAll();
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}