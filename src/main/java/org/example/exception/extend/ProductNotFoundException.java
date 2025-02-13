package org.example.exception.extend;

import org.example.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(String message) {
        super(String.format(message));
    }
}
