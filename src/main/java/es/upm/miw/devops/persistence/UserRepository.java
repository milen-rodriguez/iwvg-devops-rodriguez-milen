package es.upm.miw.devops.persistence;

import es.upm.miw.devops.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
