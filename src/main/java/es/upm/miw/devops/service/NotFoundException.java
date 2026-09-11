package es.upm.miw.devops.service;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> entityType, Object id)
    {
        super(entityType.getSimpleName() + " with id " + id + " was not found");
    }
}
