package com.example.ishare.model;

import android.os.AsyncTask;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Dao
interface LastUpdateDao {

    @Query("select date from LastUpdate where tableName = :tableName")
    Date get(String tableName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(LastUpdate... lastUpdates);
}

public class LastUpdateAsyncDao {
    public static void getLastUpdate(final String tableName, final Model.GetLastUpdateListener listener)
    {
        new AsyncTask<String,String,Date>(){

            @Override
            protected Date doInBackground(String... strings) {
                Date lastUpdate = ModelSql.db.lastUpdateDao().get(tableName);

                if (lastUpdate == null) {
                    lastUpdate = new Date(1970,0,0);
                }

                return lastUpdate;
            }

            @Override
            protected void onPostExecute(Date lastUpdate) {
                super.onPostExecute(lastUpdate);
                listener.onComplete(lastUpdate);

            }
        }.execute();
    }

    public static void setLastUpdate(LastUpdate... lastUpdate) {
        new AsyncTask<LastUpdate,String,Void>(){
            @Override
            protected Void doInBackground(LastUpdate... lastUpdates) {
                ModelSql.db.lastUpdateDao().insertAll(lastUpdates);
                return null;
            }
        }.execute(lastUpdate);
    }
}
