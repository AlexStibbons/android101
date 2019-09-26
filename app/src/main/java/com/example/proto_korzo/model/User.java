package com.example.proto_korzo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    private long id;

    private String email;

    private String password;

    // somehoe n:n
    @ColumnInfo(name = "user_favourites")
    private List<Movie> favourites = new ArrayList<>();

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Movie> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Movie> favourites) {
        this.favourites = favourites;
    }
}
