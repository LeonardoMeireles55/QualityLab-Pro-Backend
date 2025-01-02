# QualityLab Pro-API RESTful for internal laboratory quality control.

<p align="center">
<img src="https://img.shields.io/static/v1?label=STATUS&message=In%20progress&color=RED&style=for-the-badge" alt="Em desenvolvimento"/>
</p>

## Technologies

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)

## Description
The RESTful API for Laboratory Internal Quality Control is designed to assist clinical and research laboratories in monitoring and controlling the quality of their processes. This API provides endpoints to manage information related to control standards, test results, statistical analyses, and other activities essential for ensuring the accuracy and reliability of laboratory data.

## Implemented functionalities
The API efficiently manages package insert values and test values, offering simple and intuitive CRUD operations.

## Project Structure
```
src
├── main
│   ├── java
│   │   └── leonardo
│   │       └── labutilities
│   │           └── qualitylabpro
│   │               ├── ControlApplication.java
│   │               ├── configs
│   │               ├── constants
│   │               ├── controllers
│   │               ├── dtos
│   │               ├── entities
│   │               ├── enums
│   │               ├── repositories
│   │               ├── services
│   │               └── utils
│   └── resources
│       ├── META-INF
│       │   └── spring.factories
│       ├── aplication-local.properties
│       ├── application-dev.properties
│       ├── application-mariadb.properties
│       ├── application-prod.properties
│       ├── application-test.properties
│       ├── application.properties
│       └── db
│           └── migrations
└── test
    └── java
        └── leonardo
            └── labutilities
                └── qualitylabpro
                    ├── ControlApplicationTests.java
                    └── repository
```

### Key Directories:
- `config/`: Configuration classes
- `controller/`: REST API endpoints
- `domain/`: Core business logic and data structures
- `infra/`: Infrastructure components like security and exception handling
- `service/`: Business service implementations
- `resources/`: Application properties and database migrations
- `test/`: Unit and integration tests

## Requirements
* [Java 21](https://www.oracle.com/br/java/technologies/javase/jdk21-archive-downloads.html)
* [Maven](https://maven.apache.org/)
* [Docker](https://www.docker.com/get-started/)
* [Git](https://git-scm.com/)

## Installation

#### Step 1. Clone the repository
Run the command below in Git Bash or Terminal to clone the repository:
```
git clone https://github.com/LeonardoMeireles55/QualityLabPro.git
```

#### Step 2. Upload the Docker container to create the MariaDB database
In the project root directory, run the command:
```
docker-compose up or docker compose up
```

## Usage

#### Step 3. Access API documentation
```
http://localhost:8080/swagger-ui.html
```

## Services

### CoagulationAnalyticsService
Handles analytics related to coagulation tests.

### BiochemistryAnalyticsService
Handles analytics related to biochemistry tests.

### HematologyAnalyticsService
Handles analytics related to hematology tests.

## Controllers

### CoagulationAnalyticsController
Manages endpoints for coagulation analytics.

### BiochemistryAnalyticsController
Manages endpoints for biochemistry analytics.

### HematologyAnalyticsController
Manages endpoints for hematology analytics.



## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## React PlotlyJs Front-end
<img width="1470" alt="Screenshot 2024-12-06 at 18 01 35" src="https://github.com/user-attachments/assets/e43c369e-9989-4624-a495-197d09bf2245">
<img width="1470" alt="Screenshot 2024-12-06 at 18 01 35" src="https://github.com/user-attachments/assets/595fe840-c244-4f24-86f7-3ae191252cc7">






