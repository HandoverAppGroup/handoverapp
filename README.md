# HandoverApp API

[![Build Status](https://travis-ci.com/HandoverAppGroup/handoverapp.svg?branch=main)](https://travis-ci.com/HandoverAppGroup/handoverapp)

- Open in IntelliJ
- Alter `src/main/resources/application.properties` to reflect your own database setup (database url, username, password). Don't forget to set back to heroku settings before pushing to main.
- Run the bootRun application gradle task to run the api locally - you should then be able to post and get tasks at localhost:8080/api/tasks
- Additional endpoints allow you to filter by date, status and patient (see `TaskController`)
- Results are paginated, with the default page size being 30 items
- Run the test verification gradle task to run tests

## About

- Persistence layer managed by JPA, although some custom SQL queries are used
- Sends and receives data using a DTO (Data Transfer Object) for convenience so that separate patient objects do not need to be explicitly created by the user. A new patient is automatically created if needed.
