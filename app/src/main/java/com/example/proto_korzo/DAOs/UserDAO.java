package com.example.proto_korzo.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.proto_korzo.model.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM users")
    public List<User> findAllUsers();

    @Query("Select id FROM users WHERE email = :email")
    public long getUserIdByEmail(String email);

    // join?
    // public List<Movie> getUserFaves(long id);

    @Insert
    public void addUser(User user);

}
