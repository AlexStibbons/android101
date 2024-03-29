package com.example.proto_korzo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proto_korzo.database.DAOs.MovieDAO;
import com.example.proto_korzo.database.DAOs.UserDAO;
import com.example.proto_korzo.database.DAOs.UserMovieDAO;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.database.model.User;
import com.example.proto_korzo.database.model.UserMovieJoin;

@Database(entities = {Movie.class, User.class, UserMovieJoin.class},
        version = 3,
        exportSchema = false)
public abstract class DBUserMovie extends RoomDatabase {

    // name for reference
    public static final String DB_NAME = "appDb";

    private static /* volatile ?? */ DBUserMovie instance;

    // get all entities
    public abstract UserDAO getUserDao();
    public abstract MovieDAO getMovieDao();
    public abstract UserMovieDAO getUserMovieDao();

    // get a singleton
    public static synchronized DBUserMovie getInstance(Context context) {
        if (null == instance) {
            instance = buildDatabaseInstance(context);
        }
        return instance;
    }

    private static DBUserMovie buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                DBUserMovie.class,
                DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        instance = null;
    }



}
