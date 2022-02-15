Title: Monitoring Evaluation
Date: 2021
Context: Evaluation Summary
# Evaluation Summary: Monitoring

The following table shows the results for the criteria defined in [Criteria Catalog: Monitoring](../CriteriaCatalog/Monitoring.md).

-----------------------------------------------------------

| <center> __Criterion__</center> | <center>__Eventuate Tram__</center> | <center>__Netflix Conductor__</center> | <center>__Camunda__</center> | <center>__MicroProfile LRA__</center> |
| :--------| :-------------:| :----------------:| :------:| :---------------:|
| Runtime state of Sagas | via database | UI visualization | UI visualization, database | partly via coordinator API |
| Orchestrator metrics | from CDC service | from Conductor server | from embedded Process Engine | from embedded Coordinator, TravelService |
| Tracing | Zipkin integration | not directly supported | not directly supported | Zipkin & Jaeger integration |
| Logging | microservices logs | Conductor server logs | microservices logs |  microservices logs |
