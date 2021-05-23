# BA-Saga Pattern
The implementation for the paper "*An Evaluation of Saga Pattern Implementation Technologies*" of the 13th ZEUS Workshop can be found
in the following release [An Evaluation of Saga Pattern Implementation Technologies](https://github.com/KarolinDuerr/BA-SagaPattern/releases/tag/v1.0).

-----------------------------------------------------------------------------

This repository includes four implementations that realize an example travel application using orchestrated Sagas.
- using the [Eventuate Tram](https://github.com/eventuate-tram/eventuate-tram-core) and the [Eventuate Tram Sagas](https://github.com/eventuate-tram/eventuate-tram-sagas) framework
- using [Netflix Conductor](https://github.com/Netflix/conductor)
- using [Camunda](https://github.com/camunda/camunda-bpm-platform/tree/master/spring-boot-starter)

The example travel application consists of three backend services: TravelService,
HotelService and FlightService. For simplicity reasons, only the workflow for booking a trip has been implemented.


## Saga Pattern Realization

The `EventuateTram` directory includes the travel application that has been realized using Eventuate Tram, and the `Netflix Conductor` directory the one that has been realized using Netflix Conductor.
`Camunda` contains the realization using Camunda.

Additionally, a short description of the different possibilities to   __monitor__ the applications is included in all three directories in the respective `Readme` file.


## Saga Pattern Realization: Expandability Evaluation

Based on the `EventuateTram` implementation, the `EventuateTram_Expandability-Evaluation` directory includes the same travel application but extended by a __CustomerService__ to examine how easily a new service can be included.


The `NetflixConductor_Expandability-Evaluation` directory includes the same application as the `NetflixConductor` directory but also extended by a __CustomerService__.
The same applies for the `Camunda_ExpandabilityEvaluation` directory.


## Saga Pattern Realization: Failure Performance Evaluation

The `EventuateTram_FailurePerf-Evaluation` is also based on the `EventuateTram` application but includes additional sections that provoke different failure scenarios given a certain input.

The same applies to the `NetflixConductor_FailurePerf-Evaluation` directory, but the implementation is based on the `NetflixConductor` application.
The directory `Camunda_FailurePerf-Evaluation` rests on the same concept.

-----------------------------------------------------------------------------
For more information about the projects and their setups see the `Readme` files in the respective directories.
