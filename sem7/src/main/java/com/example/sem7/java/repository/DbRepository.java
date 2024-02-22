package com.example.sem7.java.repository;

import com.example.sem7.java.utils.criptare.CaesarCipher;
import com.example.sem7.java.domain.Utilizator;
import com.example.sem7.java.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;

public class DbRepository implements Repository<String, Utilizator> {

    protected String url, username, password;
    private Validator validator;
    protected int shift=3;
    public DbRepository(String url, String username, String password, Validator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Utilizator> findOne(String id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users where id=?")
        ) {

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstname = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                String userPassword=resultSet.getString("password");
                userPassword= CaesarCipher.decrypt(userPassword,shift);
                Utilizator user=new Utilizator(firstname,lastname, id,userPassword );
                user.setId(id);

                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Utilizator> findAll() {
        HashSet<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstname = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                String userPassword=resultSet.getString("password");
                userPassword= CaesarCipher.decrypt(userPassword,shift);
                Utilizator utilizator = new Utilizator(firstname, lastname,id,userPassword );

                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> save(Utilizator utilizator) {
        validator.validate(utilizator);
        String id=utilizator.getId();
        String firstname=utilizator.getFirstName();
        String lastname=utilizator.getLastName();
        String userPassword=utilizator.getPassword();
        userPassword= CaesarCipher.encrypt(userPassword,shift);
       try(Connection connection=DriverManager.getConnection(url,username,password);
       PreparedStatement statement=connection.prepareStatement("insert into users Values (?,?,?,?)");
       ) {
           statement.setString(1,firstname);
           statement.setString(2,lastname);
           statement.setString(3,id);
           statement.setString(4,userPassword);
            statement.execute();
            return Optional.of(utilizator);
       } catch (SQLException e) {

           throw new RuntimeException(e);
       }

    }

    @Override
    public Optional<Utilizator> delete(String l) {

        try(Connection connection=DriverManager.getConnection(url,username,password);
         PreparedStatement statement=connection.prepareStatement("delete from Users\n" +
                 "where id=?");

        ) {
            statement.setString(1,l);
            statement.execute();
            return Optional.of(new Utilizator("","","","" ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> update(Utilizator utilizator) {
        String id=utilizator.getId();
        String firstname=utilizator.getFirstName();
        String lastname=utilizator.getLastName();
        try(Connection connection=DriverManager.getConnection(url,username,password);
        PreparedStatement statement=connection.prepareStatement("Update users "+
                "set first_name=?,last_name=? "+
                "where id=?;")
        ) {

            statement.setString(1,firstname);
            statement.setString(2,lastname);
            statement.setString(3,id);
            statement.execute();
            return Optional.of(utilizator);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
