package es.upm.miw.devops.service;

import es.upm.miw.devops.domain.User;
import es.upm.miw.devops.persistence.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findById(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }
}
