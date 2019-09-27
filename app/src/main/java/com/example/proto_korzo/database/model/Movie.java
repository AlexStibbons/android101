package com.example.proto_korzo.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.ArrayList;

@Entity(tableName = "movies")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;

    private String description;

    // somehow n:n
    @Ignore
    @ColumnInfo(name = "favourited_by")
    private List<User> users = new ArrayList<>();

    @Ignore
    public Movie() {
    }

    public Movie(String title, String description) {
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
