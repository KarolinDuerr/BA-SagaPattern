# Camunda Testability Evaluation

This project includes an example implementation of the Saga pattern using [Camunda](https://github.com/camunda/camunda-bpm-platform/tree/master/spring-boot-starter).
The original [Saga Pattern Realization With Camunda](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/Camunda_Implementations/Camunda)
has been extended by automated tests that relate to the BookTripSaga.

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
```text
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
```shell
docker-compose down --remove-orphans
```

----------------------------

## Testability
The __TravelService__ as well as the __HotelService__ have a testing directory that includes several tests concerning
Saga-related code.

### 1. Unit test Saga definition
Unit tests regarding the Saga orchestrator and the Saga definition can be found within the __TravelService__.
Since Camunda provides a __testing framework__ specifically designed for testing the BPMN process, it is used here.

### 2. Unit test Saga participant
Examples for unit testing the Saga participant can be found within the __HotelService__.
Unit tests for a Saga participant are realized using [JUnit](https://junit.org/junit4/) and [Mockito](https://site.mockito.org/). 