package com.example.sem7.java.paging;


import com.example.sem7.java.domain.Entity;
import com.example.sem7.java.repository.Repository;

public interface PagingRepository<ID,
        E extends Entity<ID>>
        extends Repository<ID, E> {

    Page<E> findAll(Pageable pageable);
    int getSize();
}
