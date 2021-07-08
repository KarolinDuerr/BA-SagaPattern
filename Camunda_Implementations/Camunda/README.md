# Saga Pattern Realization With Camunda
This project includes an example implementation of the Saga pattern using [Camunda](https://github.com/camunda/camunda-bpm-platform/tree/master/spring-boot-starter).
The example application represents a travel application that consists of three backend services: TravelService,
HotelService and FlightService. For simplicity reasons, only the workflow for booking a trip has been implemented.

## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up --no-start`


3. Execute `docker-compose start mysql`


4. Execute `docker-compose start travelservice`


5. Execute `docker-compose up`


6. Requesting trip bookings is now possible. Either use `curl` commands,
   the provided `TravelApplication.json` insomnia file, which includes different trip booking requests,
   or access the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the different services:

   | __Service__ | __URL to Swagger UI__ |
   |:-------|:-------------------:|
   |TravelService| http://localhost:8090/swagger-ui.html
   |HotelService| http://localhost:8081/swagger-ui.html
   |FlightService| http://localhost:8082/swagger-ui.html

An example for such a request:
```
{
    "duration":
    {
        "start":"2021-12-01",
        "end":"2021-12-12"
    },
    "start":
    {
        "country":"Scotland",
        "city":"Stirling"
    },
    "destination":
    {
        "country":"Sweden",
        "city":"Stockholm"
    },
    "travellerName": "Max Mustermann",
    "boardType":"breakfast",
    "customerId":"1"
}
```

To simulate a Saga that fails because no hotel or no flight is available, use one of the following Strings
as `destination country` in the trip booking request:
```
"Provoke hotel failure"

"Provoke flight failure"
```

The services also provide a *health* and an *info* endpoint that show some information about the system like
that the DB is up and running. These endpoints can be accessed via:

| __Service__ | __URL to health endpoint__ |  __URL to info endpoint__ |
|:-------:|------------------|-------------------|
|TravelService| http://localhost:8090/api/travel/monitor/health | http://localhost:8090/api/travel/monitor/info
|HotelService| http://localhost:8081/api/hotels/monitor/health | http://localhost:8081/api/hotels/monitor/info
|FlightService| http://localhost:8082/api/flights/monitor/health | http://localhost:8082/api/flights/monitor/info


If you are on Windows or Mac, you sometimes have to replace _localhost_ with the default IP of your docker machine (use `docker-machine ip default` to get this default IP).

## Stop the Application

To stop the application and remove the created containers, execute the following command:
```
docker-compose down --remove-orphans
```

----------------------------

## General Saga Characteristics

### External Compensation Trigger

In order to start the compensation of a currently running Saga externally, a BPMN error has to be created for a scheduled
or still unfinished task. Before the BPMN error can be created, the task has to be fetched and locked if not an already
existing worker has done that. To achieve that, the following request, supplemented with the missing information, has to
be sent as POST request to the engine at: `http://localhost:8090/engine-rest/external-task/fetchAndLock`.
Since the implementation uses mainly external tasks, the BPMN error will be created for an external task which means the
request is sent to the engine's `/external-task` endpoint. If an existing worker has already fetched the respective task,
this request can be skipped.

```
 {
      "workerId":"Any name, e.g. compensationProvoker",
      "maxTasks":2,
	  "usePriority":true,
      "topics":
      [
         {
            "topicName": "Name of the topic where the task will be sent to, e.g. bookFlight",
            "lockDuration": 10000
         }
      ]
 }
```

Now that the respective task is locked and fetched, a BPMN error can be created. Therefore, the following request
supplemented with the missing information has to be sent as POST request to the engine at:
`http://localhost:8090/engine-rest/external-task/{taskId}/bpmnError`

```
{
"workerId": "Name specified in previous request: compensationProvoker",
"errorCode": "Error Code that triggers the compensation event (check the bpmn definition for that), e.g. FLIGHT_ERROR",
"errorMessage": "Any message: Provoke compensation externally"
}
```


The `TravelApplication.json` insomnia file also includes these requests within the `ExternalCompensationTrigger` directory.

----------------------------

## Monitor the Application

### Camunda's Cockpit
The cockpit can be accessed either just via http://localhost:8090/
or by using the whole path http://localhost:8090/camunda/app/welcome/default/#!/welcome.

The following credentials are needed in order to be able to access the cockpit:
Username:   admin
Password:   admin

If the respective values have been changed in the application.properties file of the TravelService
the new values have to be used for the username and the password.

### Log Files
Each service provides a log that contains some information about it.
The logs can be accessed using the name of the relevant container.
The different logs can be accessed using the following commands:

| __Log of__ | __Command to execute__ |
|:-------|:-------------------|
|TravelService| `docker logs travelservice_camunda`|
|HotelService| `docker logs hotelservice_camunda`|
|FlightService|  `docker logs flightservice_camunda`|

By using the `--follow` supplement, it will be continued to stream the service's output to the console.

The logging level can be changed in the respective `application.properties` file.
