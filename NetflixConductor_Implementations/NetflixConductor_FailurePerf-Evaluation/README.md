# Netflix Conductor Failure Performance Evaluation
This project is part of the evaluation of a Saga pattern implementation using [Netflix Conductor](https://github.com/Netflix/conductor).
Additional sections to the original [Saga Pattern Realization With Netflix Conductor](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/NetflixConductor)
have been included that simulate different failure scenarios given a particular input.

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

To simulate a Saga that fails because __no hotel__ or __no flight__ is __available__, use one of the following Strings
as `destination country` in the trip booking request:
```
"Provoke hotel failure"

"Provoke flight failure"
```
Additionally, the __Conductor UI__ can be accessed via 
http://localhost:5000/ and the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the __Conductor server__ via
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

-------------------------------------------------

## Provoke Failure Scenarios
The respective String has to be used as `destination country` in the trip booking request to provoke a participant failure.

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
    ```  
    "Provoke participant failure before receiving task"
    ```
  The __HotelService__ terminates then the docker container of the __FlightService__ while it is executing the *bookHotel* request.
  Afterwards, the __FlightService__ has to be __restarted__ manually to investigate what happens as soon as the service is running again.
  This can be done using one of the following commands:
    ```
    docker-compose start flightservice
    
    docker start flightservice_conductorFailurePerf
    ```

  If the container name of the __FlightService__ has been changed in the `docker-compose.yml` file, the
  container has to be started using this name with the `docker start` command.
  The same applies for the `docker-compose start` command if the service name of the __FlightService__ has been changed
  in the `docker-compose.yml` file.


- Provoke a __termination__ failure of the __FlightService__ participant __while executing__ a local transaction of the *BookTripSaga* with the following string as `destination country`:
    ```  
    "Provoke participant failure while executing"
    ```  
  The __FlightService__ forces then its JVM to terminate itself, after booking a flight but before informing the orchestrator about it, in order to simulate a *sudden failure* of the system.
  Afterwards, the __FlightService__, again, has to be __restarted__ using the same commands as above.


- Provoke an __exception__ in the __FlightService__ participant __while executing__ a local transaction of the *BookTripSaga* with the following string as `destination country`:
    ```  
    "Provoke exception while executing"
    ```  
  The __FlightService__ throws then a *RuntimeException* while booking a flight to simulate *unexpected behaviour* of the system.
  Afterwards, the behaviour of the service can be observed. The easiest way is to have a look at the log of the __FlightService__ during that time.
  This can be done using the following command:
  ```  
  docker logs flightservice_conductorFailurePerf --follow
   ```  

### 2. Saga Orchestrator failure
The __Conductor server__ is the orchestrator in this example application. Consequently, observing the system's behaviour during
orchestrator failures involves failures of this service.

- Provoke a failure of the __Conductor server__ while a trip booking __is being started__ with the following string as `destination country`:
    ```  
    "Provoke orchestrator failure while starting trip booking"
    ```
  The __TravelService__ terminates then the docker container of the __Conductor server__ while it is executing the *bookTrip* request but before starting the *BookTripSaga*.
  Afterwards, the __Conductor server__ has to be __restarted__ manually to investigate what happens as soon as the service is running again.
  This can be done using one of the following commands:
    ```
    docker-compose start conductor-server-ui
    
    docker start conductor-server-ui
    ```

  If the container name of the __Conductor server__ has been changed in the `docker-compose.yml` file, the
  container has to be started using this name.


- Provoke a failure of the __Conductor server while executing__ a local transaction of the *BookTripSaga* with the following string as `destination country`:
    ```  
    "Provoke orchestrator failure while executing"
    ```  
  The __FlightService__ terminates then the docker container of the __Conductor server__ after booking a flight, but before informing the orchestrator about it.
  Afterwards, the __Conductor server__, again, has to be __restarted__ using the same commands as above.
 