package org.example.exception.extend;

import org.example.exception.NotFoundException;

public class FamilyNotFoundException extends NotFoundException {
    public FamilyNotFoundException(String message) {
        super(String.format(message));
    }
}
