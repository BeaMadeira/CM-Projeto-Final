package com.cm.projetoFinal.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "topics")
public class Topic implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "topic")
    private String topic;

    public Topic() {
        super();
    }

    @Ignore
    public Topic(@NonNull String topic) {
        this.topic = topic;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    public void setTopic(@NonNull String topic) {
        this.topic = topic;
    }

    @NonNull
    @Override
    public String toString() {
        return "Topic{" + ", topic='" + topic + '\'' + '}';
    }
}