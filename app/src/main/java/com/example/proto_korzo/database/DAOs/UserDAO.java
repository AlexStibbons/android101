package com.example.proto_korzo.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proto_korzo.database.model.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM users")
    public List<User> findAllUsers();

    @Query("SELECT users.id FROM users WHERE email = :email")
    public long getUserIdByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id")
    public User getUserById(long id);

    @Insert
    public long addUser(User user);

    @Update
    public void updateUser(User user);

    @Delete
    public void deleteUser(User user);

}
