package com.example.ishare.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class LastUpdate {
    @PrimaryKey
    @NonNull
    String tableName;
    Date date;

    public LastUpdate() {

    }

    public LastUpdate(String _tableName, Date _date) {
        this.tableName = _tableName;
        this.date = _date;
    }

    @NonNull
    public String getTableName() {
        return tableName;
    }

    public void setTableName(@NonNull String tableName) {
        this.tableName = tableName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
