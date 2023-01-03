package com.cm.projetoFinal.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "Profile")
public class Profile implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long uid;

    @ColumnInfo(name="username")
    private String username;

    public Profile() {
        super();
    }

    @Ignore
    public Profile( String username) {
        this.username=username;
    }

    @Ignore
    public Profile(Long uid,String Name, String username, String email) {
        this.uid = uid;
        this.username=username;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}