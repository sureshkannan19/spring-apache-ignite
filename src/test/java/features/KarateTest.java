package features;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.ConfigurableApplicationContext;

import com.intuit.karate.junit5.Karate;
import com.intuit.karate.junit5.Karate.Test;
import com.sk.SpringwebApplication;

public class KarateTest {

	private static ConfigurableApplicationContext applicationContext;

	@BeforeAll
	public static void setUp() {
		applicationContext = SpringwebApplication.start(new String[] { "Dkarate.env=unit" });
	}

	@AfterAll
	public static void stop() {
		SpringwebApplication.stop(applicationContext);
	}

	@Test
	public Karate runTest() {
		return Karate.run("notification.feature").relativeTo(getClass());
	}
}
