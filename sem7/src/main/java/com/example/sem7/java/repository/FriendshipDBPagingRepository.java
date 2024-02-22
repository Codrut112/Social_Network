package com.example.sem7.java.repository;

import com.example.sem7.java.domain.Prietenie;
import com.example.sem7.java.domain.Tuple;
import com.example.sem7.java.paging.*;
import com.example.sem7.java.repository.DbFriendshipRepository;
import com.example.sem7.java.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;

public class FriendshipDBPagingRepository extends DbFriendshipRepository implements FriendshipsPagingRepository<Tuple<String, String>, Prietenie> {


    public FriendshipDBPagingRepository(String url, String username, String password, Validator validator)  {
        super(url, username, password, validator);
    }

    @Override
    public Page<Prietenie> findAllFriends(String id, Pageable pageable) {
        HashSet<Prietenie> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select friends.id1,friends.id2,friends.start_date,friends.status  from  friendships friends  where (id1=? or id2=?) and friends.status='accepted' limit ? offset ?");


        ) {
            statement.setString(1, id);
            statement.setString(2, id);
            statement.setInt(3, pageable.getPageSize());
            statement.setInt(4, (pageable.getPageNumber()-1)*pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id1 = resultSet.getString("id1");
                String id2 = resultSet.getString("id2");
                Date sqlDate = resultSet.getDate("start_date");
                String status = resultSet.getString("status");
                Timestamp timestamp = new Timestamp(sqlDate.getTime());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                Prietenie friendship = new Prietenie(localDateTime, id1, id2, status);
                friendships.add(friendship);
            }
            return new PageImplementation<Prietenie>(pageable, friendships.stream());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Page<Prietenie> findAllFriendRequests(String id, Pageable pageable) {
        HashSet<Prietenie> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement statement = connection.prepareStatement("select friends.id1,friends.id2,friends.start_date,friends.status  from  friendships friends  where (id1=? or id2=?) limit ? offset ?");


        ) {
            statement.setString(1, id);
            statement.setString(2, id);
            statement.setInt(3, pageable.getPageSize());
            statement.setInt(4, (pageable.getPageNumber()-1)*pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id1 = resultSet.getString("id1");
                String id2 = resultSet.getString("id2");
                Date sqlDate = resultSet.getDate("start_date");
                String status = resultSet.getString("status");
                Timestamp timestamp = new Timestamp(sqlDate.getTime());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                Prietenie friendship = new Prietenie(localDateTime, id1, id2, status);
                friendships.add(friendship);
            }
            return new PageImplementation<Prietenie>(pageable, friendships.stream());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int getSize(String idUser) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select count(*) as count from friendships where (id1=? or id2=?) and status='accepted'")

        ) {
            statement.setString(1,idUser);
            statement.setString(2,idUser);
            ResultSet resultSet= statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("count");
            }
            else throw new SQLException("ceva nu a mers");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getAllSize(String idUser) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select count(*) as count from friendships where (id1=? or id2=?) ")

        ) {
            statement.setString(1,idUser);
            statement.setString(2,idUser);
            ResultSet resultSet= statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("count");
            }
            else throw new SQLException("ceva nu a mers");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
