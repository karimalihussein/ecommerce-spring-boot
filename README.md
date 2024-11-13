# E-commerce Spring Boot Application

This project is a simple e-commerce platform built using **Spring Boot**. It includes core features like user authentication, product management, order processing, and integration with **JWT** for secure API access.

## Features

- **User Authentication**: Registration and login functionality with password encryption using **BCrypt**.
- **JWT Integration**: Secure API endpoints using **JSON Web Tokens**.
- **Product Management**: CRUD operations for products.
- **Order Management**: Place and manage user orders.
- **Inventory Tracking**: Track product stock levels.
- **Validation**: Input validation using `javax.validation`.
- **Database Seeding**: Automatically populate the database on startup.

## Technologies Used

- **Spring Boot** 3.3.x
- **MySQL** for database
- **Spring Security** for authentication and authorization
- **Java JWT** for token-based security
- **Lombok** to reduce boilerplate code
- **BCrypt** for password encryption

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/karimalihussein/ecommerce-spring-boot.git
   cd ecommerce-spring-boot 
   ```
2. Create a MySQL database and update the configuration in `src/main/resources/application.properties`.
3. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
4. The application can be accessed at `http://localhost:8081`.

## API Endpoints

Access the API

	•	Register: POST /api/auth/register
	•	Login: POST /api/auth/login
	•	Products: GET /api/products
	•	Orders: GET /api/orders

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - Framework for creating Spring applications.
- [Maven](https://maven.apache.org/) - Dependency management.
- [MySQL](https://www.mysql.com/) - Open-source relational database.
- [Spring Security](https://spring.io/projects/spring-security) - Authentication and access control framework.
- [Java JWT] - JSON Web Token for Java and Android.
- [Lombok](https://projectlombok.org/) - Library to reduce boilerplate code.

## Author

- **Karim Ali Hussein** - [karimalihussein](https://github.com/karimalihussein)

## License

This project is open-source and available under the [MIT License](LICENSE).

Contributions are welcome! If you found a bug or want to suggest a new feature, feel free to open an issue or submit a pull request.








   