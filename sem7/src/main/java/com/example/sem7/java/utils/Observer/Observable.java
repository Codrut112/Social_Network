package com.example.sem7.java.utils.Observer;

import com.example.sem7.java.domain.Message;
import com.example.sem7.java.domain.Prietenie;
import com.example.sem7.java.domain.Utilizator;

import java.util.ArrayList;
import java.util.List;

public interface Observable {
    List<Observer> observers = new ArrayList<>();

    public void addObserve(Observer o);

    public void notifyall( Message mesaj);
    public void notifyall(Prietenie prietenie);
}
