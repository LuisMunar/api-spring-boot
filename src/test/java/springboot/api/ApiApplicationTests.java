package springboot.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiApplicationTests {

	@Test
	void contextLoads() {}

	@Test
	void testTwoEqualsTwo() {
		assertEquals(2, 2, "2 should be equal to 2");
	}
}
