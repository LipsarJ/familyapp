package org.example.exception.extend;

import org.example.controlleradvice.Errors;
import org.example.exception.BadDataException;

public class FamilyNameException extends BadDataException {
    public FamilyNameException(String message, Errors errorCode) {
        super(message, errorCode);
    }
}
