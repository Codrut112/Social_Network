package com.example.sem7.java.validators;


import com.example.sem7.java.domain.Utilizator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        String erori = "";
        if (entity.getId() == null) erori += "id vid\n";
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
        Matcher specialCharMatcher = specialCharPattern.matcher(entity.getPassword());

        if (entity.getPassword().length() < 8) erori += "password must contains at least 8 characters\n";
        if (!entity.getPassword().isEmpty()) {
            if (!entity.getPassword().matches(".*[A-Z].*"))
                erori += "password must contain at least a capital letter\n";
            if (!entity.getPassword().matches(".*[a-z].*"))
                erori += "password must contain at least an uppercase\n";
            if (!entity.getPassword().matches(".*\\d.*"))
                erori += "password must contain at least a number\n";
            if (!specialCharMatcher.find())
                erori += "password must contain at least an special character\n";
        }


        if (entity.getId().isEmpty()) erori += "email invalid\n";
        if (entity.getLastName().isEmpty()) erori += "missing last name\n";
        if (entity.getFirstName().isEmpty()) erori += "missing first name\n";
        if (!erori.isEmpty()) throw new ValidationException(erori);

    }
}

