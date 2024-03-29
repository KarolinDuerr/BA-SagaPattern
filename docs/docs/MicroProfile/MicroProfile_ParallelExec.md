Title: Parallel Execution Evaluation
Context: MicroProfile

# MicroProfile LRA Parallel Execution Evaluation

This project includes an example implementation of the Saga pattern
using [MicroProfile LRA](https://github.com/eclipse/microprofile-lra). The difference to the
original [Saga Pattern Realization with MicroProfile LRA](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/MicroProfile_Implementations/MicroProfile)
is the **parallel execution** of specific tasks instead of a sequential one.

## Start the Application

1. Run `./gradlew clean build`


2. Execute `docker-compose up `


3. Requesting trip bookings is now possible. Either use `curl` commands, the provided `TravelApplication.json` insomnia
   file, which includes different trip booking requests, or access
   the [Swagger UI](https://swagger.io/tools/swagger-ui/) of the different services:

    <table>
        <tr>
            <th style="text-align:center">Service</th>
            <th style="text-align:center">URL to Swagger UI</th>
        </tr>
        <tr>
            <td>TravelService</td>
            <td rowspan="2" align="center"><a href="http://localhost:8090/openapi/ui/">http://localhost:8090/openapi/ui/</a></td>
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

The services also provide a general *health* endpoint that shows information about the system whether it is up and
running. These endpoints can be accessed via:

| <center>__Service__</center> | <center>__URL to health endpoint__</center> |
|:-------|:------------------:|
|TravelService| [http://localhost:8090/health](http://localhost:8090/health) |
|HotelService| [http://localhost:8081/health](http://localhost:8081/health) |
|FlightService| [http://localhost:8082/health](http://localhost:8082/health) |

If you are on Windows or Mac, you sometimes have to replace _localhost_ with the default IP of your docker machine (
use `docker-machine ip default` to get this default IP).

## Stop the Application

To stop the application and remove the created containers, execute the following command:

```shell
docker-compose down --remove-orphans
```

----------------------------

## Parallel Execution of Tasks

The `bookHotel` and the `bookFlight` requests, as well as the `confirmHotel` and the `confirmTrip` requests, are now
executed in parallel by asynchronously invoking the endpoints. However, the `confirmTrip` request is responsible for
ending the LRA. Therefore, it can only be executed in parallel with requests that will always succeed. Additionally, the
LRA Coordinator invokes the compensations, so this execution cannot be influenced.

---------------------
## Code Link

[:octicons-mark-github-16: MicroProfile_Implementations/MicroProfile_ParallelExec-Evaluation](https://github.com/KarolinDuerr/BA-SagaPattern/tree/master/MicroProfile_Implementations/MicroProfile_ParallelExec-Evaluation)
