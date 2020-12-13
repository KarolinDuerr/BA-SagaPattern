# BA-Saga Pattern
This repository includes two implementations that realize an example travel application using orchestrated Sagas.
One is realized using the [Eventuate Tram](https://github.com/eventuate-tram/eventuate-tram-core) and the [Eventuate Tram Sagas](https://github.com/eventuate-tram/eventuate-tram-sagas) framework, the other using [Netflix Conductor](https://github.com/Netflix/conductor).
The example travel application consists of three backend services: TravelService,
HotelService and FlightService. For simplicity reasons, only the workflow for booking a trip has been implemented.


## Saga Pattern Realization

The `EventuateTram` directory includes the travel application that has been realized using Eventuate Tram, and the `Netflix Conductor` directory the one that has been realized using Netflix Conductor.

Additionally, a short description of the different possibilities to   __monitor__ the applications is included in both directories in the respective `Readme` file.


## Saga Pattern Realization: Expandability Evaluation

Based on the `EventuateTram` implementation, the `EventuateTram_Expandability-Evaluation` directory includes the same travel application but extended by a __CustomerService__ to examine how easily a new service can be included.


The `NetflixConductor_Expandability-Evaluation` directory includes the same application as the `NetflixConductor` directory but also extended by a __CustomerService__.


## Saga Pattern Realization: Failure Performance Evaluation

The `EventuateTram_FailurePerf-Evaluation` is also based on the `EventuateTram` application but includes additional sections that provoke different failure scenarios given a certain input.

The same applies to the `NetflixConductor_FailurePerf-Evaluation` directory, but the implementation is based on the `NetflixConductor` application.

-----------------------------------------------------------------------------
For more information about the projects and their setups see the `Readme` files in the respective directories.
