package test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
//import static io.restassured.matcher.RestAssuredMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;

@Profile("dev")
public class APITest {

	private String url = "http://localhost:8080";

	@Test
	public void givenUrl_whenSuccessOnGetsResponseAndJsonHasRequiredKV_thenCorrect() {

		given().header("Content-type", MediaType.APPLICATION_JSON)
		.queryParam("keyword", "CR")
			.get(url + "/notification/getFeedByContent").then().log().body();
	}
}
