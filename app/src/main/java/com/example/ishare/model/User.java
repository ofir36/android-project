package com.example.ishare.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String image;
    public String about;
    @ServerTimestamp
    public Date lastUpdate;

    public User() {
    }

    public User(@NonNull String _id, String _name) {
        this(_id, _name, "", "", null);
    }

    public User(@NonNull String _id, String _name, String _image, String _about, Date _lastUpdate) {
        this.id = _id;
        this.name = _name;
        this.image = _image;
        this.about = _about;
        this.lastUpdate = _lastUpdate;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
