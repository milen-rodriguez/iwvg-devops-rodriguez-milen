package es.upm.miw.devops.rest.mapper;

import es.upm.miw.devops.domain.User;
import es.upm.miw.devops.rest.dto.UserRequest;
import es.upm.miw.devops.rest.dto.UserResponse;
import es.upm.miw.devops.service.command.UpdateUserCommand;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

  UpdateUserCommand toUpdateCommand(UserRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "active", ignore = true)
  @Mapping(target = "role", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User merge(@MappingTarget User user, UpdateUserCommand updateUserCommand);

  UserResponse toResponse(User user);
}
