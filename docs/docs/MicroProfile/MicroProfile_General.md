Title: Travel Application Realization
Context: MicroProfile

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

    <table>
        <tr>
            <th style="text-align:center">Service</th>
            <th style="text-align:center">URL to Swagger UI</th>
        </tr>
        <tr>
            <td>TravelService</td>
            <td rowspan="2" align="center" vertical-align: "middle"><a href="http://localhost:8090/openapi/ui/">http://localhost:8090/openapi/ui/</a></td>
        </tr>
        <tr>
            <td>LRA Coordinator (included in TravelService)</td>
        </tr>
        <tr>
            <td>HotelService</td>
            <td align="center"><a href="http://localhost:8081/openapi/ui/">http://localhost:8081/openapi/ui/</a></td>
        </tr>
        <tr>
            <td>FlightService</td>
            <td align="center"><a href="http://localhost:8082/openapi/ui/">http://localhost:8082/openapi/ui/</a></td>
        </tr>
    </table>

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

The services also provide a general *health* endpoint that shows information about the system whether it is up and running.
These endpoints can be accessed via:

| <center>__Service__</center> | <center>__URL to health endpoint__</center> |
|:-------|:------------------:|
|TravelService| [http://localhost:8090/health](http://localhost:8090/health) |
|HotelService| [http://localhost:8081/health](http://localhost:8081/health) |
|FlightService| [http://localhost:8082/health](http://localhost:8082/health) |


If you are on Windows or Mac, you sometimes have to replace _localhost_ with the default IP of your docker machine (use `docker-machine ip default` to get this default IP).

## Stop the Application

To stop the application and remove the created containers, execute the following command:
```shell
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

### Log Files
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


### Tracing
The services include the necessary gradle dependencies as well as the feature within the `server.xml` to enable
distributed tracing with [Zipkin](https://zipkin.io/) or with [Jaeger](https://www.jaegertracing.io/). If tracing with
Jaeger is enabled, Zipkin will not work. Information on how to activate/deactivate the respective tracing technology is
described in the following two subsections. The default is that Zipkin is activated and Jaeger deactivated. Here, the main
difference between Zipkin and Jaeger is that only Zipkin also shows the traces for requests to the LRA Coordinator.

#### a) Zipkin
The [Zipkin UI](http://localhost:9411/zipkin/) can be accessed via [http://localhost:9411/zipkin/](http://localhost:9411/zipkin/)

The following shortly describes how to activate and deactivate tracing with Zipkin:

- [x] __Activate Zipkin [default]__: Go to the respective `server.xml` and uncomment the following line within the __featureManager__
  section
  ```xml
      <feature>usr:opentracingZipkin-0.33</feature>
  ```

    The following line must also be uncommented within the `server.xml`:
  ```xml
      <opentracingZipkin host="${zipkin.host}" port="9411"/>
  ```

    Additionally, the following line should be active within the respective `build.gradle`:
  ```groovy
      compile "WASdev:sample.opentracing.zipkintracer:2.0.1@zip"
  ```

    Additionally, make sure that the Zipkin service is active within the `docker-compose.yml` and that the following
    environment variable is set for each microservice:
  ```yaml
      ZIPKIN_URI: zipkin
  ```

- [ ] __Deactivate Zipkin__: Go to the respective `server.xml` and comment the following line out within the __featureManager__
  section
  ```xml
      <feature>usr:opentracingZipkin-0.33</feature>
  ```

    Additionally, the following line must also be commented out within the `server.xml`:
  ```xml
      <opentracingZipkin host="${zipkin.host}" port="9411"/>
  ```

#### b) Jaeger
The [Jaeger UI](http://localhost:16686/) can be accessed via [http://localhost:16686/](http://localhost:16686/)

The following shortly describes how to activate and deactivate tracing with Jaeger:

- [ ] __Activate Jaeger__: Go to the respective `build.gradle` files and activate within the dependencies section the
  following line:
  ```groovy
    compile "io.jaegertracing:jaeger-client:1.6.0"
  ```
  Additionally, make sure that the Jaeger service is active within the `docker-compose.yml` and that the following four
  environment variables are set and active for each microservice:
  ```yaml
        JAEGER_AGENT_HOST: jaeger
        JAEGER_AGENT_PORT: 6831
        JAEGER_SAMPLER_TYPE: const
        JAEGER_SAMPLER_PARAM: 1
  ```

- [X] __Deactivate Jaeger [default]__: Go to the respective `build.gradle` files and comment out, within the dependencies section,
  the following line:
  ```groovy
    compile "io.jaegertracing:jaeger-client:1.6.0"
  ```


### Metrics

The individual microservices are publishing some metrics like the number of executed requests to a specific endpoint.
Additionally, the LRA Coordinator publishes some metrics like the maximum duration time of closing an LRA.
These metrics are also published by the TravelService since the TravelService includes the coordinator.
<table>
    <tr>
        <th style="text-align:center">Service</th>
        <th style="text-align:center">URL to Swagger UI</th>
    </tr>
    <tr>
        <td>TravelService</td>
        <td rowspan="2" align="center"><a href="http://localhost:8090/metrics//">http://localhost:8090/metrics/</a></td>
    </tr>
    <tr>
        <td>LRA Coordinator (included in TravelService)</td>
    </tr>
    <tr>
        <td>HotelService</td>
        <td align="center"><a href="http://localhost:8081/metrics//">http://localhost:8081/metrics/</a></td>
    </tr>
    <tr>
        <td>FlightService</td>
        <td align="center"><a href="http://localhost:8082/metrics//">http://localhost:8082/metrics/</a></td>
    </tr>
</table>

---------------------
## Code Link

[:octicons-mark-github-16: MicroProfile_Implementations/MicroProfile](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/MicroProfile_Implementations/MicroProfile)
