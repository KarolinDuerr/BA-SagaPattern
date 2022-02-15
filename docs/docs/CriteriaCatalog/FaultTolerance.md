Title: Fault Tolerance
Date: 2021
Context: Criteria Catalog

# :material-book-open-page-variant: Evaluation Criteria Catalog

In order to evaluate different technological approaches concerning the Saga pattern, some criteria have to be defined against which the evaluation can be performed.
The following sections consider several areas of interest related to characteristics of microservices and the Saga pattern as well as some quality attributes of the ISO/IEC 25010 Quality Models[^1].
For each area, the aspects that an evaluator should examine are explained and described.
Some of the described criteria have been taken from the previous paper published by Dürr et al. [\[3\]](#3).
The aim is to create a criteria catalog that can be used to analyze other technologies in this context.

-----------------------------------------------------------------------------

## ^^Fault Tolerance^^

Since the MSA represents a distributed system, failures have to be expected and the different services have to tolerate them.
Therefore, the following criteria concentrate on determining the behaviour of a technology when failures of either the orchestrator or the participant occur.
For this purpose, corresponding error scenarios should be simulated with the technology to be evaluated.
More information on how such error scenarios were simulated with the different technologies can be found within the respective fault tolerance implementations.

#### Execution timeouts
The definition of execution timeouts for individual transactions or even for the entire Saga can be a desirable feature to prevent the possibility of infinite waiting.
Then again, enforced timeouts are problematic when timeouts should not abort Sagas.
This criterion relates to _"Enforced execution timeouts"_ in [\[3, p. 79 ff.\]](#3).

#### Reaction to participant fault
An examination of the behaviour of the orchestrator and the participant if a failure arises within a participant.
Several failure scenarios are possible: the participant fails before receiving a message or while executing a local transaction.
In addition, a failure during execution can either be a sudden failure like the service crashes or unexpected behaviour such as throwing an exception.
These failures happen before the service could return its answer to the orchestrator.
The criterion is based upon the _"Retry of failing participant without restart"_ criterion in [\[3, p. 79 ff.\]](#3).

#### Saga continuation trigger after orchestrator crash
Since not only a participant can fail, faults have to be also taken into account for the orchestrator.
An orchestrator should be able to continue a Saga where it left off, even if it crashed during its execution.
Particularly interesting here is the trigger of such a continuation after the orchestrator crashed and becomes available again.
This criterion is related to the _"Auto–continuation after orchestrator crash"_ criterion in [\[3, p. 79 ff.\]](#3).

#### New Sagas while orchestrator unavailable
Depending on who orchestrates the Saga execution and how the communication between the components is realized, it might be possible to start new Sagas even though the orchestrator is temporarily unavailable.
This criterion was also part of Dürr et al.’s evaluation [\[3, p. 79 ff.\]](#3).

#### Independent compensating transactions
This criterion is related to the [_Compensation transaction allocation criterion_](#compensation-transaction-allocation) in the [_General Saga Characteristics_](#1-general-saga-characteristics).
If compensating transactions can only be defined for the whole Saga, the compensation process needs all services listed in this process available to finish.
Whereas, when only needed compensations are executed, only the services that already participated in the Saga have to be available during compensation.
This criterion can also be found in [\[3, p. 79 ff.\]](#3).

#### Orchestrator reaction to duplicate messages
Another interesting aspect is how the technology reacts to non-compliance with the protocol.
Therefore, this criterion aims to observe what happens when a participant sends duplicate messages to the orchestrator.
This can be provoked by causing a participant to send a message twice in immediate succession.

#### Orchestrator reaction to old messages
Following on from the previous criterion, this time, old messages are resent to the orchestrator.
A participant can simulate this by starting a new thread that resends the same message it is about to send to the orchestrator after some time, for example after five minutes.

#### High availability
This criterion considers if the technology allows achieving highly available systems, for example, by replicating the orchestrating engine.
Analyzing _High availability_ was also part of the evaluation done in [\[3, p. 79 ff.\]](#3).

-----------------------------------------------------------------------

## References

<a name="1" href="https://dx.doi.org/10.1109/SOCA.2016.15">[1] N. Alshuqayran, N. Ali, and R. Evans, "A Systematic Mapping Study in Microservice Architecture." IEEE Computer Society, 2016, pp. 44–51. [Online]. Available: https://dx.doi.org/10.1109/SOCA.2016.15</a>

<a name="2" href="https://www.oreilly.com/library/view/building-microservices/9781491950340/">[2] S. Newman, Building Microservices – Designing Fine–Grained Systems, 1st ed. O’Reilly Media, Inc., 2015, ISBN: 9781491950357.</a>

<a name="3" href="http://ceur-ws.org/Vol-2839/paper12.pdf">[3] K. Dürr, R. Lichtenthäler, and G. Wirtz, "An Evaluation of Saga Pattern Implementation Technologies," in Proceedings of the 13th European Workshop on Services and their Composition (ZEUS 2021), Bamberg, Germany, February 25–26, 2021, ser. CEUR Workshop Proceedings, vol. 2839. CEUR-WS.org, 2021, pp. 74–82. [Online]. Available: http://ceur-ws.org/Vol-2839/paper12.pdf</a>

<a name="4" href="https://doi.org/10.1002/spip.257">[4] D. Cruz, T. Wieland, and A. Ziegler, "Evaluation Criteria for Free/Open Source Software Products Based on Project Analysis," Software Process: Improvement and Practice, vol. 11, no. 2, pp. 107–122, 2006. [Online]. Available: https://doi.org/10.1002/spip.257</a>

<a name="5" href="https://doi.org/10.4018/jsita.2010101505">[5] J. P. Confino and P. A. Laplante, "An Open Source Software Evaluation Model," Int. J. Strateg. Inf. Technol. Appl., vol. 1, no. 1, pp. 60–77, 2010. [Online]. Available: https://doi.org/10.4018/jsita.2010101505</a>

<a name="6" href="https://dx.doi.org/10.1145/3183628.3183631">[6] T. Cerny, M. J. Donahoo, and M. Trnka, "Contextual Understanding of Microservice Architecture: Current and Future Directions," ACM SIGAPP Applied Computing Review, vol. 17, no. 4, pp. 29–45, 2018. [Online]. Available: https://dx.doi.org/10.1145/3183628.3183631</a>

<a name="7" href="https://dx.doi.org/10.1007/s00450-016-0337-0">[7] O. Zimmermann, "Microservices Tenets," Computer Science – Research and Development, vol. 32, no. 3–4, pp. 301–310, 2016. [Online]. Available: https://dx.doi.org/10.1007/s00450-016-0337-0</a>

-----------------------------------------------------------------------

[^1]: [https://iso25000.com/index.php/en/iso-25000-standards/iso-25010?start=0](https://iso25000.com/index.php/en/iso-25000-standards/iso-25010?start=0), last
accessed 2021-07-06

--8<-- "includes/abbreviations.md"
