package org.fgai4h.ap.domain.error;

import java.io.Serializable;

public interface Error extends Serializable {

    String getCode();

    String getMessage();
}
