# MicroProfile LRA Parallel Execution Evaluation
This project includes an example implementation of the Saga pattern using [MicroProfile LRA](https://github.com/eclipse/microprofile-lra).
The difference to the original [Saga Pattern Realization with MicroProfile LRA](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/MicroProfile_Implementations/MicroProfile)
is the **parallel execution** of specific tasks instead of a sequential one.

## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up `


3. Requesting trip bookings is now possible. Either use `curl` commands,
   the provided `TravelApplication.json` insomnia file, which includes different trip booking requests,
   or access the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the different services:

   | __Service__ | __URL to Swagger UI__ |
   |:-------|:-------------------:|
   |TravelService| http://localhost:8090/openapi/ui/
   |HotelService| http://localhost:8081/openapi/ui/
   |FlightService| http://localhost:8082/openapi/ui/
   |LRA Coordinator (included in TravelService) | http://localhost:8090/openapi/ui/

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
```
docker-compose down --remove-orphans
```

----------------------------

## Parallel Execution of Tasks

The `bookHotel` and the `bookFlight` tasks, as well as the `confirmHotel` and the `confirmTrip` tasks, are now executed in parallel
by using // TODO. The LRA Coordinator invokes the compensations, so this execution cannot be influenced.
