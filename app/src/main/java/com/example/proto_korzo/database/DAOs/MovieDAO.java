package com.example.proto_korzo.database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proto_korzo.database.model.Movie;

import java.util.List;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM movies")
    public List<Movie> getAllMovies();

    @Query("SELECT * FROM movies WHERE id = :id")
    public Movie getMovieById(int id);

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :title || '%'")
    public List<Movie> getMoviesByTitleContains(String title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addMovie(Movie movie);

    @Delete
    public void deleteMovie(Movie movie);

    @Update
    public void updateMovie(Movie movie);

    @Insert
    public void addMovieList(Movie... movies);
}
