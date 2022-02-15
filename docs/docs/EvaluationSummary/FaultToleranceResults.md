Title: Fault Tolerance Evaluation
Date: 2021
Context: Evaluation Summary
# Evaluation Summary: Fault Tolerance

The following table shows the results for the criteria defined in [Criteria Catalog: Fault Tolerance](../CriteriaCatalog/FaultTolerance.md).

----------------------------------------------


| <center> __Criterion__</center> | <center>__Eventuate Tram__</center> | <center>__Netflix Conductor__</center> | <center>__Camunda__</center> | <center>__MicroProfile LRA__</center> |
| :--------| :-------------:| :----------------:| :------:| :---------------:|
| Execution timeouts | :material-close:| enforced | enforced | &#10004; |
| Reaction to participant fault | unsubscribe | retry | retry | developer's responsibility & compensation |
| Saga continuation trigger after orchestrator crash | new Saga instance | restart of Conductor server | restart of TravelService | :material-close: <br>(developer's responsibility) |
| New Sagas while orchestrator unavailable | &#10004; | only with buffering | :material-close: | :material-close: |
| Independent compensating transactions | &#10004; | :material-close: | &#10004; | &#10004; |
| Orchestrator reaction to duplicate messages | detect & ignore | detect & ignore | log exception & ignore | developer's responsibility, detect & ignore |
| Orchestrator reaction to old messages | detect & ignore | detect & ignore | log exception & ignore | eveloper's responsibility, detect & ignore |
| High availability | through replication | through replication | through replication | – |

[^1]: [https://openliberty.io/blog/2021/01/27/microprofile-long-running-actions-beta.html](https://openliberty.io/blog/2021/01/27/microprofile-long-running-actions-beta.html), last accessed: 2021-07-15

!!! info

    The MicroProfile LRA specification does not include explicit information on whether it is possible to achieve high availability through methods like replication. Furthermore, the chosen runtime OpenLiberty, which includes the implementation for LRA, is still in a beta state[^1]. Therefore,
    their official documentation does not include information about MicroProfile LRA yet,
    which also means no information about a highly available system with it. Consequently,
    further tests would be necessary to be able to make a statement about high availability
    with MicroProfile LRA.
