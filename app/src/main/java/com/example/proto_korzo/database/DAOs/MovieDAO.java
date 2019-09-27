package com.example.proto_korzo.database.DAOs;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proto_korzo.database.model.Movie;

import java.util.List;

public interface MovieDAO {

    @Query("SELECT * FROM movies")
    public List<Movie> getAllMovies();

    @Query("SELECT * FROM movies WHERE id = :id")
    public Movie getMovieById(long id);

    @Query("SELECT * FROM movies WHERE title LIKE '%:title&'")
    public List<Movie> getMoviesByTitleContains(String title);

    @Insert
    public void addMovie(Movie movie);

    @Delete
    public void deleteMovie(Movie movie);

    @Update
    public Movie updateMovie(Movie movie);
}
