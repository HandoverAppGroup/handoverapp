# HandoverApp API

- Open in IntelliJ
- Alter src/main/resources/application.properties to reflect your own database setup (database url, username, password etc.)
- Run the bootRun application gradle task to run the api - you should then be able to post and get tasks and localhost:8080/api/tasks
- Run the test verification gradle task to run tests

## About

- Persistence layer still managed by JPA, although some custom SQL queries are used
- Data is POST-ed using a DTO (Data Transfer Object) for convenience so that separate patient bjects do not need to be explicitly created by the user. A new patient is automatically created if needed.
