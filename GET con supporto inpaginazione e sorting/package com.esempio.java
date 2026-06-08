package com.esempio.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.*;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // Inseriamo dei dati di prova all'avvio per testare la paginazione
    @Bean
    public CommandLineRunner caricaDati(ProductRepository repository) {
        return args -> {
            repository.saveAll(List.of(
                new Product("Laptop", 1200.00),
                new Product("Smartphone", 800.00),
                new Product("Tablet", 400.00),
                new Product("Monitor", 300.00),
                new Product("Tastiera", 50.00),
                new Product("Mouse", 30.00),
                new Product("Cuffie", 150.00)
            ));
        };
    }
}

// ==========================================
// 1. ENTITÀ (Product)
// ==========================================
@Entity
@Table(name = "products")
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;

    public Product() {}

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}

// ==========================================
// 2. REPOSITORY (ProductRepository)
// ==========================================
@Repository
interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Query Method che supporta la paginazione e l'ordinamento.
    // Spring Data JPA capisce dal tipo 'Pageable' che deve applicare LIMIT, OFFSET e ORDER BY.
    Page<Product> findByPriceGreaterThan(Double price, Pageable pageable);
}

// ==========================================
// 3. CONTROLLER (ProductController)
// ==========================================
@RestController
@RequestMapping("/products")
class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Endpoint GET che restituisce i prodotti con prezzo maggiore di un certo valore,
     * supportando paginazione e ordinamento tramite Query Params.
     * * URL di esempio: 
     * http://localhost:8080/products/search?minPrice=40&page=0&size=3&sortBy=price&direction=desc
     */
    @GetMapping("/search")
    public Page<Product> searchProducts(
            @RequestParam(defaultValue = "0.0") Double minPrice,
            @RequestParam(defaultValue = "0") int page,       // Numero della pagina (parte da 0)
            @RequestParam(defaultValue = "5") int size,       // Quanti elementi per pagina
            @RequestParam(defaultValue = "id") String sortBy,  // Campo su cui fare l'ordinamento
            @RequestParam(defaultValue = "asc") String direction // Direzione: "asc" o "desc"
    ) {
        
        // 1. Creiamo la direzione dell'ordinamento
        Sort sort = direction.equalsIgnoreCase("desc") ? 
                    Sort.by(sortBy).descending() : 
                    Sort.by(sortBy).ascending();
        
        // 2. Creiamo l'oggetto Pageable combinando pagina, dimensione e ordinamento
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // 3. Eseguiamo il query method e restituiamo la pagina dei risultati
        return productRepository.findByPriceGreaterThan(minPrice, pageable);
    }
}