package com.example.proto_korzo.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.ArrayList;

@Entity(tableName = "movies")
public class Movie {

    @PrimaryKey
    private int id;

    @SerializedName("imdb_id")
    private String imdb_id; //imdb_id

    private String title;

    private String overview;

    private String poster_path;

    // somehow n:n
    @Ignore
    @ColumnInfo(name = "favourited_by")
    private List<User> users = new ArrayList<>();

    @Ignore
    public Movie() {
    }

    public Movie(int id, String imdb_id, String title, String overview, String poster_path) {
        this.id = id;
        this.imdb_id = imdb_id;
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
