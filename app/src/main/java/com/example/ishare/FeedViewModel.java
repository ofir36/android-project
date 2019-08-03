package com.example.ishare;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ishare.model.Model;
import com.example.ishare.model.Post;

import java.util.List;

public class FeedViewModel extends ViewModel {
    private LiveData<List<Post>> data = Model.instance.getAllPosts();

    public LiveData<List<Post>> getData() {
        return data;
    }
}
