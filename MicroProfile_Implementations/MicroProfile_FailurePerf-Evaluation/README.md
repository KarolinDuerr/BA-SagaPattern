# MicroProfile LRA Failure Performance Evaluation
This project is part of the evaluation of a Saga pattern implementation using [MicroProfile LRA](https://github.com/eclipse/microprofile-lra).
Additional sections to the original [Saga Pattern Realization with MicroProfile LRA](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/MicroProfile_Implementations/MicroProfile)
have been included that simulate different failure scenarios given a particular input.

## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up`


3. Requesting trip bookings is now possible. Either use `curl` commands,
   the provided `TravelApplication.json` insomnia file, which includes different trip booking requests,
   or access the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the different services:

    <table>
        <tr>
            <th>Service</th>
            <th style="text-align:center">URL to Swagger UI</th>
        </tr>
        <tr>
            <td>TravelService</td>
            <td rowspan="2" align="center"><a href="http://localhost:8090/openapi/ui/">http://localhost:8090/openapi/ui/</a></td>
        </tr>
        <tr>
            <td>LRA Coordinator (included in TravelService)</td>
        </tr>
        <tr>
            <td>HotelService</td>
            <td><a href="http://localhost:8081/openapi/ui/">http://localhost:8081/openapi/ui/</a></td>
        </tr>
        <tr>
            <td>FlightService</td>
            <td><a href="http://localhost:8082/openapi/ui/">http://localhost:8082/openapi/ui/</a></td>
        </tr>
    </table>



To simulate a Saga that fails because __no hotel__ or __no flight__ is __available__, use one of the following Strings
as `destination country` in the trip booking request:
```text
"Provoke hotel failure"

"Provoke flight failure"
```

The services also provide a general *health* endpoint that shows information about the system whether it is up and running.
These endpoints can be accessed via:

| __Service__ | __URL to health endpoint__ |
|:-------:|------------------|
|TravelService| http://localhost:8090/health |
|HotelService| http://localhost:8081/health |
|FlightService| http://localhost:8082/health |

If you are on Windows or Mac, you sometimes have to replace _localhost_ with the default IP of your docker machine (use `docker-machine ip default` to get this default IP).

## Stop the Application

To stop the application and remove the created containers, execute the following command:
```shell
docker-compose down --remove-orphans
```

-------------------------------------------------

## Provoke Failure Scenarios
The respective String has to be used as `destination country` in the trip booking request to provoke a participant failure.

An example for such a request:
```json
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
        "country":"Provoke orchestrator failure while starting trip booking",
        "city":"Bamberg"
    },
    "travellerName": "Orchestrator Start",
    "boardType":"All-inclusive",
    "customerId":"4"
}
```

### 1. Saga Participant failure
- Provoke a failure of the __FlightService__ participant __before__ it started to execute a local transaction with the following string as `destination country`:
    ```text
    "Provoke participant failure before receiving task"
    ```
  The __HotelService__ terminates then the docker container of the __FlightService__ while it is executing the *bookHotel* request.
  Afterwards, the __FlightService__ has to be __restarted__ manually to investigate what happens as soon as the service is running again.
  This can be done using one of the following commands:
    ```shell
    docker-compose start flightservice

    docker start flightservice_conductorFailurePerf
    ```

  If the container name of the __FlightService__ has been changed in the `docker-compose.yml` file, the
  container has to be started using this name with the `docker start` command.
  The same applies for the `docker-compose start` command if the service name of the __FlightService__ has been changed
  in the `docker-compose.yml` file.


- Provoke a __termination__ failure of the __FlightService__ participant __while executing__ a local transaction of the *BookTripSaga* with the following string as `destination country`:
    ```text
    "Provoke participant failure while executing"
    ```  
  The __FlightService__ forces then its JVM to terminate itself, after booking a flight but before informing the orchestrator about it, in order to simulate a *sudden failure* of the system.
  Afterwards, the __FlightService__, again, has to be __restarted__ using the same commands as above.


- Provoke an __exception__ in the __FlightService__ participant __while executing__ a local transaction of the *BookTripSaga* with the following string as `destination country`:
    ```text
    "Provoke exception while executing"
    ```  
  The __FlightService__ throws then a *RuntimeException* while booking a flight to simulate *unexpected behaviour* of the system.
  Afterwards, the behaviour of the service can be observed. The easiest way is to have a look at the log of the __FlightService__ during that time.
  This can be done using the following command:
  ```shell
  docker logs flightservice_conductorFailurePerf --follow
   ```  

### 2. Saga Orchestrator failure
The __LRA Coordinator__ within the __TravelService__, as well as the __TravelService__ itself are the orchestrator in this example application. Consequently, observing the system's behaviour during orchestrator failures involves failures of this service.

- Provoke a failure of the __TravelService__ while a trip booking __is being started__ is not considered, since the __TravelService__ is needed in order to make booking requests.


- Provoke a failure of the __TravelService while executing__ a local transaction of the *BookTripSaga* with the following string as `destination country`:
    ```text
    "Provoke orchestrator failure while executing"
    ```  
  The __FlightService__ terminates then the docker container of the __TravelService__ after booking a flight, but before informing the orchestrator about it.
  Afterwards, the __TravelService__ has to be __restarted__ manually to investigate what happens as soon as the service is running again.
  This can be done using one of the following commands:
  ```shell
    docker-compose start conductor-server-ui

    docker start conductor-server-ui
  ```

  If the container name of the __TravelService__ has been changed in the `docker-compose.yml` file, the
  container has to be started using this name.


### 3. Breach of Saga protocol
// TODO
