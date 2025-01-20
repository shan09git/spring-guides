package service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class PayrollServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayrollServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepository employeeRepository) {
        return args -> {
            Stream.of(new Employee("mike", "manager", 100.00d),
                            new Employee("bob", "director", 100.23d))
                    .forEach(emp -> {
                        employeeRepository.save(emp);
                    });
        };
    }


}

class EmployeeNotFound extends RuntimeException {
    Long id;
    String message;

    public EmployeeNotFound(Long id, String message) {
    }

}

@ControllerAdvice
class AppException {


    @ExceptionHandler(EmployeeNotFound.class)
    public ProblemDetail handleException(EmployeeNotFound employeeNotFound) {
        var problemDetails = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, employeeNotFound.message);
        problemDetails.setType(URI.create("http://localhost:8080/error"));
        problemDetails.setTitle("error");
        problemDetails.setProperty("id", employeeNotFound.id);
        return problemDetails;
    }
}


@Controller
@RequestMapping("/emp")
class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    @ResponseBody
    public Collection<Employee> all() {
        var emps = this.employeeRepository.findAll();
        if (emps.isEmpty()) {
            throw new EmployeeNotFound(0l, "no employees Found");
        } else {
            return emps;
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Employee> getOne(@PathVariable Long id) {
        return this.employeeRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EmployeeNotFound(id, "Employee Not Found"));
    }

    @PostMapping("/new")
    @ResponseBody
    public Employee newEmployee(@RequestBody Employee employee) {
        return Optional.of(this.employeeRepository.save(employee)).orElse(null);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public Employee updateEmployee(@RequestBody Employee employee, @PathVariable Long id) {
        return Optional.of(this.employeeRepository.save(employee)).orElse(null);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        this.employeeRepository.deleteById(id);
    }

}


interface EmployeeRepository extends JpaRepository<Employee, Long> {
}


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "employee")
class Employee {
    @Id
    @GeneratedValue
    Long id;

    String name;
    String title;

    Double salary;

    public Employee(String name, String title, Double salary) {
        this.name = name;
        this.title = title;
        this.salary = salary;
    }
}
