package com.example.ishare.model;

import android.os.AsyncTask;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
interface UserDao {
    @Query("select * from User")
    List<User> getAll();

    @Query("select * from User where id = :id")
    User get(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);
}

public class UserAsyncDao {
    interface GetUserListener {
        void onComplete(User user);
    }
    public static void getUser(final String id, final GetUserListener listener) {
        new AsyncTask<String,String,User>(){

            @Override
            protected User doInBackground(String... strings) {
                User user = ModelSql.db.userDao().get(id);
                return user;
            }

            @Override
            protected void onPostExecute(User data) {
                super.onPostExecute(data);
                listener.onComplete(data);

            }
        }.execute();
    }

    interface InsertUserListener {
        void onComplete();
    }

    public static void insertUser(final User user, final InsertUserListener listener) {
        new AsyncTask<User,String,Void>(){
            @Override
            protected Void doInBackground(User... users) {
                ModelSql.db.userDao().insert(users[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void avoid) {
                super.onPostExecute(avoid);
                listener.onComplete();
            }
        }.execute(user);
    }
}
