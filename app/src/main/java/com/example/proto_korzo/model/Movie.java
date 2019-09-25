package com.example.proto_korzo.model;

import java.util.List;
import java.util.ArrayList;

public class Movie {

    private long id; // check ROOM requirements
    private String title;
    private String rating; // ?
    private List<User> users = new ArrayList<>();

    public Movie() {
    }

    public Movie(String title, String rating) {
        this.title = title;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
