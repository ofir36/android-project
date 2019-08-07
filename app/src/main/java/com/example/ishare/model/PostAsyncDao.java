package com.example.ishare.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
interface PostDao {
    @Query("select * from Post order by lastUpdate desc")
    List<Post> getAll();

//    @Query("select * from Post")
//    LiveData<List<Post>> getAllPosts();

    @Query("select * from Post where id = :id")
    Post get(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Insert
    void insert(List<Post> posts);

    @Delete
    void delete(Post post);
}

public class PostAsyncDao {
    public static void getAllPosts(final Model.GetAllPostsListener listener) {
        new AsyncTask<String,String,List<Post>>(){

            @Override
            protected List<Post> doInBackground(String... strings) {
                List<Post> list = ModelSql.db.postDao().getAll();
                return list;
            }

            @Override
            protected void onPostExecute(List<Post> data) {
                super.onPostExecute(data);
                listener.onComplete(data);

            }
        }.execute();
    }

    public static void insertPosts(Post... posts) {
        new AsyncTask<Post,String,Void>(){
            @Override
            protected Void doInBackground(Post... posts) {
                ModelSql.db.postDao().insertAll(posts);
                return null;
            }
        }.execute(posts);
    }
}
