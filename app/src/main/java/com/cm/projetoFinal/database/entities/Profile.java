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

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name="username")
    private String username;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "email")
    private String email;

    public Profile() {
        super();
    }

    @Ignore
    public Profile(String Name, String username, String email) {
        this.name=name;
        this.username=username;
        this.email=email;
    }

    @Ignore
    public Profile(Long uid,String Name, String username, String email) {
        this.uid = uid;
        this.name=name;
        this.username=username;
        this.email=email;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;
        Profile profile = (Profile) o;
        return Objects.equals(getUid(), profile.getUid()) && Objects.equals(getName(), profile.getName()) && Objects.equals(getUsername(), profile.getUsername()) && Objects.equals(getCreatedAt(), profile.getCreatedAt()) && Objects.equals(getEmail(), profile.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid(), getName(), getUsername(), getCreatedAt(), getEmail());
    }

    @Override
    public String toString() {
        return "Profile{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", createdAt=" + createdAt +
                ", email='" + email + '\'' +
                '}';
    }

}