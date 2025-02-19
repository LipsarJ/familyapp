package org.example.exception.extend;

import org.example.controlleradvice.Errors;
import org.example.exception.BadDataException;

public class IllegalStatusException extends BadDataException {
    public IllegalStatusException(String message, Errors errorCode) {
        super(message, errorCode);
    }
}
