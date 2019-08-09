package com.example.ishare;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ishare.model.Model;
import com.example.ishare.model.Post;
import com.example.ishare.model.User;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<User> userDetails;
    private MutableLiveData<List<Post>> posts;

    public LiveData<User> getUserDetails() {
        if (userDetails == null)
        {
            userDetails = new MutableLiveData<>();
            loadUserDetails();
        }

        return userDetails;
    }

    private void loadUserDetails() {
        Model.instance.getUserDetails(new Model.GetUserDetailsListener() {
            @Override
            public void onComplete(User user) {
               userDetails.setValue(user);
            }
        });
    }

    public LiveData<List<Post>> getUserPosts()
    {
        if (posts == null)
        {
            posts = new MutableLiveData<>();
            loadUserPosts();
        }

        return posts;
    }

    public void loadUserPosts() {
        Model.instance.getUserPosts(new Model.GetAllPostsListener() {
            @Override
            public void onComplete(List<Post> data) {
                posts.setValue(data);
            }
        });
    }
}
