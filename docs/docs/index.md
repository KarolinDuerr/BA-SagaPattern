Title: Home
Date: 2021
# BA-Saga Pattern

The implementation for the paper "*An Evaluation of Saga Pattern Implementation Technologies*" of the 13^th^ ZEUS Workshop can be found
in the following release [:octicons-tag-24: An Evaluation of Saga Pattern Implementation Technologies](https://github.com/KarolinDuerr/BA-SagaPattern/releases/tag/v1.0).

-----------------------------------------------------------------------------

This repository includes four implementations that realize an example travel application using orchestrated Sagas.
Implementation

- using the [Eventuate Tram](https://github.com/eventuate-tram/eventuate-tram-core) and the [Eventuate Tram Sagas](https://github.com/eventuate-tram/eventuate-tram-sagas) framework
- using [Netflix Conductor](https://github.com/Netflix/conductor)
- using [Camunda](https://github.com/camunda/camunda-bpm-platform/tree/master/spring-boot-starter)
- using [MicroProfile LRA](https://github.com/eclipse/microprofile-lra)

The example travel application consists of three backend services: TravelService,
HotelService and FlightService. For simplicity reasons, only the workflow for booking a trip has been implemented.


## Saga Pattern Realization

Each directory contains several realizations using the respective technology: A basic implementation(*) for the mentioned trip booking scenario and up to four further implementations that extend the basic one:

| <center>__Directory__</center> | <center>__Short Description__</center> |
   |:----------------------------|:-------------------|
| <center>*</center> | The travel application contains the workflow for booking a trip.|
|||
| *_Expandability-Evaluation | A _CustomerService_ extends the travel application to examine how easily a new service can be included.|
| *_FailurePerf-Evaluation |  The application includes additional sections that provoke different failure scenarios given a certain input.|
| *_ParallelExec-Evaluation | If possible, some transactions within the BookTripSaga are executed in parallel. The same applies for some compensating transactions.|
| *_Testability-Evaluation | The project includes automatic tests for some Saga related parts of the implementation.|

For more information about the projects and their setups see the respective sections in this documentation:

- [X] [Camunda Implementation](Camunda/Camunda_General.md)
- [X] [Eventuate Tram Implementation](EventuateTram/EventuateTram_General.md)
- [X] [MicroProfile LRA Implementation](MicroProfile/MicroProfile_General.md)
- [X] [Netflic Conductor Implementation](NetflixConductor/NetflixConductor_General.md)

-----------------------------------------------------------------------------

## Evaluation Criteria Catalog

This section includes a detailed description of the criteria catalog that was used for the evaluation of the individual technologies and can be found here: [Criteria Catalog](CriteriaCatalog/index.md).

## Evaluation Summary

A summarization of the evaluation results for the individual technologies can be found here: [Evaluation Summary](EvaluationSummary/index.md).
