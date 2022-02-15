Title: OSS Characteristics
Date: 2021
Context: Criteria Catalog

# :material-book-open-page-variant: Evaluation Criteria Catalog

In order to evaluate different technological approaches concerning the Saga pattern, some criteria have to be defined against which the evaluation can be performed.
The following sections consider several areas of interest related to characteristics of microservices and the Saga pattern as well as some quality attributes of the ISO/IEC 25010 Quality Models[^1].
For each area, the aspects that an evaluator should examine are explained and described.
Some of the described criteria have been taken from the previous paper published by Dürr et al. [\[3\]](#3).
The aim is to create a criteria catalog that can be used to analyze other technologies in this context.

-----------------------------------------------------------------------------

## ^^Open-Source Software Characteristics^^

OSS characteristics, such as who provides the technology or under which license it is offered, can also be relevant aspects to consider before selecting a technology.
If the technology is mainly implemented by a well–known company and a big community supports it, this can imply a certain quality and reliability.
Additionally, the company, the project’s circumstances and its community influence the product’s present and anticipated future [\[4, p. 108\]](#4).
However, some OSS characteristics have to be treated with caution as they may be subject to different interpretations, like repository stars, or since no generally accepted metrics exist, for example regarding documentation [\[5, p. 70\]](#5).

#### Provider
The organization or person that creates and provides the respective technology.


#### License type
The OSS license type under which the implementation is released.
The license type may introduce some restrictions which can be especially relevant for commercial usage.

#### Repository stars
The number of stars that the respective repository currently has.
This criterion may indicate the technology’s popularity.
However, since the significance of such a star is not certain, it is not possible to use this as a reliable metric.

#### Contributor count
The number of people the repository lists as contributors.

#### Fork count
The number of forks the repository currently has.
Forking a repository means creating a copy of it to another users account.
When editing content in the forked repository, it does not introduce changes to the parent repository[^8].

#### Support
Examining the kind of community and/or commercial support that is offered for this technology.
Such options can be discussion forums, the possibility to document issues or enterprise support like priority hotfixes.

#### Documentation
Comprehensive and up–to–date documentation is essential when using OSS, especially if no support is available.
Therefore, this criterion assesses if documentation on the use of the technology exists and whether it is up–to–date.
Such documentation can be, for example, a website or a readme file in the repository.

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

[^8]: [https://docs.github.com/en/get-started/quickstart/fork-a-repo](https://docs.github.com/en/get-started/quickstart/fork-a-repo), last accessed:
2021-06-27

--8<-- "includes/abbreviations.md"
