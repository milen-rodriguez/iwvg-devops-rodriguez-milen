package es.upm.miw.devops.rest.dto;

import es.upm.miw.devops.domain.User;

public record UserResponse(
    Long id,
    String firstName,
    String familyName,
    String email,
    String identity,
    String address,
    String city,
    String province,
    String postalCode,
    boolean billable,
    boolean active) {

  public static UserResponse from(User user) {
    return new UserResponse(
        user.getId(),
        user.getFirstName(),
        user.getFamilyName(),
        user.getEmail(),
        user.getIdentity(),
        user.getAddress(),
        user.getCity(),
        user.getProvince(),
        user.getPostalCode(),
        user.isBillable(),
        user.isActive());
  }
}
