# BA-Saga Pattern
The implementation for the paper "*An Evaluation of Saga Pattern Implementation Technologies*" of the 13th ZEUS Workshop can be found
in the following release [An Evaluation of Saga Pattern Implementation Technologies](https://github.com/KarolinDuerr/BA-SagaPattern/releases/tag/v1.0).

-----------------------------------------------------------------------------

This repository includes four implementations that realize an example travel application using orchestrated Sagas.
Implementation
- using the [Eventuate Tram](https://github.com/eventuate-tram/eventuate-tram-core) and the [Eventuate Tram Sagas](https://github.com/eventuate-tram/eventuate-tram-sagas) framework
- using [Netflix Conductor](https://github.com/Netflix/conductor)
- using [Camunda](https://github.com/camunda/camunda-bpm-platform/tree/master/spring-boot-starter)
- using [MicroProfile LRA](https://github.com/eclipse/microprofile-lra)

The example travel application consists of three backend services: TravelService,
HotelService and FlightService. For simplicity reasons, in most projects only the workflow for booking a trip has been implemented.


## Saga Pattern Realization

Each directory contains several realizations using the respective technology: A basic implementation(*) for the mentioned trip booking scenario and three further implementations that extend the basic one:
- __*:__ the travel application contains the workflow for booking a trip.
- __*_Expandability-Evaluation:__ a _CustomerService_ extends the travel application to examine how easily a new service can be included.
- __*_FailurePerf-Evaluation:__  the application includes additional sections that provoke different failure scenarios given a certain input.
- __*_InterleavingSagas__: the travel application also offers the possibility to cancel trips. Therefore, the _CancelBookTripSaga_ is also implemented.

-----------------------------------------------------------------------------
For more information about the projects and their setups see the `Readme` files in the respective directories.
