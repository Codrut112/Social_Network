package com.example.sem7.java.domain;

import java.util.Objects;

public class Utilizator extends Entity<String> {
    //firstname
    private String firstName;
    //lastname
    private String lastName;

    private String password;

    public String getPassword() {
        return password;
    }

    /**
     * construnctor
     *
     * @param firstName String
     * @param lastName  String
     * @param username  String
     * @param password
     */
    public Utilizator(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.setId(username);
    }

    /**
     * @return the firstname
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * return LastName
     *
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * override toString
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                " id= " + id +
                '}';
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
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());

    }

    /**
     * override hashcode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}