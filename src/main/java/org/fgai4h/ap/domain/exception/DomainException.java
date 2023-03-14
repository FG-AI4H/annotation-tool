package org.fgai4h.ap.domain.exception;

import lombok.Getter;
import org.fgai4h.ap.domain.error.Error;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

@Getter
public abstract class DomainException extends RuntimeException {

    private static final long serialVersionUID = 117084997795977835L;

    private final Error error;
    private final String messageCode;

    protected DomainException(final Throwable cause, final Error error, final String messageCode, final Object... args) {
        super(isNotEmpty(args) ? String.format(error.getMessage(), args) : error.getMessage(), cause);
        this.error = error;
        this.messageCode = messageCode;
    }

    protected DomainException(final Throwable cause, final Error error, final Object... args) {
        this(cause, error, null, args);
    }

    protected DomainException(final Error error, final String messageCode, final Object... args) {
        super(isNotEmpty(args) ? String.format(error.getMessage(), args) : error.getMessage());
        this.error = error;
        this.messageCode = messageCode;
    }

    protected DomainException(final Error error, final Object... args) {
        this(error, null, args);
    }
}
