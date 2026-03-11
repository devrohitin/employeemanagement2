package com.example.employeemanagement;

public class Employee {
    private final int id;
    private final String name;
    private final String department;
    private final String role;
    private final double salary;

    public Employee(int id, String name, String department, String role, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.role = role;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getRole() {
        return role;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Department: " + department +
                " | Role: " + role + " | Salary: " + String.format("%.2f", salary);
    }
}
