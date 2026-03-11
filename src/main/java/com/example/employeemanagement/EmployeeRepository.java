package com.example.employeemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository {
    private final Connection connection;

    public EmployeeRepository(Connection connection) {
        this.connection = connection;
    }

    public void createTableIfNotExists() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS employees (
                    id INT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    department VARCHAR(100) NOT NULL,
                    role VARCHAR(100) NOT NULL,
                    salary DOUBLE NOT NULL
                )
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public boolean addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (id, name, department, role, salary) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employee.getId());
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getDepartment());
            ps.setString(4, employee.getRole());
            ps.setDouble(5, employee.getSalary());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    public List<Employee> listEmployees() throws SQLException {
        String sql = "SELECT id, name, department, role, salary FROM employees ORDER BY id";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            List<Employee> employees = new ArrayList<>();
            while (rs.next()) {
                employees.add(mapRow(rs));
            }
            return employees;
        }
    }

    public Optional<Employee> findById(int id) throws SQLException {
        String sql = "SELECT id, name, department, role, salary FROM employees WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET name = ?, department = ?, role = ?, salary = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getDepartment());
            ps.setString(3, employee.getRole());
            ps.setDouble(4, employee.getSalary());
            ps.setInt(5, employee.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean removeEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public List<Employee> listByDepartment(String department) throws SQLException {
        String sql = "SELECT id, name, department, role, salary FROM employees WHERE LOWER(department) = LOWER(?) ORDER BY id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, department);
            try (ResultSet rs = ps.executeQuery()) {
                List<Employee> employees = new ArrayList<>();
                while (rs.next()) {
                    employees.add(mapRow(rs));
                }
                return employees;
            }
        }
    }

    public double totalPayroll() throws SQLException {
        String sql = "SELECT COALESCE(SUM(salary), 0) AS total FROM employees";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            rs.next();
            return rs.getDouble("total");
        }
    }

    public void seedData() throws SQLException {
        if (!listEmployees().isEmpty()) {
            return;
        }
        addEmployee(new Employee(101, "Alice", "Engineering", "Backend Developer", 70000));
        addEmployee(new Employee(102, "Bob", "HR", "HR Manager", 55000));
        addEmployee(new Employee(103, "Charlie", "Engineering", "QA Engineer", 50000));
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("department"),
                rs.getString("role"),
                rs.getDouble("salary")
        );
    }
}
