# Getting Started

## Requierements
- Install the next dependencies in the POM fot the unit test
  - `spring-boot-starter-test`
  - `junit-jupiter`
  - `mockito-inline`
  - `mockito-core`
  - `mockito-junit-jupiter`

- Install the next plugins in the POM for the unit test coverage
  - `jacoco-maven-plugin`

- NOTE: Consider the posibility that to execute unit test with jacoc, mockito or junit, we need change th version java in pom to 17 `<java.version>17</java.version>`

## Scripts
- To execute all unit test
  - `make test`

- To execute a single test
  - `make test-single TEST-CLASS=<TestClassName>`

- To execute unit test with coverage
  - `make test-coverage`
  - NOTE: If the jacoco report is damage, we need execute `make clean`
  - NOTE: We can execute clean and unit test in the se command if we wish `make test-coverage-clean`
