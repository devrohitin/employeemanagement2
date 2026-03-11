package com.example.employeemanagement;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        employee.setId(null);
        return employeeRepository.save(employee);
    }

    @GetMapping
    public List<Employee> listEmployees(@RequestParam(required = false) String department) {
        if (department != null && !department.isBlank()) {
            return employeeRepository.findByDepartmentIgnoreCase(department);
        }
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        existing.setName(request.getName());
        existing.setDepartment(request.getDepartment());
        existing.setRole(request.getRole());
        existing.setSalary(request.getSalary());
        return employeeRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeEmployee(@PathVariable Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Employee not found");
        }
        employeeRepository.deleteById(id);
        return Map.of("message", "Employee removed");
    }

    @GetMapping("/payroll/total")
    public Map<String, Double> totalPayroll() {
        double total = employeeRepository.findAll()
                .stream()
                .mapToDouble(Employee::getSalary)
                .sum();
        return Map.of("totalPayroll", total);
    }
}
