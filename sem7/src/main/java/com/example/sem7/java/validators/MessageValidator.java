package com.example.sem7.java.validators;

import com.example.sem7.java.domain.Message;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        String erori = "";
        if (entity.getId() == null) erori += "id vid ";
        if (entity.getMessage().isEmpty()) erori += "missing message ";
        if (entity.getFrom().isEmpty()) erori += "dont know who sent this message ";
        if(entity.getTo().isEmpty())erori += "message has no receiver ";
        if (!erori.isEmpty()) throw new ValidationException(erori);
    }
}
