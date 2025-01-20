package service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.server.port=0"})
class OpsServiceApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;


    @Value("${local.management.port}")
    private int managementPort;


    @Test
    void checkForRestControllerStatus() throws Exception {
        ResponseEntity<Map> response =
                testRestTemplate
                        .getForEntity("http://localhost:" + port + "/greet", Map.class);
        then(response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    void checkForManagementStatus() throws Exception {
        ResponseEntity<Map> response =
                testRestTemplate
                        .getForEntity("http://localhost:" + managementPort + "/actuator", Map.class);
        then(response.getStatusCode().equals(HttpStatus.OK));
    }

}
