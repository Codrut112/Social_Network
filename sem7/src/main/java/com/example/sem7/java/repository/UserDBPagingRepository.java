package com.example.sem7.java.repository;

import com.example.sem7.java.utils.criptare.CaesarCipher;
import com.example.sem7.java.domain.Utilizator;
import com.example.sem7.java.paging.Page;
import com.example.sem7.java.paging.PageImplementation;
import com.example.sem7.java.paging.Pageable;
import com.example.sem7.java.paging.PagingRepository;
import com.example.sem7.java.validators.Validator;

import java.sql.*;
import java.util.HashSet;

public class UserDBPagingRepository extends DbRepository implements PagingRepository<String, Utilizator>
{


    public UserDBPagingRepository(String url, String username, String password, Validator validator) {
        super(url, username, password,validator);
    }

    @Override
    public Page<Utilizator> findAll(Pageable pageable) {
      /*  Stream<Utilizator> result = StreamSupport.stream(this.findAll().spliterator(), false)
                .skip(pageable.getPageNumber()  * pageable.getPageSize())
                .limit(pageable.getPageSize());
        return new PageImplementation<>(pageable, result);*/

        HashSet<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users limit ? offset ?");

        ) {
            statement.setInt(1,pageable.getPageSize());
            statement.setInt(2,(pageable.getPageNumber()-1)*pageable.getPageSize());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstname = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                String password=resultSet.getString("password");
                password=CaesarCipher.decrypt(password,shift);
                Utilizator utilizator = new Utilizator(firstname, lastname,id,password);
                utilizator.setId(id);
                users.add(utilizator);
            }
            return  new PageImplementation<Utilizator>(pageable,users.stream());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getSize() {
        try(Connection connection=DriverManager.getConnection(url,username,password);
        PreparedStatement statement=connection.prepareStatement("select count(*) as count from users")
        ){

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
