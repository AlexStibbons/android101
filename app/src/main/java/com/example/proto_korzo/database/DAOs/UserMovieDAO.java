package com.example.proto_korzo.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.proto_korzo.database.model.User;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.UserMovieJoin;

import java.util.List;

@Dao
public interface UserMovieDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addUserMovie(UserMovieJoin userMovie);

    @Delete
    public void deleteUserMovie(UserMovieJoin userMovie);

    @Query("DELETE FROM user_movie WHERE user_movie.movieId = :id")
    public void deleteByMovieId(int id);

    @Query("DELETE FROM user_movie WHERE user_movie.movieId = :movieId AND user_movie.userId = :userId")
    public void deleteByMovieAndUserId(int userId, int movieId);

    @Query("SELECT * FROM movies INNER JOIN user_movie ON movies.id = user_movie.movieId WHERE user_movie.userId = :id")
    public List<Movie> getMoviesByUserId(int id);


    @Query("SELECT * FROM users INNER JOIN user_movie ON users.id = user_movie.userId WHERE user_movie.movieId = :id")
    public List<User> getUsersByMovieId(int id);

    @Query("SELECT movies.id FROM movies INNER JOIN user_movie ON movies.id = user_movie.movieId WHERE user_movie.userId = :id")
    public List<Long> faveMoviesIdsByUserId(int id);

}
