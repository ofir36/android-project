package com.example.ishare.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
interface UserDao {
    @Query("select * from User")
    List<User> getAll();

    @Query("select * from User where id = :id")
    User get(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);
}

public class UserAsyncDao {

}
