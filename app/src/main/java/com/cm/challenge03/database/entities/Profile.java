package com.cm.challenge03.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "Profiles")
public class Profile implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "foto")
    private Blob foto;

    public Profile() {
        super();
    }

    @Ignore
    public Profile(String Name, Blob foto) {
        this.name=name;
        this.foto=foto;
    }

    @Ignore
    public Profile(int uid,String Name, Blob foto) {
        this.uid = uid;
        this.name=name;
        this.foto=foto;
    }



}