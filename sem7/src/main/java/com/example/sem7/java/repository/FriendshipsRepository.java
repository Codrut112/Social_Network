package com.example.sem7.java.repository;

import com.example.sem7.java.domain.Entity;
import com.example.sem7.java.domain.Prietenie;

import java.util.List;

public interface FriendshipsRepository<ID,E extends Entity<ID>> extends Repository<ID,E> {
    public List<Prietenie> findAllFriends(String idUtilizator);
}
