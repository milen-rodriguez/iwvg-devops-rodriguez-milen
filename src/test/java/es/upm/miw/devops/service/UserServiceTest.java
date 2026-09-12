package es.upm.miw.devops.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import es.upm.miw.devops.domain.User;
import es.upm.miw.devops.persistence.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  @Test
  void shouldFindExistingUserById() {
    Long userId = 1L;
    User user =
        new User(
            "Ada", "Lovelace", "ada@example.com", "ID-1", "Address", "City", "Province", "12345");
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
            "Ada", "Lovelace", "ada@example.com", "ID-1", "Address", "City", "Province", "12345");
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
