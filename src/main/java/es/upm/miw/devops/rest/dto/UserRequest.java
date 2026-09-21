package es.upm.miw.devops.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
    @NotBlank String firstName,
    @NotBlank String familyName,
    @NotBlank @Email String email,
    @NotBlank String identity,
    @NotBlank String address,
    @NotBlank String city,
    @NotBlank String province,
    @NotBlank String postalCode) {}
