package com.example.ishare;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ishare.model.Model;
import com.example.ishare.model.User;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<User> userDetails;

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
}
