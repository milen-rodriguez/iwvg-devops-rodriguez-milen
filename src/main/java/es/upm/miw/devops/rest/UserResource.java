package es.upm.miw.devops.rest;

import es.upm.miw.devops.rest.dto.UserResponse;
import es.upm.miw.devops.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserResource.USER)
public class UserResource {

  public static final String USER = "/user";

  private final UserService userService;

  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public UserResponse findById(@PathVariable Long id) {
    return UserResponse.from(userService.findById(id));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Long id) {
    userService.deleteById(id);
  }
}
