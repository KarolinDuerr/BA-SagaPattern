# BA-Saga Pattern: Eventuate Tram
This directory contains four different realizations concerning the Saga Pattern using Camunda.


## Saga Pattern Realization

The `EventuateTram` directory includes the travel application that has been realized using Eventuate Tram.

Additionally, a short description of the different possibilities to   __monitor__ the applications is included in the respective `Readme` file.


## Saga Pattern Realization: Expandability Evaluation

Based on the `EventuateTram` implementation, the `EventuateTram-Evaluation` directory includes the same travel application but extended by a __CustomerService__ to examine how easily a new service can be included.



## Saga Pattern Realization: Failure Performance Evaluation

The `EventuateTram_FailurePerf-Evaluation` is also based on the `EventuateTram` application but includes additional sections that provoke different failure scenarios given a certain input.


## Saga Pattern Realization: Testability Evaluation

Based on the `EventuateTram` implementation, the `EventuateTram_Testability-Evaluation` directory includes the same travel application but extended by some automatic tests for Saga related parts of the implementation.

-----------------------------------------------------------------------------
For more information about the projects and their setups see the `Readme` files in the respective directories.
