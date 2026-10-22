package com.sortmaster.exception;

public class AlgorithmNotFoundException extends RuntimeException {
    public AlgorithmNotFoundException(String algorithmId) {
        super("Algorithm not found with ID: " + algorithmId);
    }
}
