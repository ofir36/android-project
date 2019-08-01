package com.example.ishare.model;

import java.util.Date;

public class Post {
    public String id;
    public String text;
    public String userId;
    public String image;
    public int isDeleted;
    public double lastUpdate;

    public Post(String _id, String _text, String _userId, String _image, double _lastUpdate, int _isDeleted)
    {
        id = _id;
        text = _text;
        userId = _userId;
        image = _image;
        lastUpdate = _lastUpdate;
        isDeleted = _isDeleted;
    }


//    var date:Date {
//        get {
//            return Date(timeIntervalSince1970: lastUpdate! / 1000);
//        }
//    }
}
