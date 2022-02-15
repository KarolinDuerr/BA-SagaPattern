Title: Security Evaluation
Date: 2021
Context: Evaluation Summary

# Evaluation Summary: Security

The following table shows the results for the criteria defined in [Criteria Catalog: Security](../CriteriaCatalog/Security.md).
These aspects have not been implemented by the prototype application and are based on the respective technology documenentations.

-------------------------------------------

| <center> __Criterion__</center> | <center>__Eventuate Tram__</center> | <center>__Netflix Conductor__</center> | <center>__Camunda__</center> | <center>__MicroProfile LRA__</center> |
| :--------| :-------------:| :----------------:| :------:| :---------------:|
| Encrypted communication[^1]</span></a> | &#10004; | &#10004; | &#10004;</a> | &#10004;</a> |
| Authentication support[^1] | :material-close: | :material-close:  | &#10004; | MicroProfile JWT[^2] |
| Authorization support[^1]| :material-close:  | :material-close:  | &#10004;| MicroProfile JWT[^2] |

[^1]: Not implemented by the prototype
[^2]: [https://download.eclipse.org/microprofile/microprofile-jwt-auth-1.2/microprofile-jwt-auth-spec-1.2.html](https://download.eclipse.org/microprofile/microprofile-jwt-auth-1.2/microprofile-jwt-auth-spec-1.2.html), last accessed: 2021-07-11
