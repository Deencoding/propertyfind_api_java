# Property Find API

A backend RESTful API for managing property listings, user authentication, and notifications.  
This API allows users to search for properties, register as home seekers or providers, and interact securely using JWT authentication.

---

## Features

- User registration and login for Home Seekers and Providers
- JWT-based authentication for secure endpoints
- CRUD operations for property listings
- Admin role for managing users and listings

---

## Technologies Used

- Java 21
- Spring Boot 3.5
- Spring Security with JWT
- PostgreSQL
- JPA / Hibernate
- JDBC
- Maven

---

## Getting Started

### Prerequisites

- Java 21 or higher
- PostgreSQL
- Maven

### Installation

1. Clone the repository:

```bash
git clone https://github.com/Deencoding/propertyfind_api_java.git
```

2. Configure the database in application.properties

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/propertyfind
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
```

3. Build and run the project:

```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at: http://localhost:8080

## API Endpoints

### Authentication

- POST /auth/register – Register a new user
- POST /auth/login – Login and receive a JWT token

### Properties

- GET /properties – Retrieve all properties
- POST /properties – Create a new property (requires JWT)
- GET /properties/{id} – Retrieve a property by ID
- PUT /properties/{id} – Update a property (requires JWT)
- DELETE /properties/{id} – Delete a property (requires JWT)

### Users

- GET /users – Retrieve all users (Admin only)
- GET /users/{id} – Retrieve a user by ID

### Database

Main entities:

- UserEntity – Stores user details, roles, and registration date
- PropertyEntity – Stores property information such as name, location, price, and description


### Future Improvements

- Implement property search filters
- Add image upload support
- Add pagination for property listings
- Add email notifications



