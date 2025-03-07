package org.example.exception.extend;

import org.example.controlleradvice.Errors;
import org.example.exception.BadDataException;

public class ProductAlreadyExists extends BadDataException {
    public ProductAlreadyExists(String message, Errors errorCode) {
        super(message, errorCode);
    }
}
