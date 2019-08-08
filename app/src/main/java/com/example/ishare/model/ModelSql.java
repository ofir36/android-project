package com.example.ishare.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ishare.MyApplication;

import java.util.LinkedList;
import java.util.List;

@Database(entities = {Post.class, LastUpdate.class, User.class}, version = 5, exportSchema = false)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract LastUpdateDao lastUpdateDao();
    public abstract UserDao userDao();
}

public class ModelSql {
    static public AppLocalDbRepository db = Room.databaseBuilder(MyApplication.getContext(),
            AppLocalDbRepository.class,
            "database.db")
            .fallbackToDestructiveMigration()
            .build();
}
