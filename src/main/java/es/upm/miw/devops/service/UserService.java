package es.upm.miw.devops.service;

import es.upm.miw.devops.domain.User;
import es.upm.miw.devops.persistence.UserRepository;
import es.upm.miw.devops.rest.mapper.UserMapper;
import es.upm.miw.devops.service.command.UpdateUserCommand;
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
}
