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

    private long userId;

    private long movieId;

    @Ignore
    public UserMovieJoin() {
    }

    public UserMovieJoin(long userId, long movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }
}
