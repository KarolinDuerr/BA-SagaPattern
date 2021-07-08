# Saga Pattern Realization With MicroProfile LRA
This project includes an example implementation of the Saga pattern using [MicroProfile LRA](https://github.com/eclipse/microprofile-lra).
The example application represents a travel application that consists of three backend services: TravelService,
HotelService and FlightService. For simplicity reasons, only the workflow for booking a trip has been implemented.

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

## General Saga Characteristics

### External Compensation Trigger

In order to start the compensation of a currently running Saga externally, the LRA coordinator offers an endpoint to 
trigger the compensation of a specific LRA. To achieve that, an empty PUT request has to be sent to the LRA coordinator 
at: `http://localhost:8090/lrac/lra-coordinator/{lraId}/cancel`


The `TravelApplication.json` insomnia file also includes this request within the `ExternalCompensationTrigger` directory.

----------------------------

## Monitor the Application
// TODO

### Log Files // TODO
Each service provides a log that contains some information about it.
The logs can be accessed using the name of the relevant container.
The different logs can be accessed using the following commands:

| __Log of__ | __Command to execute__ |
|:-------|:-------------------|
|TravelService| `docker logs travelservice_microProfile`|
|HotelService| `docker logs hotelservice_microProfile`|
|FlightService|  `docker logs flightservice_microProfile`|

By using the `--follow` supplement, it will be continued to stream the service's output to the console.

The logging level can be changed in the respective `application.properties` file.

### Zipkin // TODO
The services include the necessary gradle dependencies and feature activation to enable distributed tracing with 
[Zipkin](https://zipkin.io/).

The Zipkin UI can be accessed via http://localhost:9411/zipkin/

### Jaeger // TODO
The services include the necessary gradle dependencies and feature activation to enable distributed tracing with 
[Jaeger](https://www.jaegertracing.io/). --> maybe deactivate zipkin, uncomment Jaeger integration in build gradle
However, Jaeger does not show the traces to the LRA Coordinator.

The Zipkin UI can be accessed via http://localhost:16686/