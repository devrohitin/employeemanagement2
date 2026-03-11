# Basic Employee Management System (Java + H2)

A simple employee management system built in **Java** with an **H2 database**.

## Main points
- Add employee
- View all employees
- Search employee by ID
- Update employee details
- Remove employee
- View employees by department
- Show total payroll

## Tech stack
- Java 17
- H2 Database (JDBC)
- Maven
- JUnit 5 (tests)

## Run
```bash
mvn clean compile
mvn -q exec:java -Dexec.mainClass="com.example.employeemanagement.Main"
```

## Test
```bash
mvn test
```

The app seeds a few employees on first run.
