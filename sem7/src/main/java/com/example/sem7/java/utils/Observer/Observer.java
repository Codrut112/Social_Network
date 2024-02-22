package com.example.sem7.java.utils.Observer;

import com.example.sem7.java.domain.Message;
import com.example.sem7.java.domain.Prietenie;

public interface Observer {

    public void updateMessage(Message message);
    public void updateFriendship(Prietenie friendship);
}
