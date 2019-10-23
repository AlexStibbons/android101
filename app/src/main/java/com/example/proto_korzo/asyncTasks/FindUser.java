package com.example.proto_korzo.asyncTasks;

import android.os.AsyncTask;

import com.example.proto_korzo.database.DBUserMovie;

public class FindUser extends AsyncTask<String, Void, Integer> {

    private DBUserMovie database;
    private AsyncTaskManager.OnFindUser listener;
    private int userId;

    public FindUser(DBUserMovie database, AsyncTaskManager.OnFindUser listener){
        this.database = database;
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {

        Long id = database.getUserDao().getUserIdByEmail(strings[0]);
        userId = id.intValue();

        return userId;
    }

    @Override
    protected void onPostExecute(Integer userId) {
        super.onPostExecute(userId);

        if (listener != null) {
            if (userId > 0) {
                listener.onUserFound(userId);
            } else {
                listener.onUserNotFound();
            }
        }
    }
}
