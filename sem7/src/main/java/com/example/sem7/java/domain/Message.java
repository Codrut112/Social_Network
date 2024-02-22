package com.example.sem7.java.domain;

import com.example.sem7.java.domain.Entity;
import com.example.sem7.java.domain.Utilizator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Message extends Entity<UUID> {

    private List<String> to;
    private LocalDateTime date;
    private String message;
    private String from;
    private UUID reply;


    public String getFrom() {
        return from;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<String> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public Message(String from, List<String> receivers, LocalDateTime date, String message) {
        this.from=from;
        this.setId(UUID.randomUUID());
        this.to = receivers;
        this.date = date;
        this.message = message;

    }


    public UUID getReply() {
        return reply;
    }

    public void setReply(UUID reply) {
        this.reply = reply;
    }

}
