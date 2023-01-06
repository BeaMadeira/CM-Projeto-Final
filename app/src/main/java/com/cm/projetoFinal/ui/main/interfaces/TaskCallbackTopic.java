package com.cm.projetoFinal.ui.main.interfaces;

import java.util.List;

public interface TaskCallbackTopic {
    <T> void onCompletedGetAllTopics(List<T> result);

    <T> void onCompletedInsertTopics(List<T> result);

    <T> void onCompletedDeleteTopicByName(T result);

    void onNullPointer();

    void onSQLiteConstraintException();
}
