package com.example.employeemanagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final EmployeeRepository employeeRepository;

    public DataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.count() > 0) {
            return;
        }

        employeeRepository.save(new Employee("Alice", "Engineering", "Backend Developer", 70000.0));
        employeeRepository.save(new Employee("Bob", "HR", "HR Manager", 55000.0));
        employeeRepository.save(new Employee("Charlie", "Engineering", "QA Engineer", 50000.0));
    }
}
