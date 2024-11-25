package org.askend.controller;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

public class ResponseIdentifierError extends Exception {
    private static final long serialVersionUID = -3658520249501649146L;
    private String identifier;
    private String description;
    private String errorInfo;
    private HttpStatus status = HttpStatus.CONFLICT;

    public ResponseIdentifierError(String identifier) {
        super(identifier);
        this.identifier = identifier;
    }

    public ResponseIdentifierError(String message, Throwable cause, String identifier, HttpStatus status) {
        super(message, cause);
        this.identifier = identifier;
        this.status = status;
        this.errorInfo = format("%s%n%s", cause.getMessage(), getOurRootCauseStackTrace(cause.getStackTrace()));
    }

    public static String getOurRootCauseStackTrace(StackTraceElement[] stackTraceElements) {
        List<String> collStack = Arrays.stream(stackTraceElements)
                .filter(el ->
                        el.getClassName().startsWith("com.askend") && !el.getFileName().equals("<generated>"))
                .map(el -> "\tat " + el).toList();
        return String.join(";\n", collStack);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
