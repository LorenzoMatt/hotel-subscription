
# Hotel Subscription Management System

## Description
This project implements a subscription management system for hoteliers, featuring a backend API and a frontend interface that allows users to manage hotel subscriptions.

## Technologies Used
- **Backend**: Spring Boot, Kotlin
- **Frontend**: Angular, Typescript
- **Database**: PostgreSQL

## Getting Started

### Prerequisites
- Docker installed on your machine.

### Installation
1. Unzip the project to your desired directory.
2. Navigate to the root of the project where the `docker-compose.yml` file is located.

### Running the Application
To build and start all services from the root of the project, execute:
```bash
docker-compose up --build
```

### Accessing the Application
Access the frontend client at [http://localhost/](http://localhost/).
## Database Access

The application is configured to connect to a PostgreSQL database. Access details for the database, along with default values, are as follows:

- **URL**: `jdbc:postgresql://db:5432/${PG_DB}` (Default URL constructed using database name, which is `jdbc:postgresql://db:5432/postgres`)
- **Username**: `${PG_USER}` (Default: `postgres`)
- **Password**: `${PG_PASSWORD}` (Default: `password`)
- **Schema**: `${DB_SCHEMA}` (Default: `hotel_subscription`)

These values are set as environment variables in the `.env` file located in the root directory of the project. You can customize the database username, password, database name and schema by modifying the respective entries in this file.

## API Documentation
The Swagger UI can be accessed at:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Additionally, the raw OpenAPI specification can be accessed directly at:

- **OpenAPI Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Environment Variables
Below are the key environment variables used in the project, which can be found and modified in the `.env` file:
- **`DB_PORT`**: The port used by the PostgreSQL database (Default: `5432`).
- **`PG_DB`**: The name of the PostgreSQL database (Default: `postgres`).
- **`PG_USER`**: The username for accessing the PostgreSQL database (Default: `postgres`).
- **`PG_PASSWORD`**: The password for the PostgreSQL user (Default: `password`).
- **`DB_URL`**: The JDBC URL for connecting to the PostgreSQL database, constructed using the environment variables (Default: `jdbc:postgresql://db:5432/${PG_DB}`).
- **`LOGGING_LEVEL_CONSOLE`**: The logging level for console output (Default: `INFO`).
- **`LOGGING_LEVEL_ROOT`**: The root logging level (Default: `INFO`).
- **`LOG_DIRECTORY`**: The directory where logs are stored (Default: `/var/log/hotel_subscription`).
- **`BACKEND_PORT`**: The port on which the backend server runs (Default: `8080`).
- **`DEBUG_PORT`**: The port used for debugging purposes (Default: `5005`).
- **`FRONTEND_PORT`**: The port on which the frontend client is accessible (Default: `80`).
- **`DB_SCHEMA`**: The specific database schema name to be used (Default: `hotel_subscription`).
- **`SEED_SUBSCRIPTIONS`**: Flag to enable or disable database seeding on startup (Default: `true`).

## Implemented Functionalities

### Batch Processing
- A batch job is implemented to periodically update the status of subscriptions from "ACTIVE" to "EXPIRED". This job checks if the `nextPayment` date is less than the current date and runs daily at one minute past midnight.


### RESTful Services
The project implements the following RESTful services as specified in the requirements:
- **Start a New Subscription**: Allows starting a new subscription with either MONTHLY or YEARLY terms, which can begin on a future date.
- **Cancel a Subscription**: Enables the cancellation of active subscriptions.
- **List All Subscriptions**: Retrieves a list of all subscriptions along with their status and next payment date.
- **Restart a Subscription**: Provides the ability to restart a previously cancelled subscription.
- **Filtering by Status**: Retrieves a list of subscriptions filtering by status (ACTIVE, EXPIRED, CANCELED).
- **Filtering by Month**: Retrieves a list of subscriptions filtering by start date month.

### Audit Table
- Utilizes Spring Data Envers to maintain a history of changes in the subscriptions.

### Frontend
A frontend client that allows users to perform all operations exposed by the backend:
- **Subscription Management**: Start, cancel, and restart subscriptions.
- **View Subscriptions**: View a list of all subscriptions.
- **Advanced Filtering**: Utilize filtering options to view subscriptions by status and start date month.




## Future Improvements
### Security Enhancements
- **Implement Authentication**: Introduce an authentication mechanism, such as OAuth2 or JWT, to ensure that only authorized users can access the application.

### Audit Enhancements
- **Enhance Audit Table Contents**: Augment the audit tables to include more contextual metadata, such as user actions, IP addresses, timestamps, and more detailed descriptions of changes. This would provide a richer and more useful audit trail that can be useful for compliance and monitoring.

### User Interface Improvements
- **GUI Enhancements**: Redesign the graphical user interface to be more appealing and intuitive.

### Configuration Robustness
- **Dynamic Backend Port Configuration**: Address the issue where changes in the backend service port (from the default 8080 to another) break the frontend. Implement a mechanism to dynamically set and update the frontend's API URL based on the backend's current port. This will ensure that the frontend remains functional and accessible even if the backend port is reconfigured.

