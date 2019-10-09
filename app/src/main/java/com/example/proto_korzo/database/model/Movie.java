package com.example.proto_korzo.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.ArrayList;

@Entity(tableName = "movies")
public class Movie {

    @PrimaryKey
    private long id;

    private String title;

    private String description;

    private String imgUrl;

    // somehow n:n
    @Ignore
    @ColumnInfo(name = "favourited_by")
    private List<User> users = new ArrayList<>();

    @Ignore
    public Movie() {
    }

    public Movie(long id, String title, String description, String imgUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
