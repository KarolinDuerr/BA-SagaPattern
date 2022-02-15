Title: General Saga Characteristics Summary
Date: 2021
Context: Evaluation Summary
# Evaluation Summary: General Saga Characteristics
The following table shows the results for the criteria defined in [Criteria Catalog: General Saga Characteristics](../CriteriaCatalog/GeneralSagaCharacteristics.md).

------------------------------------------------------

| <center> __Criterion__</center> | <center>__Eventuate Tram__</center> | <center>__Netflix Conductor__</center> | <center>__Camunda__</center> | <center>__MicroProfile LRA__</center> |
| :--------| :-------------:| :----------------:| :------:| :---------------:|
| Saga definition | Eventuate DSL | JSON / provided clients| Modeler / XML / Java DSL | Annotations |
| Orchestrating services | CDC Service, TravelService | Conductor server | Camunda Engine in TravelService | MicroProfile Coordinator, TravelService |
| Specifying compensating transactions | &#10004; | &#10004; | &#10004; | &#10004; |
| Compensation transaction allocation | specific transaction | entire workflow | specific transaction | resource class |
| Parallel transaction execution | :material-close: | &#10004; | &#10004; | developer's responsibility |
| Parallel execution configurable | :material-close: | &#10004; | &#10004; | developer's responsibility |
| Participant communication selectable | :material-close: | &#10004; | &#10004; | :material-close: |
| External compensation trigger | not directly | via API | via API | via API |
| Choreographed Sagas | &#10004; | :material-close: | :material-close:| :material-close: |
