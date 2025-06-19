# GestifySolution Server

GestifySolution Server is a backend project to manage furniture stores, including inventory, suppliers, orders, warehouses, and user authentication with Keycloak. The project is built with Spring Boot and provides a RESTful API for all management operations.

## Storage with MinIO

This project uses [MinIO](https://min.io/) as an S3-compatible object storage service for file and asset management. MinIO is included as a service in both the Docker Compose and DevContainer setups.

- **MinIO Console:** [http://localhost:9001](http://localhost:9001) (the port can be configured in your `.env` file)
- **MinIO S3 API:** [http://localhost:9000](http://localhost:9000) (the port can be configured in your `.env` file)
- **Credentials:**  
  The MinIO username and password are set in your `.env` file using the `MINIO_ROOT_USER` and `MINIO_ROOT_PASSWORD` variables.  
  By default, these are:
  - User: `admin`
  - Password: `minioadmin`
  You can change these values in the `.env` file to suit your needs.

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
6. MinIO will be available at [http://localhost:9001](http://localhost:9001) (console) and [http://localhost:9000](http://localhost:9000) (S3 API).

### 2. Run with DevContainers (VS Code or Cursor)

1. Before opening the project, copy the `env.template` file into the `.devcontainer` folder and rename it to `.env`:
   ```sh
   cp env.template .devcontainer/.env
   ```
2. Open `.devcontainer/.env` and fill in the required environment variables.
   - **Important:** Make sure that the values you set for the variables (such as container names, ports, and service names) match the corresponding service names and settings defined in the `docker-compose.yml` file in this folder. This ensures that Docker Compose can correctly link the services and that everything works as expected.
3. Open the project folder in VS Code or Cursor.
4. If prompted, reopen in a Dev Container.
5. The development environment will be set up automatically, including all dependencies and services.
6. The server and services will be available at the same ports as above.

## API Documentation

The API documentation is generated with **Swagger**.

- Once the server is running, access the Swagger UI at:
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

For more details, see the code and configuration files in this repository.
