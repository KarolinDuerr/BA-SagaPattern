Title: Testability Evaluation
Context: Netflix Conductor

# Netflix Conductor Testability Evaluation
This project includes an example implementation of the Saga pattern using [Netflix Conductor](https://github.com/Netflix/conductor).
The original [Saga Pattern Realization With Netflix Conductor](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/NetflixConductor_Implementations/NetflixConductor)
has been extended by automated tests that relate to the BookTripSaga.

## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up --no-start`


3. Execute `docker-compose start elasticsearch`


4. Execute `docker-compose start dynomite`


5. Execute `docker-compose up`


6. Requesting trip bookings is now possible. Either use `curl` commands,
   the provided `TravelApplication.json` insomnia file, which includes different trip booking requests,
   or access the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the different services:

   | <center>__Service__</center> | <center>__URL to Swagger UI__</center> |
   |:-------|:-------------------:|
   |TravelService| [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html)
   |HotelService| [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
   |FlightService| [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

An example for such a request:
```json title="TravelRequest"
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

Additionally, the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the __Conductor server__ can be accessed via
[http://localhost:8080/](http://localhost:8080/).

The services also provide a *health* and an *info* endpoint that show some information about the system like
that the DB is up and running. These endpoints can be accessed via:

| <center>__Service__</center> | <center>__URL to health endpoint__</center> |  <center>__URL to info endpoint__</center> |
|:-------:|------------------|-------------------|
|TravelService| [http://localhost:8090/api/travel/monitor/health](http://localhost:8090/api/travel/monitor/health) | [http://localhost:8090/api/travel/monitor/info](http://localhost:8090/api/travel/monitor/info)
|HotelService| [http://localhost:8081/api/hotels/monitor/health](http://localhost:8081/api/hotels/monitor/health) | [http://localhost:8081/api/hotels/monitor/info](http://localhost:8081/api/hotels/monitor/info)
|FlightService| [http://localhost:8082/api/flights/monitor/health](http://localhost:8082/api/flights/monitor/health) | [http://localhost:8082/api/flights/monitor/info](http://localhost:8082/api/flights/monitor/info)


If you are on Windows or Mac, you sometimes have to replace _localhost_ with the default IP of your docker machine (use `docker-machine ip default` to get this default IP).

## Stop the Application

To stop the application and remove the created containers, execute the following command:
```shell
docker-compose down --remove-orphans
```

---------------------------

## Testability
The __TravelService__ as well as the __HotelService__ have a testing directory that includes several tests concerning
Saga-related code.

### 1. Unit test Saga Definition {#markdown data-toc-label="1. Unit test Saga definition"}
Unit tests regarding the Saga orchestrator and the Saga definition can be found within the __TravelService__.
However, Netflix Conductor does __not__ provide a __testing framework__ or allow to test the workflow definition
__in isolation__. Therefore, it is only possible to test whether the correct workflow definition is started when
executing the _BookTripSaga_.

### 2. Unit Test Saga Participant
Examples for unit testing the Saga participant can be found within the __HotelService__.
Unit tests for a Saga participant are realized using [JUnit](https://junit.org/junit4/) and [Mockito](https://site.mockito.org/).

---------------------
## Code Link

[:octicons-mark-github-16: NetflixConductor_Implementations/NetflixConductor_Testability-Evaluation](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/NetflixConductor_Implementations/NetflixConductor_Testability-Evaluation)
