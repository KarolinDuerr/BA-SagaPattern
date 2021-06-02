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

Each directory contains several realizations using the respective technology: A basic implementation(*) for the mentioned trip booking scenario and 
five further implementations that extend the basic one:

| <center>__Directory__</center> | <center>__Short Description__</center> |
   |:----------------------------|:-------------------| 
| <center>*</center> | the travel application contains the workflow for booking a trip.|
|||
| *_Expandability-Evaluation | a _CustomerService_ extends the travel application to examine how easily a new service can be included.|
| *_FailurePerf-Evaluation |  the application includes additional sections that provoke different failure scenarios given a certain input.|
| *_InterleavingSagas-Evaluation | the travel application also offers the possibility to cancel trips. Therefore, the _CancelBookTripSaga_ is also implemented.|
| *_NestedSagas-Evaluation | the _HotelService_ offers the possibility to book an event, e.g. a day trip to an amusement park additionally. This functionality is implemented with a Saga. Consequently, the BookTripSaga includes another Saga.|
| *_ParallelExec-Evaluation | If possible, some transactions within the BookTripSaga are executed in parallel. The same applies for some compensating transactions.|

-----------------------------------------------------------------------------
For more information about the projects and their setups see the `Readme` files in the respective directories.
