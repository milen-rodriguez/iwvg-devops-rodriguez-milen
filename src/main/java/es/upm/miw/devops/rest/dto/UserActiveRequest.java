package es.upm.miw.devops.rest.dto;

import jakarta.validation.constraints.NotNull;

public record UserActiveRequest(@NotNull Long id, boolean active) {}
