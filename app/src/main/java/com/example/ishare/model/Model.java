package com.example.ishare.model;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class Model {
    final public static Model instance = new Model();

    ModelSql modelSql;
    ModelFirebase modelFirebase;

    private Model() {
        modelSql = new ModelSql();
        modelFirebase = new ModelFirebase();
    }

    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }

    public LiveData<List<Post>> getAllPosts() {
        final MutableLiveData<List<Post>> data = new
                MutableLiveData<>();

        modelFirebase.getAllPosts(new GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> posts) {
                data.setValue(posts);
            }
        });

//        PostAsyncDao.getAllPosts(new GetAllPostsListener() {
//            @Override
//            public void onComplete(List<Post> posts) {
//                data.setValue(posts);
//            }
//        });

        return data;
    }

    public interface AddPostListener{
        void onComplete(boolean success);
    }
    public void addPost(Post post, AddPostListener listener) {
        //TODO: fix async impl
        modelFirebase.addPost(post, listener);
    }


    public interface SaveImageListener{
        void onComplete(String url);
    }
    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, listener);
    }
}
