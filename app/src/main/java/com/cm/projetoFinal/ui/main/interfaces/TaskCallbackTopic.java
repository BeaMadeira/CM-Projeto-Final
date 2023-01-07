package com.cm.projetoFinal.ui.main.interfaces;

import com.cm.projetoFinal.database.entities.Topic;

import java.util.List;

public interface TaskCallbackTopic {
    <T> void onCompletedGetAllTopics(List<T> result);

    <T> void onCompletedGetTopics(List<T> result);

    <T> void onCompletedGetTopic(T result);

    <T> void onCompletedInsertTopics(List<T> result);

    <T> void onCompletedDeleteTopicByName(T result);

    void onNullPointer();

    void onSQLiteConstraintException();
}
