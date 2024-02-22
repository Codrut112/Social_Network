package com.example.sem7.java.validators;

public class Error extends RuntimeException {

    private String message;//mesajul transmis de eroare

    public Error(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
