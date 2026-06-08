package com.esempio.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// ==========================================
// 1. ENTITY (Student)
// ==========================================
@Entity
@Table(name = "students")
class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String surname;
    private boolean isWorking;

    public Student() {}

    public Student(Long id, String name, String surname, boolean isWorking) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.isWorking = isWorking;
    }

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public boolean getIsWorking() { return isWorking; }
    public void setIsWorking(boolean working) { isWorking = working; }
}

// ==========================================
// 2. REPOSITORY (StudentRepository)
// ==========================================
@Repository
interface StudentRepository extends JpaRepository<Student, Long> {
}

// ==========================================
// 3. SERVICE (StudentService)
// ==========================================
@Service
class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Servizio logico per cambiare lo stato lavorativo (isWorking) di uno studente.
     */
    public Optional<Student> changeWorkingStatus(Long id, boolean isWorking) {
        return studentRepository.findById(id).map(student -> {
            student.setIsWorking(isWorking);
            return studentRepository.save(student);
        });
    }
}

// ==========================================
// 4. CONTROLLER (StudentController)
// ==========================================
@RestController
@RequestMapping("/students")
class StudentController {

    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public StudentController(StudentRepository studentRepository, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    // (1) CREATING A NEW STUDENT
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // (2) GETTING A LIST OF ALL THE STUDENTS
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // (3) GETTING A SPECIFIC STUDENT BY PATH VARIABLE
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // (4) UPDATING THE PRIMARY KEY (AND DATA) OF A STUDENT
    // Nota: L'aggiornamento della Primary Key in JPA richiede la cancellazione del vecchio record 
    // e il salvataggio di uno nuovo con il nuovo ID se l'id strutturale cambia.
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentKey(@PathVariable Long id, @RequestBody Student studentDetails) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Eliminiamo il vecchio record se l'ID nel body è differente da quello nel path
        if (!id.equals(studentDetails.getId())) {
            studentRepository.deleteById(id);
        }
        Student updatedStudent = studentRepository.save(studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    // (5) UPDATING THE ISWORKING VALUE VIA REQUEST PARAM
    @PatchMapping("/{id}/working")
    public ResponseEntity<Student> updateIsWorking(@PathVariable Long id, @RequestParam boolean working) {
        return studentService.changeWorkingStatus(id, working)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // (6) DELETING A STUDENT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}