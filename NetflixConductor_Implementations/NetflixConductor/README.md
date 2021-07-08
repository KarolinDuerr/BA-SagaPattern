# Saga Pattern Realization With Netflix Conductor
This project includes an example implementation of the Saga pattern using [Netflix Conductor](https://github.com/Netflix/conductor).
The example application represents a travel application that consists of three backend services: TravelService,
HotelService and FlightService. For simplicity reasons, only the workflow for booking a trip has been implemented.

## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up --no-start`


3. Execute `docker-compose start elasticsearch`


4. Execute `docker-compose start dynomite`


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

Additionally, the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the __Conductor server__ can be accessed via
http://localhost:8080/.

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

In order to start the compensation of a currently running Saga externally, a task that is either presently *RUNNING*
or *SCHEDULED* can be marked as *FAILED* using the API of the Conductor server. Already *COMPLETED* tasks cannot be marked
as *FAILED* and thus not be used to start the compensation workflow. The following request, supplemented with the
missing information, has to be sent as POST request to the Conductor server at: `http://localhost:8080/tasks`

```
{
"workflowInstanceId": "ID of workflow instance that is supposed to be compensated.",
"taskId": "ID of the task that is marked as FAILED to start compensation.",
"reasonForIncompletion" : "Reason for failure",
"callbackAfterSeconds": 0,
"status": "FAILED_WITH_TERMINAL_ERROR"
}
```

By using the *FAILED_WITH_TERMINAL_ERROR* status, Conductor immediately starts the compensation workflow and does not
retry the failed task even if a retry configuration exists.


The `TravelApplication.json` insomnia file also includes this request within the `ExternalCompensationTrigger` directory. 

---------------------------

## Monitor the Application

### Conductor's UI
The project already includes the UI module in the __Conductor Server__ that is started using
`docker-compose`.

The UI can be accessed via http://localhost:5000/.

### Log Files
Each service provides a log that contains some information about it.
The logs can be accessed using the name of the relevant container.
The different logs can be accessed using the following commands:

| __Log of__ | __Command to execute__ |
|:-------|:-------------------|
|TravelService| `docker logs travelservice_conductor`|
|HotelService| `docker logs hotelservice_conductor`|
|FlightService|  `docker logs flightservice_conductor`|
|Conductor Server|  `docker logs conductor-server-ui`|

By using the `--follow` supplement, it will be continued to stream the service's output to the console.

The logging level can be changed in the respective `application.properties` file.

### Metrics of the Conductor Server and the Java Client
The __Conductor Server__ publishes some metrics concerning the server and the client, like the amount of time it takes to execute a task.

The necessary gradle dependency has already been added to the services.
To connect the metrics registry with the logging framework, two lines within the `config.properties`
have to be __activated__ as they are currently commented out:
```
conductor.additional.modules=com.netflix.conductor.contribs.metrics.MetricsRegistryModule,com.netflix.conductor.contribs.metrics.LoggingMetricsModule
com.netflix.conductor.contribs.metrics.LoggingMetricsModule.reportPeriodSeconds=15
```
These lines can be found
beneath the `Additional modules for metrics collection (optional)` comment within the file.  

Additionally, it can be configured that the metrics will be printed into a dedicated file instead
of printing it as a regular log message.
To achieve that, the `conductor-server-ui` service within the `docker-compose.yml` file has to
include another environment variable: `LOG4J_PROP=log4j-file-appender.properties`.
The referenced file is already included within the project's `serverAndUi` directory.
It configures the different logging properties.

These logs could be further processed using a collector such as ElasticSearch and then visualized
with [Kibana UI](https://www.elastic.co/de/kibana). However, this has not been realized within this project
for information about how to achieve that see Netflix Conductor's official [documentation](https://netflix.github.io/conductor/metrics/server/).
