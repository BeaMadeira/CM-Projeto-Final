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
import java.util.UUID;

@Entity(tableName = "Profile")
public class Profile implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private UUID uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name="username")
    private String username;

    @ColumnInfo(name = "foto")
    private Blob foto;

    public Profile() {
        super();
    }

    @Ignore
    public Profile(String Name, Blob foto) {
        this.uid = UUID.randomUUID();
        this.name=name;
        this.foto=foto;
    }

    @Ignore
    public Profile(UUID uid,String Name, Blob foto) {
        this.uid = uid;
        this.name=name;
        this.foto=foto;
    }



}