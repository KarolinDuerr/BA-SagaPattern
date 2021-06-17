# MicroProfile LRA Expandability Evaluation
This project is part of the evaluation of a Saga pattern implementation using [MicroProfile LRA](https://github.com/eclipse/microprofile-lra).
The original [Saga Pattern Realization with MicroProfile LRA](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/MicroProfile_Implementations/MicroProfile)
has been extended by a __CustomerService__ in order to evaluate the expandability of an implementation using Eventuate Tram
to realize the Saga pattern. The __CustomerService__ has to authorize the customer before a hotel is being booked.


## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up `


3. Requesting trip bookings is now possible. Either use `curl` commands,
   the provided `TravelApplication.json` insomnia file, which includes different trip booking requests,
   or access the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the different services:

   | __Service__ | __URL to Swagger UI__ |
   |:-------|:-------------------:| 
   |TravelService| http://localhost:8090/openapi/ui/ // TODO --> currently shows LRA Coordinator API
   |HotelService| http://localhost:8081/openapi/ui/
   |FlightService| http://localhost:8082/openapi/ui/
   |CustomerService| http://localhost:8084/openapi/ui/
   |LRA Coordinator| http://localhost:8080/openapi/ui/ // TODO doesn't work anymore --> included in TravelService

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

To simulate a Saga that fails because __no hotel__ or __no flight__ is __available__, use one of the following Strings
as `destination country` in the trip booking request:
```
"Provoke hotel failure"

"Provoke flight failure"
```

To simulate a Saga that fails because the __customer validation failed__, the `customerId` in the trip booking request
has to be __smaller than 1__, for example:
```
{
    ...
    customerId: "-1"
}
```


The services also provide a *health* and an *info* endpoint that show some information about the system like
that the DB is up and running. These endpoints can be accessed via:

| __Service__ | __URL to health endpoint__ |
|:-------:|------------------|
|TravelService| http://localhost:8090/health
|HotelService| http://localhost:8081/health
|FlightService| http://localhost:8082/health
|CustomerService| http://localhost:8084/health

If you are on Windows or Mac, you sometimes have to replace _localhost_ with the default IP of your docker machine (use `docker-machine ip default` to get this default IP).

## Stop the Application

To stop the application and remove the created containers, execute the following command:
```
docker-compose down --remove-orphans
```