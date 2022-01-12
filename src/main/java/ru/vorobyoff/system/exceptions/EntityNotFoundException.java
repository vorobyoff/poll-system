package ru.vorobyoff.system.exceptions;

public final class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super("Entity with the given id {" + id + "} does not exists.");
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}