MVNW=./mvnw

test:
	$(MVNW) test

test-coverage:
	$(MVNW) test jacoco:report

test-coverage-clean:
	$(MVNW) clean test jacoco:report