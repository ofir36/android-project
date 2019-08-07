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

    public User(@NonNull String id, String name, String image, String about, Date lastUpdate) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.about = about;
        this.lastUpdate = lastUpdate;
    }

    public User(@NonNull String id, String name) {
        this(id, name, "", "", null);
    }

    public User() {
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
