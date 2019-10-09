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
    public long addUserMovie(UserMovieJoin userMovie);

    @Delete
    public void deleteUserMovie(UserMovieJoin userMovie);

    // why not LEFT JOIN?

    // how is this select a movie?
   @Query("SELECT movies.id, movies.title, movies.description FROM movies INNER JOIN user_movie ON movies.id = user_movie.movieId WHERE user_movie.userId = :id")
    public List<Movie> getMoviesByUserId(long id);

   /* @Query("SELECT * FROM movies INNER JOIN user_movie ON movies.id = user_movie.movieId WHERE user_movie.userId = :id")
    public List<Movie> getMoviesByUserId(long id);*/

    @Query("SELECT * FROM users INNER JOIN user_movie ON users.id = user_movie.userId WHERE user_movie.movieId = :id")
    public List<User> getUsersByMovieId(long id);

    @Query("SELECT user_movie.movieId FROM user_movie INNER JOIN users ON users.id = user_movie.userId WHERE user_movie.userId = :id")
    public List<Long> getUsersMoviesIds(long id);

/*    @Query("SELECT movie.id FROM movies INNER JOIN user_movie ON users.id = users_movie.userId WHERE user_movie.userId = :id")
    public List<Long> getUsersMoviesIds(long id);*/

}
