package com.example.sem7.java.service;

import com.example.sem7.java.domain.Prietenie;
import com.example.sem7.java.domain.Tuple;
import com.example.sem7.java.domain.Utilizator;
import com.example.sem7.java.domain.Message;
import com.example.sem7.java.paging.*;
import com.example.sem7.java.repository.MessageRepository;

import com.example.sem7.java.utils.Observer.Observable;
import com.example.sem7.java.utils.Observer.Observer;
import com.example.sem7.java.validators.Error;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.ceil;

public class Service implements Observable {
    private PagingRepository<String, Utilizator> repoUtilizatori;
    private FriendshipsPagingRepository<Tuple<String, String>, Prietenie> repoPrietenii;
    private MessageRepository<UUID, Message> repoMessages;
    private UUID specialUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /**
     * constructor
     *
     * @param repoUtilizatori
     * @param repoPrietenii
     */
    public Service(PagingRepository repoUtilizatori, FriendshipsPagingRepository<Tuple<String, String>, Prietenie> repoPrietenii, MessageRepository<UUID, Message> repoMessages) {
        this.repoUtilizatori = repoUtilizatori;
        this.repoPrietenii = repoPrietenii;
        this.repoMessages = repoMessages;
    }

    /**
     * addd an user
     *
     * @param firstName String
     * @param lastName  String
     * @param id
     * @throws Error if id invalid
     */
    public void addUtilizator(String firstName, String lastName, String id, String password) throws Error {
        Utilizator utilizator = new Utilizator(firstName, lastName, id, password);
        Optional<Utilizator> ok = repoUtilizatori.save(utilizator);
        if (ok.isEmpty()) throw new Error("id invalid");
        //   System.out.println(utilizator.getId());
    }

    public void updateUtiliator(String id, String firstName, String lastName) {
        Utilizator utilizator = new Utilizator(firstName, lastName, id, "");
        Optional<Utilizator> ok = repoUtilizatori.update(utilizator);
        if (ok.isEmpty()) throw new Error("utilizator inexistent");
    }



    /**
     * create a friendship betweeen user1 and user2
     *
     * @param idUtilizator1 long
     * @param idUtilizator2 long
     * @throws Error -> pritenie existenta
     *               -> utilizator inexistent
     */
    public void addFriendship(String idUtilizator1, String idUtilizator2, LocalDateTime date) throws Error {

        if (idUtilizator1.equals(idUtilizator2)) throw new Error("you cant be friend with yourself");
        if (repoUtilizatori.findOne(idUtilizator1).isPresent() && repoUtilizatori.findOne(idUtilizator2).isPresent()) {
            Prietenie prietenie = new Prietenie(LocalDateTime.now(), idUtilizator1, idUtilizator2, "pending");

            if (repoPrietenii.findOne(new Tuple(idUtilizator1, idUtilizator2)).isEmpty() && repoPrietenii.findOne(new Tuple(idUtilizator2, idUtilizator1)).isEmpty()) {
                {
                    prietenie.setId(new Tuple(idUtilizator1, idUtilizator2));
                    prietenie.setDate(date);
                }
            } else {

                Optional<Prietenie> prietenie1 = repoPrietenii.findOne(new Tuple(idUtilizator1, idUtilizator2));
                if (prietenie1.isEmpty()) prietenie1 = repoPrietenii.findOne(new Tuple(idUtilizator2, idUtilizator1));
                Prietenie friendship = prietenie1.get();
                System.out.println(friendship.getStatus());
                switch (friendship.getStatus()) {
                    case "accepted":
                        throw new Error("you are already friend with this user");
                    case "rejected":
                        throw new Error("The user already rejected your friendship");
                    case "pending":
                        throw new Error("friend request already sent");
                    default:
                        throw new Error("eroare");
                }


            }

            repoPrietenii.save(prietenie);
            notifyall(prietenie);
        } else throw new Error("utilizator inexistent");
    }

    /**
     * return all the users
     *
     * @return toti utilizatorii
     */

    public Iterable<Utilizator> getPageOfUsers(int pageNumber, int pageSize) {
        Pageable pageable = new PageableImplementation(pageNumber, pageSize);
        return repoUtilizatori.findAll(pageable).getContent().toList();
    }




    public List<Utilizator> friendsOfUser(String idUtilizator) {

        List<Prietenie> rez = repoPrietenii.findAllFriends(idUtilizator);

        List<Utilizator> utilizators = rez.stream()
                .filter(prietenie -> prietenie.getStatus().equals("accepted"))
                .map(prietenie -> {

                    return repoUtilizatori.findOne(prietenie.prietenComun(idUtilizator)).get();
                }).toList();

        return utilizators;
    }

    public List<Utilizator> friendsOfUserPage(String id, int pageNumber, int pageSize) {
        Pageable pageable = new PageableImplementation(pageNumber, pageSize);
        Stream<Prietenie> rez = repoPrietenii.findAllFriends(id, pageable).getContent();

        List<Utilizator> utilizators = rez
                .filter(prietenie -> prietenie.getStatus().equals("accepted"))
                .map(prietenie -> {

                    return repoUtilizatori.findOne(prietenie.prietenComun(id)).get();
                }).toList();

        return utilizators;

    }

    public int getNumberOfPagesFriends(String id, int pageSizeFriends) {
        int numberOfFriends = repoPrietenii.getSize(id);
        System.out.println("are " + numberOfFriends);
        if (numberOfFriends % pageSizeFriends == 0) return numberOfFriends / pageSizeFriends;
        else return numberOfFriends / pageSizeFriends + 1;
    }


    public Iterable<Prietenie> getAllUserFriendships(String id) {
        List<Prietenie> rez = repoPrietenii.findAllFriends(id);

        return rez;
    }

    public Iterable<Prietenie> getPageFriendships(String id, int pageNumber, int pageSize) {
        Pageable pageable = new PageableImplementation(pageNumber, pageSize);
        List<Prietenie> rez = repoPrietenii.findAllFriendRequests(id, pageable).getContent().toList();

        return rez;
    }

    public Utilizator verifyUser(String username, String password) {
        Optional<Utilizator> found = repoUtilizatori.findOne(username);
        if (found.isPresent()) {
            Utilizator user = found.get();
            System.out.println(user.getPassword());
            if (!password.equals(user.getPassword())) throw new RuntimeException("login fail");
            return user;
        } else throw new RuntimeException("login fail");
    }

    public Utilizator findUser(String username) {
        Optional<Utilizator> found = repoUtilizatori.findOne(username);
        if (found.isPresent()) return found.get();
        else throw new RuntimeException("no user");
    }


    public List<Message> getAllMessBetweenTwoUsers(String id1, String id2) {
        return repoMessages.findAllFMessages(id1, id2).stream()
                .sorted(Comparator.comparing(Message::getDate))
                .toList();
    }


    public Message addMessage(String from, List<String> users, LocalDateTime date, String message) {
        Message mess = new Message(from, users, date, message);
        repoMessages.save(mess);
        notifyall(mess);
        return mess;
    }

    public Message addReplyMessage(String from, List<String> users, LocalDateTime date, String message, UUID respond) {
        Message mess = new Message(from, users, date, message);
        mess.setReply(respond);
        repoMessages.save(mess);
        notifyall(mess);
        return mess;
    }

    public String getFullMessage(Message message) {
        if (!Objects.equals(message.getReply(), specialUUID) && message.getReply() != null) {

            if (repoMessages.findOne(message.getReply()).isPresent()) {
                Message from = repoMessages.findOne(message.getReply()).get();
                return message.getFrom() + ": " + getFullMessage(from) + " reply: " + message.getMessage();

            }
        }
        return message.getFrom() + ": " + message.getMessage();
    }

    public void updateFriendship(Tuple<String, String> id, String newStatus) {
        Optional<Prietenie> found = repoPrietenii.findOne(id);
        if (found.isPresent()) {
            Prietenie friendship = found.get();
            friendship.setStatus(newStatus);
            repoPrietenii.update(friendship);
            notifyall(friendship);
        }
    }

    public int getNumberOfPagesUsers(int pageSizeUsers) {
        int numberOfUsers = repoUtilizatori.getSize();

        return (int) ceil((double) numberOfUsers / pageSizeUsers);
    }

    public int getNumberOfPagesFriendhsips(String idUser, int pageSizeFriendships) {
        int numberOfFriendships = repoPrietenii.getAllSize(idUser);
        return (int) ceil((double) numberOfFriendships / pageSizeFriendships);
    }

    @Override
    public void addObserve(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyall( Message message) {
        for(Observer o:observers)o.updateMessage(message);
    }

    @Override
    public void notifyall(Prietenie prietenie) {
        for(Observer o:observers)o.updateFriendship(prietenie);
    }


}
