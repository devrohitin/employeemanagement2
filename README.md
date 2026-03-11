# Basic Employee Management System (Spring Boot + PostgreSQL)

Simple Employee Management backend using **Spring Boot** and **PostgreSQL**.

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
- Spring Boot (Web + Spring Data JPA)
- PostgreSQL
- Maven

## Configure PostgreSQL
Default config is in `src/main/resources/application.properties`:
- DB: `employee_db`
- User: `postgres`
- Password: `postgres`

Update these values as needed.

## Run
```bash
mvn spring-boot:run
```

## API endpoints
- `POST /api/employees`
- `GET /api/employees`
- `GET /api/employees?department=Engineering`
- `GET /api/employees/{id}`
- `PUT /api/employees/{id}`
- `DELETE /api/employees/{id}`
- `GET /api/employees/payroll/total`

## Test
```bash
mvn test
```

App seeds sample employees automatically if table is empty.
