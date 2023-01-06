package com.cm.projetoFinal.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cm.projetoFinal.database.entities.Topic;

import java.util.List;

@Dao
public interface TopicDao {
    @Query("SELECT * FROM topics")
    List<Topic> getTopics();

    @Query("SELECT * FROM topics WHERE topic = :topic")
    Topic getTopic(String topic);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    List<Long> insertTopics(Topic... topics);

    @Delete
    Integer deleteTopics(Topic topic);
}