package es.upm.miw.devops.rest.dto;

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
    boolean active) {}
