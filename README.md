# HandoverApp API

[![Build Status](https://travis-ci.com/HandoverAppGroup/handoverapp.svg?branch=main)](https://travis-ci.com/HandoverAppGroup/handoverapp)

## Deployment

The API is deployed to free dynos on Heroku which take a long time to start up and start accepting requests when they are sleeping. Therefore, there could be a long wait the first time you call the API.

You check out the deployed api [here](https://handoverapp.herokuapp.com/api/tasks).

## How to run

- Open in IntelliJ
- Alter `src/main/resources/application.properties` to reflect your own database setup (database url, username, password). Don't forget to set back to heroku settings before pushing to main.
- Run the bootRun application gradle task to run the api locally - you should then be able to post and get tasks at `localhost:8080/api/tasks`
- Additional endpoints allow you to filter by date, status and patient
- Results are paginated, with the default page size being 30 items
- Run the test verification gradle task to run tests

## About

- Persistence layer managed by JPA, although some custom SQL queries are used
- Sends and receives data using a DTO (Data Transfer Object) for convenience so that separate patient objects do not need to be explicitly created by the user. A new patient is automatically created if needed.

## GET Endpoints

- GET `/tasks` : list all tasks
- GET `/tasks/recent` : list all tasks created in the last 48 hours
- GET `/tasks/uncompleted` : list all uncompleted tasks
- GET `/tasks/{id}` : retrieve a task by id
- GET `/tasks/byDate?earliestDate={yyyy-MM-dd-hh-mm}&latestDate={yyyy-MM-dd-hh-mm}` : list tasks within a certain date range
- GET `/tasks/byPatient?mrn={mrn}` : list all tasks for a patient

### Addition paging query parameters

- You can always add the `page={page number}` and `size={page size}` query parameters to paginate through many results
- For task lists longer than 30 items, the default page size is 30, and the default page number is 0 (the first page)

### Example JSON Responses

Note: all GET calls return the 200 status code if successful

#### An uncompleted task

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
  "plannedCompleter": {
    "name": "Dr. Davidson",
    "grade": "A"
  },
  "completer": null
}
```

#### A completed task

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
  "plannedCompleter": {
    "name": "Dr. Davidson",
    "grade": "A"
  },
  "completer": {
    "name": "Dr Richards",
    "grade": "A"
  }
}
```



## POST Endpoints

- POST `/tasks` : post a new task (successful creation will return 201 status code)

```json
{
  "description": "Cut patient's leg off",
  "gradeRequired": "A",
  "patientMrn": "565656",
  "patientClinicalSummary": "Infected leg wound - needs amputation",
  "patientLocation": "Somewhere in Charing Cross Hospital",
  "creator": {
    "name": "Dr. Donald Duck",
    "grade": "B"
  }
}
```

- POST `/tasks/{id}/complete` : complete a task (json body is a Doctor object who is the task completer) (successful completion will return 200 status code)

```json
{
  "name": "Dr. Donald Duck",
  "grade": "B"
}
```

- POST `/tasks/{id}/claim` : claim a task (json body is a Doctor object who is the task completer) (successful claim will return 200 status code)

```json
{
  "name": "Dr. Davidson",
  "grade": "A"
}
```

## PUT Endpoints

- PUT `/tasks/id` : change task details (successful edit will return 200 status code)

```json
{
  "description": "Cut patient's arm off",
  "gradeRequired": "B",
  "patientMrn": "565656",
  "patientClinicalSummary": "Infected arm - needs amputation",
  "patientLocation": "Somewhere specific in Charing Cross Hospital",
  "creator": {
    "name": "Dr. Donald Duck",
    "grade": "B"
  }
}
```

## DELETE Endpoints 

- DELETE `/tasks/id` : delete task by id (successful deletion will return 204 status code)

