package es.upm.miw.devops.service.command;

public record UpdateUserCommand(
    String firstName,
    String familyName,
    String email,
    String identity,
    String address,
    String city,
    String province,
    String postalCode) {}
