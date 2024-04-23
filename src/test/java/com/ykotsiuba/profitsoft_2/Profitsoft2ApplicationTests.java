package com.ykotsiuba.profitsoft_2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest
@Import(TestProfitsoft2Application.class)
@ActiveProfiles("test")
class Profitsoft2ApplicationTests {

	@Test
	void contextLoads() {
	}

}
