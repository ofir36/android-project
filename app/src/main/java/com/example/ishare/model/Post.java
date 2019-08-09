package com.example.ishare.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Post implements Serializable {
    @PrimaryKey
    @NonNull
    public String id;
    public String text;
    public String userId;
    public String image;
    public int isDeleted;
    @ServerTimestamp
    public Date lastUpdate;

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getUserId() {
        return userId;
    }

    public String getImage() {
        return image;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public Post() {

    }

    public Post(String _id, String _text, String _userId, String _image, Date _lastUpdate, int _isDeleted)
    {
        id = _id;
        text = _text;
        userId = _userId;
        image = _image;
        lastUpdate = _lastUpdate;
        isDeleted = _isDeleted;
    }

    public Post(String _id, String _text, String _userId, String _image)
    {
        this(_id, _text, _userId, _image, null, 0);
    }


//    var date:Date {
//        get {
//            return Date(timeIntervalSince1970: lastUpdate! / 1000);
//        }
//    }
}
