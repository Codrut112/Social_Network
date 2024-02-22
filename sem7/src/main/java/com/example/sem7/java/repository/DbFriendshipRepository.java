package com.example.sem7.java.repository;

import com.example.sem7.java.domain.Prietenie;
import com.example.sem7.java.domain.Tuple;
import com.example.sem7.java.domain.Utilizator;
import com.example.sem7.java.validators.Error;
import com.example.sem7.java.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class DbFriendshipRepository implements FriendshipsRepository<Tuple<String, String>, Prietenie> {
    protected final String url;
    protected final String username;
    protected String password;
    protected final Validator validator;

    public DbFriendshipRepository(String url, String username, String password, Validator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Prietenie> findOne(Tuple<String, String> id) {

        String id1 = id.getLeft();
        String id2 = id.getRight();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendships" + " where id1=? and id2=?")
        ) {
            statement.setString(1, id1);
            statement.setString(2, id2);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id1 = resultSet.getString("id1");
                id2 = resultSet.getString("id2");
                Date sqlDate = resultSet.getDate("start_date");
                String status=resultSet.getString("status");
                Timestamp timestamp = new Timestamp(sqlDate.getTime());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                Prietenie friendship = new Prietenie(localDateTime, id1, id2,status);
                return Optional.of(friendship);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Prietenie> findAll() {
        HashSet<Prietenie> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendships");
             ResultSet resultSet = statement.executeQuery();

        ) {
            while (resultSet.next()) {
                String id1 = resultSet.getString("id1");
                String id2 = resultSet.getString("id2");
                Date sqlDate = resultSet.getDate("start_date");
                String status=resultSet.getString("status");
                Timestamp timestamp = new Timestamp(sqlDate.getTime());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                Prietenie friendship = new Prietenie(localDateTime, id1, id2,status);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Prietenie> save(Prietenie friendship) {

        validator.validate(friendship);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into friendships Values(?,?,?,?)");
        ) {

            statement.setString(2, friendship.getId().getLeft());
            statement.setString(3, friendship.getId().getRight());


            LocalDateTime localDateTime = friendship.getDate(); // Replace with your LocalDateTime variable
            //  System.out.println("haide");
            Timestamp sqlTimestamp = Timestamp.valueOf(localDateTime);
            // System.out.println("aproapre");
            statement.setTimestamp(1, sqlTimestamp);
            // System.out.println("a reusit");
            statement.setString(4,friendship.getStatus());
            System.out.println("se pregateste executia");
            statement.execute();
            System.out.println("a executat");
            return Optional.of(friendship);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Prietenie> delete(Tuple<String, String> id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from Friendships\n" +
                     "where (id1=? and id2=?) ");

        ) {
            statement.setString(1, id.getLeft());
            statement.setString(2, id.getRight());
            statement.execute();


            return Optional.of(new Prietenie(LocalDateTime.now(), "", "",""));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Prietenie> findAllFriends(String idUtilizator) {
        List<Prietenie> friendships = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select friends.id1,friends.id2,friends.start_date,friends.status from users u\n" +
                     "inner join friendships friends\n" +
                     "on u.id=friends.id1 or u.id=friends.id2\n" +
                     "where u.id=?");

        ) {
            statement.setString(1, idUtilizator);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                String id1 = resultSet.getString("id1");
                String id2 = resultSet.getString("id2");
                Date date = resultSet.getDate("start_date");
                String status=resultSet.getString("status");
                Timestamp timestamp = new Timestamp(date.getTime());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                Prietenie friendship = new Prietenie(localDateTime, id1, id2,status);

                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {

            try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement("update friendships set status=? where id1 in (?,?) and id2 in (?,?)")
            ){
                statement.setString(1,entity.getStatus());
                statement.setString(2,entity.getId().getLeft());
                statement.setString(3,entity.getId().getRight());
                statement.setString(4,entity.getId().getLeft());
                statement.setString(5,entity.getId().getRight());
                statement.execute();
                return Optional.of(entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

    }
}
