package com.assertsl.workshop;

import com.assertsl.workshop.dto.DrugDto;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootApplicationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CamelContext camelContext;

	@Autowired
	private ProducerTemplate producerTemplate;

	@Test
	public void restDsl() {
		// Call the REST API
		ResponseEntity<String> response = restTemplate.getForEntity("/store/drug/all", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void createDrugRouteTest() throws Exception {

		MockEndpoint mock = camelContext.getEndpoint("mock:finishRoute", MockEndpoint.class);

		AdviceWith.adviceWith(camelContext, "createDrugRoute",
				// intercepting an exchange on route
				r -> {
					r.weaveAddLast().to(mock);
				}
		);

		// setting expectations
		mock.expectedMessageCount(1);

		DrugDto dto = new DrugDto();
		dto.setProductNdc("69618-010");

		// invoking consumer
		producerTemplate.sendBody("direct:createDrug", dto);

		// asserting mock is satisfied
		mock.assertIsSatisfied();


	}


}
