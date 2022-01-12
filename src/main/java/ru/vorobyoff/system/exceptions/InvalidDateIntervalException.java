package ru.vorobyoff.system.exceptions;

import java.time.temporal.Temporal;

public final class InvalidDateIntervalException extends RuntimeException {
    public InvalidDateIntervalException(Temporal past, Temporal future) {
        super(future + " > " + past);
    }

    public InvalidDateIntervalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateIntervalException(String message) {
        super(message);
    }
}