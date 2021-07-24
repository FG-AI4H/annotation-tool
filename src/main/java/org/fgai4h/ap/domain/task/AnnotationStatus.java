package org.fgai4h.ap.domain.task;

public enum AnnotationStatus {
    PENDING("P"),COMPLETED("C"), REJECTED("R");

    private String code;

    private AnnotationStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
