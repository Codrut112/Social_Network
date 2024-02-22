package com.example.sem7.java.paging;

import com.example.sem7.java.domain.Entity;
import com.example.sem7.java.domain.Prietenie;
import com.example.sem7.java.repository.FriendshipsRepository;
import com.example.sem7.java.repository.Repository;

public interface FriendshipsPagingRepository<ID,
        E extends Entity<ID>>
        extends FriendshipsRepository<ID, E> {

    Page<E> findAllFriends(String id,Pageable pageable);
    int getSize(String idUser);
    int getAllSize(String idUser);
    Page<Prietenie> findAllFriendRequests(String id, Pageable pageable);
}