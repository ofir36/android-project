package com.example.ishare.model;

import android.graphics.Bitmap;

import java.util.List;

public class Model {
    final public static Model instance = new Model();

    ModelSql modelSql;
    //ModelFirebase modelFirebase;
    private Model() {
        modelSql = new ModelSql();
        //modelFirebase = new ModelFirebase();
//
//        for (int i =0; i<5; i++)
//        {
//            modelSql.addPost(new Post("id" + i, "Post number " + (i+1), "zamir", "image", 12345, 0));
//        }

    }




    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public void getAllPosts(GetAllPostsListener listener) {
        //modelFirebase.getAllStudents(listener);
        List<Post> posts = modelSql.getAllPosts();
        listener.onComplete(posts);
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
