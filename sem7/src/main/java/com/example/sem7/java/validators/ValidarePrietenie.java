package com.example.sem7.java.validators;

import com.example.sem7.java.domain.Prietenie;

import java.time.LocalDateTime;

public class ValidarePrietenie implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie prietenie) throws ValidationException {
        String IdPrieten1 = prietenie.getId().getLeft();
        String IdPrieten2 = prietenie.getId().getRight();
        if (IdPrieten1 == IdPrieten2) throw new ValidationException("a friendship cannot be between the same person");
        if (prietenie.getDate().isAfter(LocalDateTime.now())) throw new ValidationException("invalide friendship date");

    }
}
