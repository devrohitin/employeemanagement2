package com.example.employeemanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRepositoryTest {
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseManager db = new DatabaseManager();
        Connection connection = db.getInMemoryConnection("testdb");
        repository = new EmployeeRepository(connection);
        repository.createTableIfNotExists();
        connection.createStatement().execute("DELETE FROM employees");
    }

    @Test
    void addAndFindEmployee() throws Exception {
        boolean created = repository.addEmployee(new Employee(1, "John", "Sales", "Executive", 40000));
        assertTrue(created);

        Employee found = repository.findById(1).orElseThrow();
        assertEquals("John", found.getName());
    }

    @Test
    void duplicateIdIsRejected() throws Exception {
        repository.addEmployee(new Employee(1, "John", "Sales", "Executive", 40000));
        boolean created = repository.addEmployee(new Employee(1, "Jane", "HR", "Manager", 50000));
        assertFalse(created);
    }

    @Test
    void updateEmployee() throws Exception {
        repository.addEmployee(new Employee(1, "John", "Sales", "Executive", 40000));
        boolean updated = repository.updateEmployee(new Employee(1, "John", "Sales", "Senior Executive", 50000));
        assertTrue(updated);

        Employee found = repository.findById(1).orElseThrow();
        assertEquals("Senior Executive", found.getRole());
        assertEquals(50000, found.getSalary());
    }

    @Test
    void removeEmployee() throws Exception {
        repository.addEmployee(new Employee(1, "John", "Sales", "Executive", 40000));
        boolean removed = repository.removeEmployee(1);
        assertTrue(removed);
        assertTrue(repository.findById(1).isEmpty());
    }
}
