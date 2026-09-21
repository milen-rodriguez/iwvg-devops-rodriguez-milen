package es.upm.miw.devops.service.command;

public record UpdateUserActiveCommand(Long id, boolean active) {}
