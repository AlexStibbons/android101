package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;

import com.example.proto_korzo.database.DBUserMovie;
import com.example.proto_korzo.database.model.User;

public class CreateUser extends AsyncTask<String, Void, Integer> {

    private DBUserMovie database;
    private AsyncTaskManager.OnFindUser listener;
    private int userId;

    public CreateUser(DBUserMovie database, AsyncTaskManager.OnFindUser listener) {
        this.database = database;
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {

        Long id = database.getUserDao().addUser(new User(strings[0], strings[1]));
        userId = id.intValue();

        return userId;
    }

    @Override
    protected void onPostExecute(Integer userId) {
        super.onPostExecute(userId);

        if (listener != null) {
            listener.onUserFound(userId);
        }
    }
}
