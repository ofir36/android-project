package com.example.ishare.model;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class Model {
    final public static Model instance = new Model();

    ModelSql modelSql;
    //ModelFirebase modelFirebase;
    private Model() {
        modelSql = new ModelSql();
        //modelFirebase = new ModelFirebase();

//        for (int i =0; i<5; i++)
//        {
//            ModelSql.db.postDao().insertAll(new Post("id" + i, "Post number " + (i+1), "zamir", "image", 12345, 0));
//        }

    }




    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
//    public void getAllPosts(GetAllPostsListener listener) {
//        //modelFirebase.getAllStudents(listener);
//        List<Post> posts = modelSql.getAllPosts();
//        listener.onComplete(posts);
//    }

    public LiveData<List<Post>> getAllPosts() {
        final MutableLiveData<List<Post>> data = new
                MutableLiveData<>();

        PostAsyncDao.getAllPosts(new GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> posts) {
                data.setValue(posts);
            }
        });

        return data;
    }

//    public interface AddStudentListener{
//        void onComplete(boolean success);
//    }
//    public void addStudent(Student student, AddStudentListener listener) {
//        //TODO: fix async impl
//        modelFirebase.addStudent(student, listener);
//    }
//
//
//    public interface SaveImageListener{
//        void onComplete(String url);
//    }
//    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
//        modelFirebase.saveImage(imageBitmap, listener);
//    }
}
