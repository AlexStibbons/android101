package com.example.proto_korzo.DAOs;

import androidx.room.Insert;
import androidx.room.Query;

import com.example.proto_korzo.model.Movie;
import com.example.proto_korzo.model.User;
import com.example.proto_korzo.model.UserMovieJoin;

import java.util.List;

public interface UserMovieDAO {

    @Insert
    public void addUserMovie(UserMovieJoin userMovie);

    // why not LEFT JOIN?

    @Query("SELECT * FROM movies INNER JOIN user_movie ON movies.id = user_movie.movieId WHERE user_movie.userId = :id")
    public List<Movie> getMoviesByUserId(long id);

    @Query("SELECT * FROM users INNER JOIN user_movie ON users.id = user_movie.userId WHERE user_movie.movieId = :id")
    public List<User> getUsersByMovieId(long id);


}
