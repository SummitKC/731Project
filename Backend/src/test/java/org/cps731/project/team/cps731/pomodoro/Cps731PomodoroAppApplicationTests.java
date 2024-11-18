package org.cps731.project.team.cps731.pomodoro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EntityScan(basePackages = "org.cps731.project.team.cps731.pomodoro.data.model")
public class Cps731PomodoroAppApplicationTests {

	@Test
	void contextLoads() {
	}

}
