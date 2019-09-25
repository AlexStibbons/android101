package com.example.proto_korzo.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private long id;
    private String email;
    private String password;
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
