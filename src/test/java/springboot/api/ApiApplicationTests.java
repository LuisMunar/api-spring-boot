package springboot.api;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiApplicationTests {

	@Test
	void contextLoads() {

	}

	@Test
	void testMainMethod() {
		try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
			ApiApplication.main(new String[] {});
			mockedSpringApplication.verify(() -> SpringApplication.run(ApiApplication.class, new String[] {}));
		}
	}
}
