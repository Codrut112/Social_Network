package com.example.sem7.java.domain;

import java.io.Serializable;
import java.util.Objects;

public class Entity<ID> implements Serializable {

    // private static final long serialVersionUID = 7331115341259248461L;
    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    /**
     * override equals
     *
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    /**
     * override hashcode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * override toString
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}