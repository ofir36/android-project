package com.example.ishare.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ishare.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class ModelSql {
    MyHelper mDbHelper;

    public ModelSql() {
        mDbHelper = new MyHelper(MyApplication.getContext());
    }

    public List<Post> getAllPosts() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        LinkedList<Post> data = new LinkedList<Post>();
        Cursor cursor = db.query("posts", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("post_id");
            int textIndex = cursor.getColumnIndex("post_text");
            int dateIndex = cursor.getColumnIndex("post_date");
            int userIdIndex = cursor.getColumnIndex("user_id");
            int imageIndex = cursor.getColumnIndex("image_url");
            int isDeletedIndex = cursor.getColumnIndex("is_deleted");

            do {
                String id = cursor.getString(idIndex);
                String text = cursor.getString(textIndex);
                double date = cursor.getDouble(dateIndex);
                String user = cursor.getString(userIdIndex);
                String image = cursor.getString(imageIndex);
                int isDeleted = cursor.getInt(isDeletedIndex);

                Post post = new Post(id, text, user, image, date, isDeleted);
                data.add(post);
            } while (cursor.moveToNext());
        }
        return data;
    }

    public void addPost(Post post) {
        ContentValues values = new ContentValues();
        values.put("post_id", post.id);
        values.put("post_text", post.text);
        values.put("post_date", post.lastUpdate);
        values.put("user_id", post.userId);
        values.put("image_url", post.image);
        values.put("is_deleted", post.isDeleted);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert("posts", "post_id", values);
    }

    class MyHelper extends SQLiteOpenHelper {

        public MyHelper(Context context) {
            super(context, "database.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table posts(post_id text primary key, post_text text, post_date double, user_id text, image_url text, is_deleted int)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table posts");
            db.execSQL("create table posts(post_id text primary key, post_text text, post_date double, user_id text, image_url text, is_deleted int)");
        }
    }

}
