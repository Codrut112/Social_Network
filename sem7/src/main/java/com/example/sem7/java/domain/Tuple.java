package com.example.sem7.java.domain;

import java.util.Objects;


/**
 * Define a Tuple o generic type entities
 *
 * @param <E1> - tuple first entity type
 * @param <E2> - tuple second entity type
 */
public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    /**
     * constructor
     *
     * @param e1 tuple first entity type
     * @param e2 tuple second entity type
     */
    public Tuple(E1 e1, E2 e2) {

        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * @return tuple first entity type
     */
    public E1 getLeft() {
        return e1;
    }

    /**
     * set tuple first entity value
     *
     * @param e1 E1
     */
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    /**
     * @return tuple second entity type
     */
    public E2 getRight() {
        return e2;
    }


    /**
     * override toString
     *
     * @return String
     */
    @Override
    public String toString() {
        return "" + e1 + "," + e2;

    }

    /**
     * override equals
     *
     * @param obj Object
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return this.e1.equals(((Tuple) obj).e1) && this.e2.equals(((Tuple) obj).e2);
    }

    /**
     * override hashCode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}