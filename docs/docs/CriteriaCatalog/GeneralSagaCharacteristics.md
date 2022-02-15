Title: General Saga Characteristics
Date: 2021
Context: Criteria Catalog

# :material-book-open-page-variant: Evaluation Criteria Catalog

In order to evaluate different technological approaches concerning the Saga pattern, some criteria have to be defined against which the evaluation can be performed.
The following sections consider several areas of interest related to characteristics of microservices and the Saga pattern as well as some quality attributes of the ISO/IEC 25010 Quality Models[^1].
For each area, the aspects that an evaluator should examine are explained and described.
Some of the described criteria have been taken from the previous paper published by Dürr et al. [\[3\]](#3).
The aim is to create a criteria catalog that can be used to analyze other technologies in this context.

-----------------------------------------------------------------------------

## ^^General Saga Characteristics^^

The Saga pattern includes different characteristics like the definition of compensating transactions.
Thus, the following criteria examine how the implementation technology supports realizing those characteristics or related matters.

#### Saga definition
As this paper focuses on orchestrated Sagas, the sequence of the included transactions has to be defined, for example in a JSON file or programmatically via a DSL.
Therefore, this criterion describes the different possibilities the examined technology offers to define the individual Saga process with its included steps.

#### Orchestrating services
Depending on the particular implementation, the Saga orchestrator can be an additional service that exclusively orchestrates the Saga or an existing one that also participates in it.
However, some technologies might already provide an orchestrating engine that orchestrates the actual process single–handedly.
Hence, an existing self–implemented service cannot be used as the real orchestrator.
Instead, it might have to register the Saga definition with the technology’s orchestrator but otherwise constitutes merely a participant.
As a result, this criterion analyzes which service or services represent the Saga orchestrator.
The criterion idea was based on the _"No. of services for orchestration criterion"_ in [\[3, p. 79\]](#3).

#### Specifying compensating transactions
Compensating transactions represent an essential aspect of the Saga pattern to undo previously done changes.
Consequently, the technology should provide the possibility to define compensating transactions.
This criterion was also part of the evaluation in [\[3, p. 79 f.\]](#3).

#### Compensation transaction allocation
Compensating previously done changes can be ensured using two ways: starting the compensation only for transactions that have been executed or always starting all compensating transactions of a Saga.
This has different implications for the implementation, such as introducing the necessity to prevent failures if the orchestrator tries to compensate non–executed transactions.
Therefore, this criterion examines if a compensating transaction is allocated to the corresponding transaction or simply to the entire Saga.
It is related to the _"Compensation only where needed criterion"_ in [\[3, p. 79 f.\]](#3).

#### Parallel execution of transactions
To reduce the Saga’s duration and improve performance, parallel execution of transactions can be preferable.
Therefore, the technology’s support of this is investigated here.
Dürr et al. also used this in their analysis [\[3, p. 79 f.\]](#3).

#### Parallel execution configurable
There might exist transactions that have to be executed sequentially.
Therefore, for technologies that allow parallel execution of transactions, the question arises if it is also possible to configure only certain transactions to be executed in parallel.

#### Participant communication selectable
The orchestrator and the participants can communicate in multiple ways, for example, using message brokers or HTTP requests.
Therefore it is investigated whether a technology facilitates different communication possibilities or only one.

#### External compensation trigger
Situations might arise that require aborting a running Saga externally, for example to update the system.
However, since some services might have already committed their changes, simply terminating the Saga will not be sufficient.
So instead of aborting the Saga, it is advantageous if a possibility is offered to trigger the compensation mechanism for a running Saga.
An example of such a possibility can be an API endpoint or a UI feature.

#### Choreographed Sagas
Sagas can also be organized using choreography instead of orchestration, which is why this criterion considers if a technology also supports implementing a choreographed approach.
Another criterion that Dürr et al. also evaluated [\[3, p. 79 f.\]](#3).

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
