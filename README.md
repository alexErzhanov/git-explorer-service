## Git explorer service

This service provides a REST API for listing all non-fork GitHub repositories for a given user. The information returned
for each repository includes the repository name, owner login, and details of each branch like its name and the last
commit SHA.

## Features
- List all non-fork repositories for a given GitHub user.
- Get branch details including branch names and the last commit SHA for each non-fork repository.
- Handle non-existent users with a 404 response.
- Validate `Accept` header and respond with a 406 status code when not compliant.

## Prerequisites

- JDK 17+
- Maven
- Docker (for containerization)

## Local Setup

### Using Maven

1. Clone the repository:
   ```
   git clone https://github.com/alexErzhanov/git-explorer-service.git
   cd git-explorer-service
   ```

2. Compile and package the application using Maven Wrapper:
   ```
   ./mvnw clean package
   ``` 

3. Run the application:
   ```
   ./mvnw spring-boot:run
   ```

### Using Docker

1. Clone the repository:
   ```
   git clone https://github.com/alexErzhanov/git-explorer-service.git
   cd git-explorer-service
   ```

2. Build the Docker image:
   ```
   docker build -t git-explorer-service .
   ```

3. Run the Docker container:
   ```
   docker run -p 8080:8080 git-explorer-service
   ```

## API Usage

### List Non-Fork Repositories

- **GET** `/api/v1/repos/{username}`

  Fetches all non-fork repositories of the provided GitHub username.

  Required Header(default):
  ```
   Accept: application/json
  ```

  Example Request:
  ```
  curl --location 'http://localhost:8080/api/v1/repos/{username}' --header 'Accept: application/json'
  ```

## Swagger API Documentation

You can view and interact with the API documentation by visiting the Swagger UI after running the application:

  ```
    http://localhost:8080/swagger-ui.html
  ```

## Testing

To run unit tests, use the following command:
   ```
   ./mvnw clean test
   ```

To run both unit and integration tests, use the following command:
   ```
   ./mvnw clean verify
   ```