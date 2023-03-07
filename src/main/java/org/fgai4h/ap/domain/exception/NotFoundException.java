package org.fgai4h.ap.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.fgai4h.ap.domain.error.Error;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends DomainException {

    private static final long serialVersionUID = -5401861657559581998L;

    public NotFoundException(final Error error, final Object... args) {
        super(error, args);
    }
}
