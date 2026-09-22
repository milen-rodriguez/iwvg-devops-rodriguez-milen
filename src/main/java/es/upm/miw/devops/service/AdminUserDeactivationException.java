package es.upm.miw.devops.service;

public class AdminUserDeactivationException extends RuntimeException {

  public AdminUserDeactivationException(Long id) {
    super("Admin user with id " + id + " cannot be deactivated");
  }
}
