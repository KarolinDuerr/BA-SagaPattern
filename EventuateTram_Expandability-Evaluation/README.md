# BA-SagaPattern: Eventuate Tram Expandability Evaluation
This project is part of the evaluation of a Saga pattern implementation using the Eventuate Tram framework.
The original [Eventuate Saga pattern implementation](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/EventuateTram)
has been extended by a __CustomerService__ in order to evaluate the expandability of an implementation using Eventuate Tram
to realize the Saga pattern. The __CustomerService__ has to authorize the customer before a hotel is being booked.


## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up `


3. Requesting trip bookings is now possible. Either use `curl` commands,
   the provided `TravelApplication.json` insomnia file, which includes different trip booking requests,
   or access the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the different services:
- TravelService: http://localhost:8090/swagger-ui.html
- HotelService: http://localhost:8081/swagger-ui.html
- FlightService: http://localhost:8082/swagger-ui.html
- CustomerService: http://localhost:8083/swagger-ui.html

To simulate a Saga that fails because __no hotel__ or __no flight__ is __available__, use one of the following Strings
as `destination country` in the trip booking request:
```
"Provoke hotel failure"

"Provoke flight failure"
```

To simulate a Saga that fails because the __customer validation failed__, the `customerId` in the trip bookig request
has to be __smaller than 1__, for example:
```
{
    ...
    customerId: "-1"
}
```

Additionally, the [Zipkin](https://zipkin.io/) UI can be accessed to trace performed calls:
http://localhost:9411/zipkin/

The services also provide a *health* and an *info* endpoint that show some information about the system like
that the DB is up and running. These endpoints can be accessed via:
- TravelService:

  http://localhost:8090/api/travel/monitor/health

  http://localhost:8090/api/travel/monitor/info


- HotelService:  
  http://localhost:8081/api/hotels/monitor/health

  http://localhost:8081/api/hotels/monitor/info


- FlightService:

  http://localhost:8082/api/flights/monitor/health

  http://localhost:8082/api/flights/monitor/info


- CustomerService:
  
  http://localhost:8083/api/customers/monitor/health
  
  http://localhost:8083/api/customers/monitor/info

If you are on Windows or Mac, you sometimes have to replace _localhost_ with the default IP of your docker machine (use `docker-machine ip default` to get this default IP).

## Stop the Application

To stop the application and remove the created containers execute the following command:
```
docker-compose down --remove-orphans
```