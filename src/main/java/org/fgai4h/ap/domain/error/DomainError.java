package org.fgai4h.ap.domain.error;

public enum DomainError implements Error {

    NOT_FOUND("The %s with %s [%s] could not be found"), CONFLICT_RECIPIENT("The %s with %s [%s] is exist");

    private final String message;

    DomainError(final String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
