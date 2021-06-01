# Interleaving the BookTripSaga With Eventuate Tram
This project is part of the evaluation of a Saga pattern implementation using the [Eventuate Tram](https://github.com/eventuate-tram/eventuate-tram-core)
and [Eventuate Tram Sagas](https://github.com/eventuate-tram/eventuate-tram-sagas) framework.
The original [Saga Pattern Realization with Eventuate Tram](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/EventuateTram_Implementations/EventuateTram)
has been extended by implementing another workflow for cancelling a booked trip. Therefore, this project includes another Saga: the __CancelTripSaga__. 


## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up `


3. Requesting trip bookings is now possible. Either use `curl` commands,
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

## CancelTripSaga
It is possible to cancel already booked trips, as well as trips that are currently being executed.
Again curl requests, or the [Swagger UI](https://swagger.io/tools/swagger-ui/) can be used to cancel a trip.

An example for such a cancelling request:
```
{
   "tripId": 1,
   "customerId": 1,
   "provokeFailureType": "NONE"
}
```
Setting the `provokeFailureType` to the value `NONE` implies that the trip cancelling is supposed to be successful. 

To simulate a CancelTripSaga that fails because the hotel, or the flight booking cannot be cancelled anymore, 
use one of the following enum types as `provokeFailureType` in the trip cancelling request:
```
"HOTEL_FAILURE"

"FLIGHT_FAILURE"
```