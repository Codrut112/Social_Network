package com.example.sem7.java.repository;

import com.example.sem7.java.domain.Message;

import com.example.sem7.java.validators.Validator;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class DbMessageRepository implements MessageRepository<UUID, Message> {
    private String url, username, password;
    private Validator validator;

    public DbMessageRepository(String url, String username, String password, Validator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Message> findOne(UUID id) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages where id=?")
        ) {
            statement.setObject(1, id, Types.OTHER);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                String fromUser = resultSet.getString("from_user");
                String toUser = resultSet.getString("to_user");
                Date date = resultSet.getDate("date_sent");
                Timestamp timestamp = new Timestamp(date.getTime());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                String context = resultSet.getString("message");
                Message message = new Message(fromUser, Collections.singletonList(toUser), localDateTime, context);
                return Optional.of(message);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        HashSet<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages")
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String fromUser = resultSet.getString("from_user");
                String toUser = resultSet.getString("to_user");
                Date date = resultSet.getDate("date_sent");
                Timestamp timestamp = new Timestamp(date.getTime());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                String context = resultSet.getString("message");
                UUID respond = UUID.fromString(resultSet.getString("respond"));

                UUID id = UUID.fromString(resultSet.getString("id"));
                Message message = new Message(fromUser, Collections.singletonList(toUser), localDateTime, context);

                message.setReply(respond);
                message.setId(id);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> save(Message entity) {
        validator.validate(entity);
        UUID id = entity.getId();
        String from = entity.getFrom();
        List<String> to = entity.getTo();
        String message = entity.getMessage();
        UUID respond = entity.getReply();
        LocalDateTime date = entity.getDate();
        if (respond == null) respond = UUID.fromString("00000000-0000-0000-0000-000000000000");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into messages Values (?,?,?,?,?,?)");
        ) {
            for (String user : to) {
                statement.setObject(1, UUID.randomUUID());
                statement.setObject(2, from);
                statement.setObject(3, user);
                statement.setObject(4, date);
                statement.setObject(5, message);
                statement.setObject(6, respond);
                statement.execute();
            }
            return Optional.of(entity);
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Message> delete(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }

    private int getOffsetMessage(String id1, String id2) {


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select count(*) as count from messages\n" +
                     "where to_user in (?,?) and from_user in (?,?)")) {
            statement.setObject(1, id1);
            statement.setObject(2, id2);
            statement.setObject(3, id2);
            statement.setObject(4, id1);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                int count = resultSet.getInt("count");
                if (count > 10) return count - 10;
                else return 0;

            } else throw new SQLException("ceva nu a mers");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Message> findAllFMessages(String id1, String id2) {

        List<Message> messages = new ArrayList<>();
        int offset=getOffsetMessage(id1,id2);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages\n" +
                     "where to_user in (?,?) and from_user in (?,?) offset ?")) {
            statement.setObject(1, id1);
            statement.setObject(2, id2);
            statement.setObject(3, id2);
            statement.setObject(4, id1);
            statement.setObject(5,offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                String fromUser = resultSet.getString("from_user");
                String toUser = resultSet.getString("to_user");
                Date date = resultSet.getDate("date_sent");
                Timestamp timestamp = new Timestamp(date.getTime());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                String context = resultSet.getString("message");
                UUID respond = UUID.fromString(resultSet.getString("respond"));
                UUID id = UUID.fromString(resultSet.getString("id"));

                Message message = new Message(fromUser, Collections.singletonList(toUser), localDateTime, context);
                message.setReply(respond);
                message.setId(id);

                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
