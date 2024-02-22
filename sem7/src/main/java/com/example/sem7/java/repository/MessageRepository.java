package com.example.sem7.java.repository;

import com.example.sem7.java.domain.Entity;
import com.example.sem7.java.domain.Message;

import java.util.List;

public interface MessageRepository<ID,E extends Entity<ID>> extends Repository<ID, E> {
   List<Message> findAllFMessages(String id1 ,String  id2);
}
