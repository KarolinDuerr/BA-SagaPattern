Title: Testability Evaluation
Date: 2021
Context: Evaluation Summary
# Evaluation Summary: Testability

The following table shows the results for the criteria defined in [Criteria Catalog: Testability](../CriteriaCatalog/Testability.md).
The results regarding _Saga integration tests_ have not been implemented by the prototype application and are based on the respective technology documenentations.

---------------

| <center> __Criterion__</center> | <center>__Eventuate Tram__</center> | <center>__Netflix Conductor__</center> | <center>__Camunda__</center> | <center>__MicroProfile LRA__</center> |
| :--------| :-------------:| :----------------:| :------:| :---------------:|
| In-house test framework | &#10004; | :material-close:| &#10004; | :material-close:|
| Unit test Saga definition | &#10004; | :material-close:| &#10004; | successful Saga |
| Unit test Saga participant | &#10004; | &#10004; | &#10004; | &#10004; |
| Saga integration test[^1] | &#10004; | &#10004; | &#10004; | &#10004; |

[^1]: Not implemented by the prototype
