package com.example.sem7.java.validators;

@FunctionalInterface
public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
