package com.cm.challenge03.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "temperatures")
public class Temperature implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "value")
    private Double value;

    public Temperature() {
        super();
    }

    @Ignore
    public Temperature(Date date, Double value) {
        this.date = date;
        this.value = value;
    }

    @Ignore
    public Temperature(int uid, Date date, Double value) {
        this.uid = uid;
        this.date = date;
        this.value = value;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}