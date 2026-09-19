package es.upm.miw.devops.functionaltests;

import static org.assertj.core.api.Assertions.assertThat;

import es.upm.miw.devops.rest.SystemResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class SystemResourceFT {

  @Autowired private WebTestClient webTestClient;

  @Test
  void testReadBadge() {
    webTestClient
        .get()
        .uri(SystemResource.VERSION_BADGE)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .value(body -> assertThat(body).isNotNull().startsWith("<svg"));
  }

  @Test
  void testReadInfo() {
    webTestClient
        .get()
        .uri("/")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .value(body -> assertThat(body).isNotNull().isNotEmpty());
  }

  @Test
  void testReadHealth() {
    webTestClient
        .get()
        .uri("/actuator/health")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo("UP");
  }
}
