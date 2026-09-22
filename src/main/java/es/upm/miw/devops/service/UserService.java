package es.upm.miw.devops.service;

import es.upm.miw.devops.domain.User;
import es.upm.miw.devops.persistence.UserRepository;
import es.upm.miw.devops.persistence.UserSpecifications;
import es.upm.miw.devops.rest.mapper.UserMapper;
import es.upm.miw.devops.service.command.UpdateUserActiveCommand;
import es.upm.miw.devops.service.command.UpdateUserCommand;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;

  public UserService(UserMapper userMapper, UserRepository userRepository) {
    this.userMapper = userMapper;
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
  }

  @Transactional(readOnly = true)
  public List<User> findAll(Boolean billable, Boolean active) {
    return userRepository.findAll(UserSpecifications.withFilters(billable, active));
  }

  @Transactional
  public User updatePersonalDataById(Long id, UpdateUserCommand command) {
    User user = findById(id);
    return userMapper.merge(user, command);
  }

  @Transactional
  public void deleteById(Long id) {
    userRepository.delete(findById(id));
  }

  @Transactional
  public void activateById(Long id) {
    User user = findById(id);
    user.setActive(true);
  }

  @Transactional
  public void updateActiveStatuses(List<UpdateUserActiveCommand> commands) {
    Map<Long, User> usersById =
        userRepository
            .findAllById(commands.stream().map(UpdateUserActiveCommand::id).toList())
            .stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));

    commands.forEach(
        command -> {
          User user = usersById.get(command.id());
          if (user == null) {
            throw new NotFoundException(User.class, command.id());
          }
          if (user.isAdmin() && !command.active()) {
            throw new AdminUserDeactivationException(command.id());
          }
          user.setActive(command.active());
        });
  }
}
