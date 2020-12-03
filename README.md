# HandoverApp API

[![Build Status](https://travis-ci.com/HandoverAppGroup/handoverapp.svg?branch=main)](https://travis-ci.com/HandoverAppGroup/handoverapp)

## Deployment

The API is deployed to free dynos on Heroku which take a long time to start up and start accepting requests when they are sleeping. Therefore, there could be a long wait the first time you call the API.

You check out the deployed api [here](https://handoverapp.herokuapp.com/api/tasks).

## How to run

- Open in IntelliJ
- Alter `src/main/resources/application.properties` to reflect your own database setup (database url, username, password). Don't forget to set back to heroku settings before pushing to main.
- Run the bootRun application gradle task to run the api locally - you should then be able to post and get tasks at localhost:8080/api/tasks
- Additional endpoints allow you to filter by date, status and patient (see `TaskController`)
- Results are paginated, with the default page size being 30 items
- Run the test verification gradle task to run tests

## About

- Persistence layer managed by JPA, although some custom SQL queries are used
- Sends and receives data using a DTO (Data Transfer Object) for convenience so that separate patient objects do not need to be explicitly created by the user. A new patient is automatically created if needed.

## GET Endpoints

- GET `/tasks` : list all tasks
- GET `/tasks/today` : list all today's tasks
- GET `/tasks/uncompleted` : get all uncompleted tasks
- GET `/tasks/id` : get a task by id

## POST Endpoints

- POST `/tasks` : post a new task
- POST `/tasks/id/complete` : complete a task (json body is a Doctor object who is the task completer)

## PUT Endpoints

- PUT `/tasks/id` : change task details (cannot change date created or id)

## DELETE Endpoints 

- DELETE `/tasks/id` : delete task by id

## Example JSON

### A doctor

```json
{
  "name": "Dr. Donald Duck",
  "grade": "B"
}
```

### An uncompleted task

```json
{
  "id": 1,
  "completed": false,
  "dateCreated": "2020-12-01T21:00:14.639+00:00",
  "dateCompleted": null,
  "description": "Cut patient's leg off",
  "gradeRequired": "A",
  "patientMrn": "565656",
  "patientClinicalSummary": "Infected leg wound - needs amputation",
  "patientLocation": "Somewhere in Charing Cross Hospital",
  "creator": {
    "name": "Dr. Donald Duck",
    "grade": "B"
  },
  "completer": null
}
```

### A completed task

```json
{
  "id": 3,
  "completed": true,
  "dateCreated": "2020-12-01T21:03:24.664+00:00",
  "dateCompleted": "2020-12-01T21:03:24.673+00:00",
  "description": "Check on how the alien is sleeping",
  "gradeRequired": "A",
  "patientMrn": "123alien",
  "patientClinicalSummary": "Used to live on Mars, came to Earth to get treatment",
  "patientLocation": "Somewhere in Charing Cross Hospital",
  "creator": {
    "name": "Dr. Banana",
    "grade": "bananas cannot have a grade"
  },
  "completer": {
    "name": "Dr Richards",
    "grade": "A"
  }
}
```
