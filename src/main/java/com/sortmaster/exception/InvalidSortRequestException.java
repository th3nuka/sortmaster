package com.sortmaster.exception;

public class InvalidSortRequestException extends RuntimeException {
    public InvalidSortRequestException(String message) {
        super(message);
    }
}
