package com.ubs.fxinfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FxinfoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FxinfoApplicationTests {

	private TestRestTemplate template = new TestRestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	@LocalServerPort
	private int port;

	@Test
	public void testLogin() {
		LoginTestRequest login = new LoginTestRequest();
		login.setUserName("satendra");
		login.setPassword(new char[] { 't', 'e', 's', 't' });
		HttpEntity<LoginTestRequest> entity = new HttpEntity<LoginTestRequest>(
				login, headers);

		ResponseEntity<String> response = template.exchange(
				createURLWithPort("/login/login"), HttpMethod.POST, entity,
				String.class);
		System.out.println("got: " + response.getBody());

	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
