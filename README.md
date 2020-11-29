# SpringBootExample

- Open in IntelliJ
- Alter src/main/resources/application.properties to reflect your own database setup (database url, username, password etc.)
- Run the bootRun application gradle task to run the api - you should then be able to post and get tasks and localhost:8080/api/tasks
- Run the test verification gradle task to run tests

## About this version

- This version is a more "from scratch" implementation as opposed to spring-boot-data-rest
- A custom controller and service are defined
- Persistence layer still managed by JPA, although some custom SQL queries are used
- Not strictly REST-compliant / no hypermedia
- Data is POST-ed using a DTO (Data Transfer Object) for convenience so that separate patient and doctor objects do not need to be explicitly created by the user. A new patient or doctor is automatically created if needed.
