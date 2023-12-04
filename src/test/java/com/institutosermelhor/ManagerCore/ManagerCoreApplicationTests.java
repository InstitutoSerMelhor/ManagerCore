package com.institutosermelhor.ManagerCore;

import static org.assertj.core.api.Assertions.*;

import com.institutosermelhor.ManagerCore.controller.AuthenticationController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ManagerCoreApplicationTests {
	@Test
	void contextLoads(ApplicationContext context) {
		assertThat(context).isNotNull();
	}

	@Test
	void hasAuthController(ApplicationContext context) {
		assertThat(context.getBean(AuthenticationController.class)).isNotNull();
	}
}
