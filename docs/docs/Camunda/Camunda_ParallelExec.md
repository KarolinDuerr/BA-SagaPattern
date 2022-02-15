Title: Parallel Execution Evaluation
Context: Camunda

# Saga Pattern Parallel Execution Evaluation

This project includes an example implementation of the Saga pattern using [Camunda](https://github.com/camunda/camunda-bpm-platform/tree/master/spring-boot-starter).
The difference to the original [Saga Pattern Realization With Camunda](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/Camunda_Implementations/Camunda)
is the **parallel execution** of specific tasks instead of a sequential one.

## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up --no-start`


3. Execute `docker-compose start mysql`


4. Execute `docker-compose start travelservice`


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

Additionally, the __Camunda Cockpit__ can be accessed via
[http://localhost:8090/](http://localhost:8090/) with the credentials:<br> `Username: admin | Password: admin`

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

----------------------------

## Parallel Execution of Tasks

The `bookTrip`, `bookHotel` and the `bookFlight` tasks, as well as the `confirmHotel` and the `confirmTrip` tasks, are now executed in parallel
by using _parallel gateways_. Since the Camunda process engine is responsible for the invocation of the compensating transactions,
this execution cannot be influenced.

-------------------------------
## Code Link

[:octicons-mark-github-16: Camunda_Implementations/Camunda_ParallelExec-Evaluation](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/Camunda_Implementations/Camunda_ParallelExec-Evaluation)
