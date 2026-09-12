package es.upm.miw.devops.functionaltests;

import static org.assertj.core.api.Assertions.assertThat;

import es.upm.miw.devops.domain.User;
import es.upm.miw.devops.persistence.UserRepository;
import es.upm.miw.devops.rest.UserResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class UserResourceFT {

  @Autowired private WebTestClient webTestClient;

  @Autowired private UserRepository userRepository;

  @Test
  void shouldDeleteExistingUser() {
    User user = userRepository.saveAndFlush(user());

    webTestClient
        .delete()
        .uri(UserResource.USER + "/" + user.getId())
        .exchange()
        .expectStatus()
        .isNoContent()
        .expectBody()
        .isEmpty();

    assertThat(userRepository.findById(user.getId())).isEmpty();
  }

  @Test
  void shouldReturnNotFoundWhenDeletingUnknownUser() {
    webTestClient
        .delete()
        .uri(UserResource.USER + "/9223372036854775807")
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.error")
        .isEqualTo("NotFoundException")
        .jsonPath("$.message")
        .isEqualTo("User with id 9223372036854775807 was not found")
        .jsonPath("$.code")
        .isEqualTo(404);
  }

  @Test
  void shouldReturnNotFoundWhenDeletingNegativeUserId() {
    webTestClient.delete().uri(UserResource.USER + "/-1").exchange().expectStatus().isNotFound();
  }

  @Test
  void shouldNotAllowDeletingTheSameUserTwice() {
    User user = userRepository.saveAndFlush(user());
    String uri = UserResource.USER + "/" + user.getId();

    webTestClient.delete().uri(uri).exchange().expectStatus().isNoContent();

    webTestClient.delete().uri(uri).exchange().expectStatus().isNotFound();
  }

  private User user() {
    return new User(
        "Ada",
        "Lovelace",
        "ada.lovelace@example.com",
        "ID-DELETE-TEST",
        "1 Analytical Engine Way",
        "London",
        "London",
        "N1 1AA");
  }
}
