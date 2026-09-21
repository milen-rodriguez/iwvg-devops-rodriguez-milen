package es.upm.miw.devops.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import es.upm.miw.devops.domain.User;
import es.upm.miw.devops.persistence.UserRepository;
import es.upm.miw.devops.rest.mapper.UserMapper;
import es.upm.miw.devops.service.command.UpdateUserCommand;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserService userService;

  @Test
  void shouldUpdateExistingUserPersonalData() {
    Long userId = 1L;
    User user =
        new User(
            "Ada",
            "Lovelace",
            "ada@example.com",
            "ID-1",
            "Address",
            "City",
            "Province",
            "12345",
            false);
    UpdateUserCommand command =
        new UpdateUserCommand(
            "Ada",
            "Byron Lovelace",
            "ada@example.com",
            "ID-1",
            "New address",
            "City",
            "Province",
            "12345");
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userMapper.merge(user, command)).thenReturn(user);

    User result = userService.updatePersonalDataById(userId, command);

    assertThat(result).isSameAs(user);
    verify(userRepository).findById(userId);
    verify(userMapper).merge(user, command);
    verifyNoMoreInteractions(userRepository, userMapper);
  }

  @Test
  void shouldNotUpdateUserPersonalDataWhenItDoesNotExist() {
    Long userId = 1L;
    UpdateUserCommand command =
        new UpdateUserCommand(
            "Ada",
            "Byron Lovelace",
            "ada@example.com",
            "ID-1",
            "New address",
            "City",
            "Province",
            "12345");
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.updatePersonalDataById(userId, command))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("User with id 1 was not found");

    verify(userRepository).findById(userId);
    verifyNoMoreInteractions(userRepository, userMapper);
  }

  @Test
  void shouldActivateExistingUser() {
    Long userId = 1L;
    User user =
        new User(
            "Ada",
            "Lovelace",
            "ada@example.com",
            "ID-1",
            "Address",
            "City",
            "Province",
            "12345",
            false);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    userService.activateById(userId);

    assertThat(user.isActive()).isTrue();
    verify(userRepository).findById(userId);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldNotActivateUserWhenItDoesNotExist() {
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.activateById(userId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("User with id 1 was not found");

    verify(userRepository).findById(userId);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldFindExistingUserById() {
    Long userId = 1L;
    User user =
        new User(
            "Ada",
            "Lovelace",
            "ada@example.com",
            "ID-1",
            "Address",
            "City",
            "Province",
            "12345",
            false);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    User result = userService.findById(userId);

    assertThat(result).isSameAs(user);
    verify(userRepository).findById(userId);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldThrowNotFoundExceptionWhenFindingUnknownUser() {
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.findById(userId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("User with id 1 was not found");

    verify(userRepository).findById(userId);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldDeleteExistingUser() {
    Long userId = 1L;
    User user =
        new User(
            "Ada",
            "Lovelace",
            "ada@example.com",
            "ID-1",
            "Address",
            "City",
            "Province",
            "12345",
            false);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    userService.deleteById(userId);

    verify(userRepository).findById(userId);
    verify(userRepository).delete(user);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldNotDeleteUserWhenItDoesNotExist() {
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.deleteById(userId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("User with id 1 was not found");

    verify(userRepository).findById(userId);
    verifyNoMoreInteractions(userRepository);
  }
}
