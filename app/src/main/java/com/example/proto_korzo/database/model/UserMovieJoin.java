package com.example.proto_korzo.database.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity(tableName = "user_movie",
        primaryKeys = { "userId", "movieId" },
        foreignKeys = {
            @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId"),
            @ForeignKey(entity = Movie.class,
                        parentColumns = "id",
                        childColumns = "movieId")
        })
public class UserMovieJoin {

    private int userId;

    private int movieId;

    @Ignore
    public UserMovieJoin() {
    }

    public UserMovieJoin(int userId, int movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
