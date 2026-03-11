package com.example.employeemanagement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            EmployeeRepository repository = new EmployeeRepository(connection);
            repository.createTableIfNotExists();
            repository.seedData();

            while (true) {
                printMenu();
                System.out.print("Select an option: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> addEmployee(scanner, repository);
                    case "2" -> listAll(repository);
                    case "3" -> findById(scanner, repository);
                    case "4" -> updateEmployee(scanner, repository);
                    case "5" -> removeEmployee(scanner, repository);
                    case "6" -> listByDepartment(scanner, repository);
                    case "7" -> showPayroll(repository);
                    case "0" -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Employee Management System (Java + H2) ===");
        System.out.println("1. Add employee");
        System.out.println("2. List all employees");
        System.out.println("3. Find employee by ID");
        System.out.println("4. Update employee");
        System.out.println("5. Remove employee");
        System.out.println("6. List employees by department");
        System.out.println("7. Show total payroll");
        System.out.println("0. Exit");
    }

    private static void addEmployee(Scanner scanner, EmployeeRepository repository) throws SQLException {
        Integer id = readInt(scanner, "Employee ID: ");
        if (id == null) {
            return;
        }
        String name = readRequired(scanner, "Name: ");
        String department = readRequired(scanner, "Department: ");
        String role = readRequired(scanner, "Role: ");
        Double salary = readDouble(scanner, "Salary: ");
        if (salary == null) {
            return;
        }

        boolean created = repository.addEmployee(new Employee(id, name, department, role, salary));
        System.out.println(created ? "Employee added." : "Employee ID already exists or invalid data.");
    }

    private static void listAll(EmployeeRepository repository) throws SQLException {
        List<Employee> employees = repository.listEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees available.");
            return;
        }
        employees.forEach(System.out::println);
    }

    private static void findById(Scanner scanner, EmployeeRepository repository) throws SQLException {
        Integer id = readInt(scanner, "Employee ID: ");
        if (id == null) {
            return;
        }
        Optional<Employee> employee = repository.findById(id);
        System.out.println(employee.map(Employee::toString).orElse("Employee not found."));
    }

    private static void updateEmployee(Scanner scanner, EmployeeRepository repository) throws SQLException {
        Integer id = readInt(scanner, "Employee ID to update: ");
        if (id == null) {
            return;
        }

        Optional<Employee> existing = repository.findById(id);
        if (existing.isEmpty()) {
            System.out.println("Employee not found.");
            return;
        }

        Employee current = existing.get();
        String name = readOptional(scanner, "New name (leave blank to keep current): ", current.getName());
        String department = readOptional(scanner, "New department (leave blank to keep current): ", current.getDepartment());
        String role = readOptional(scanner, "New role (leave blank to keep current): ", current.getRole());
        Double salary = readOptionalDouble(scanner, "New salary (leave blank to keep current): ", current.getSalary());
        if (salary == null) {
            return;
        }

        boolean updated = repository.updateEmployee(new Employee(id, name, department, role, salary));
        System.out.println(updated ? "Employee updated." : "Employee not found.");
    }

    private static void removeEmployee(Scanner scanner, EmployeeRepository repository) throws SQLException {
        Integer id = readInt(scanner, "Employee ID to remove: ");
        if (id == null) {
            return;
        }
        boolean deleted = repository.removeEmployee(id);
        System.out.println(deleted ? "Employee removed." : "Employee not found.");
    }

    private static void listByDepartment(Scanner scanner, EmployeeRepository repository) throws SQLException {
        String department = readRequired(scanner, "Department name: ");
        List<Employee> employees = repository.listByDepartment(department);
        if (employees.isEmpty()) {
            System.out.println("No employees in this department.");
            return;
        }
        employees.forEach(System.out::println);
    }

    private static void showPayroll(EmployeeRepository repository) throws SQLException {
        System.out.printf("Total payroll: %.2f%n", repository.totalPayroll());
    }

    private static Integer readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number.");
            return null;
        }
    }

    private static Double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number.");
            return null;
        }
    }

    private static Double readOptionalDouble(Scanner scanner, String prompt, double defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid salary.");
            return null;
        }
    }

    private static String readRequired(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? "N/A" : input;
    }

    private static String readOptional(Scanner scanner, String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }
}
