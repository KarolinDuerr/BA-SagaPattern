# BA-Saga Pattern: MicroProfile LRA
This directory contains four different realizations concerning the Saga Pattern using Camunda.


## Saga Pattern Realization

The `MicroProfile` directory includes the travel application that has been realized using Eventuate Tram.

Additionally, a short description of the different possibilities to   __monitor__ the applications is included in the respective `Readme` file.


## Saga Pattern Realization: Expandability Evaluation

Based on the `MicroProfile` implementation, the `MicroProfile_Expandability-Evaluation` directory includes the same travel application but extended by a __CustomerService__ to examine how easily a new service can be included.



## Saga Pattern Realization: Failure Performance Evaluation

The `MicroProfile_FailurePerf-Evaluation` is also based on the `MicroProfile` application but includes additional sections that provoke different failure scenarios given a certain input.


## Saga Pattern Realization: Parallel Execution Evaluation

Based on the `MicroProfile` implementation, the `MicroProfile_ParallelExec-Evaluation` directory includes the same travel application but some transactions within the BookTripSaga are executed in parallel instead of sequentially.

-----------------------------------------------------------------------------
For more information about the projects and their setups see the `Readme` files in the respective directories.
