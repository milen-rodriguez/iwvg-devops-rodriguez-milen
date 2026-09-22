package es.upm.miw.devops.rest.dto;

import es.upm.miw.devops.domain.Role;

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
    Role role,
    boolean billable,
    boolean active) {}
