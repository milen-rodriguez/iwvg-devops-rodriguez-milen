package es.upm.miw.devops.functionaltests;

import static org.assertj.core.api.Assertions.assertThat;

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
  void shouldActivateExistingUser() {
    webTestClient
        .put()
        .uri(UserResource.USER + "/1/active")
        .exchange()
        .expectStatus()
        .isNoContent()
        .expectBody()
        .isEmpty();

    webTestClient
        .get()
        .uri(UserResource.USER + "/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.active")
        .isEqualTo(true);
  }

  @Test
  void shouldReturnNotFoundWhenActivatingUnknownUser() {
    webTestClient
        .put()
        .uri(UserResource.USER + "/9223372036854775807/active")
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  void shouldFindExistingUserById() {
    webTestClient
        .get()
        .uri(UserResource.USER + "/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(1)
        .jsonPath("$.firstName")
        .isEqualTo("Ada")
        .jsonPath("$.familyName")
        .isEqualTo("Lovelace")
        .jsonPath("$.email")
        .isEqualTo("ada.lovelace@example.com")
        .jsonPath("$.identity")
        .isEqualTo("ID-1")
        .jsonPath("$.address")
        .isEqualTo("1 Analytical Engine Way")
        .jsonPath("$.city")
        .isEqualTo("London")
        .jsonPath("$.province")
        .isEqualTo("London")
        .jsonPath("$.postalCode")
        .isEqualTo("N1 1AA")
        .jsonPath("$.billable")
        .isEqualTo(true);
  }

  @Test
  void shouldMarkUserWithBlankAddressAsNotBillable() {
    webTestClient
        .get()
        .uri(UserResource.USER + "/5")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.billable")
        .isEqualTo(false);
  }

  @Test
  void shouldMarkUserWithNullAddressAsNotBillable() {
    webTestClient
        .get()
        .uri(UserResource.USER + "/6")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.billable")
        .isEqualTo(false);
  }

  @Test
  void shouldReturnNotFoundWhenFindingUnknownUser() {
    webTestClient
        .get()
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
  void shouldDeleteExistingUser() {
    webTestClient
        .delete()
        .uri(UserResource.USER + "/2")
        .exchange()
        .expectStatus()
        .isNoContent()
        .expectBody()
        .isEmpty();

    assertThat(userRepository.findById(2L)).isEmpty();
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
    String uri = UserResource.USER + "/3";

    webTestClient.delete().uri(uri).exchange().expectStatus().isNoContent();

    webTestClient.delete().uri(uri).exchange().expectStatus().isNotFound();
  }
}
