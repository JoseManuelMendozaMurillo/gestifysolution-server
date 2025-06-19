# GestifySolution Server

GestifySolution Server is a backend project to manage furniture stores, including inventory, suppliers, orders, warehouses, and user authentication with Keycloak. The project is built with Spring Boot and provides a RESTful API for all management operations.

## How to Run the Project

### 1. Run with Docker Compose (Recommended)

1. Make sure you have Docker and Docker Compose installed.
2. Copy `env.template` to `.env` and fill in the required environment variables.
3. From the root of the project, run:
   ```sh
   docker compose up --build
   ```
4. The server will be available at [http://localhost:8080](http://localhost:8080) (or the port you set in your `.env` as `SERVER_EXPOSED_PORT`).
5. Keycloak will be available at [http://localhost:9090](http://localhost:9090) (or the port you set in your `.env`).

### 2. Run with DevContainers (VS Code or Cursor)

1. Open the project folder in VS Code or Cursor.
2. If prompted, reopen in a Dev Container.
3. The development environment will be set up automatically, including all dependencies and services.
4. The server and services will be available at the same ports as above.

## API Documentation

The API documentation is generated with **Swagger**.

- Once the server is running, access the Swagger UI at:
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

For more details, see the code and configuration files in this repository.
