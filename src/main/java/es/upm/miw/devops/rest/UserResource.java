package es.upm.miw.devops.rest;

import es.upm.miw.devops.rest.dto.UserActiveRequest;
import es.upm.miw.devops.rest.dto.UserRequest;
import es.upm.miw.devops.rest.dto.UserResponse;
import es.upm.miw.devops.rest.mapper.UserMapper;
import es.upm.miw.devops.service.UserService;
import es.upm.miw.devops.service.command.UpdateUserActiveCommand;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserResource.USER)
public class UserResource {

  public static final String USER = "/user";

  private final UserMapper userMapper;
  private final UserService userService;

  public UserResource(UserMapper userMapper, UserService userService) {

    this.userMapper = userMapper;
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public UserResponse findById(@PathVariable Long id) {
    return userMapper.toResponse(userService.findById(id));
  }

  @GetMapping
  public List<UserResponse> findAll(
      @RequestParam(required = false) Boolean billable,
      @RequestParam(required = false) Boolean active) {
    return userService.findAll(billable, active).stream().map(userMapper::toResponse).toList();
  }

  @PutMapping("/{id}")
  public UserResponse updateById(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
    return userMapper.toResponse(
        userService.updatePersonalDataById(id, userMapper.toUpdateCommand(request)));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Long id) {
    userService.deleteById(id);
  }

  @PutMapping("/{id}/active")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void activateById(@PathVariable Long id) {
    userService.activateById(id);
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateActiveStatuses(@RequestBody List<@Valid UserActiveRequest> requests) {
    userService.updateActiveStatuses(
        requests.stream()
            .map(request -> new UpdateUserActiveCommand(request.id(), request.active()))
            .toList());
  }
}
