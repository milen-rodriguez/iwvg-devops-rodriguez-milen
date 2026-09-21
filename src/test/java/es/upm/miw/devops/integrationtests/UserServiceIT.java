package es.upm.miw.devops.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import es.upm.miw.devops.persistence.UserRepository;
import es.upm.miw.devops.service.NotFoundException;
import es.upm.miw.devops.service.UserService;
import es.upm.miw.devops.service.command.UpdateUserCommand;
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
  void shouldUpdateSeededUserPersonalData() {
    UpdateUserCommand command =
        new UpdateUserCommand(
            "Ada",
            "Byron Lovelace",
            "ada.lovelace@example.com",
            "ID-1",
            "New address",
            "London",
            "London",
            "N1 1AA");

    userService.updatePersonalDataById(1L, command);

    assertThat(userRepository.findById(1L).orElseThrow().getFamilyName())
        .isEqualTo("Byron Lovelace");
  }

  @Test
  void shouldThrowNotFoundExceptionWhenUpdatingUnknownUser() {
    UpdateUserCommand command =
        new UpdateUserCommand(
            "Ada",
            "Lovelace",
            "ada.lovelace@example.com",
            "ID-1",
            "Address",
            "London",
            "London",
            "N1 1AA");

    assertThatThrownBy(() -> userService.updatePersonalDataById(999L, command))
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
