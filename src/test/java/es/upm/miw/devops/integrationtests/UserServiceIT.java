package es.upm.miw.devops.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import es.upm.miw.devops.persistence.UserRepository;
import es.upm.miw.devops.service.NotFoundException;
import es.upm.miw.devops.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceIT {

  @Autowired private UserService userService;

  @Autowired private UserRepository userRepository;

  @Test
  void shouldActivateSeededUserById() {
    userService.activateById(1L);

    assertThat(userRepository.findById(1L).orElseThrow().isActive()).isTrue();
  }

  @Test
  void shouldThrowNotFoundExceptionWhenActivatingUnknownUser() {
    assertThatThrownBy(() -> userService.activateById(999L))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("User with id 999 was not found");
  }

  @Test
  void shouldFindSeededUserById() {
    assertThat(userService.findById(1L).getEmail()).isEqualTo("ada.lovelace@example.com");
  }

  @Test
  void shouldDeleteSeededUserById() {
    userService.deleteById(4L);

    assertThat(userRepository.findById(4L)).isEmpty();
  }

  @Test
  void shouldThrowNotFoundExceptionWhenDeletingUnknownUser() {
    assertThatThrownBy(() -> userService.deleteById(999L))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("User with id 999 was not found");
  }
}
