package com.cm.projetoFinal.database.repositories;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cm.projetoFinal.database.AppDatabase;
import com.cm.projetoFinal.database.dao.TopicDao;
import com.cm.projetoFinal.database.entities.Topic;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallbackTopic;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TopicRepository {
    private final TopicDao topicDao;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public TopicRepository(@NonNull Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        topicDao = appDatabase.topicDao();
    }

    public void getAllTopics(TaskCallbackTopic callback) {
        executor.execute(() -> {
            List<Topic> tempList = topicDao.getTopics();
            handler.post(() -> callback.onCompletedGetAllTopics(tempList));
        });
    }

    public void insertTopics(TaskCallbackTopic callback, Topic... topics) {
        executor.execute(() -> {
            try {
                topicDao.insertTopics(topics);
                List<Topic> tempList = topicDao.getTopics();
                handler.post(() -> callback.onCompletedInsertTopics(tempList));
            } catch (SQLiteConstraintException e) {
                Log.w("DEBUG", e.toString());
                handler.post(callback::onSQLiteConstraintException);
            }
        });
    }

    public void deleteTopic(TaskCallbackTopic callback, String topic) {
        executor.execute(() -> {
            Topic tempTopic = topicDao.getTopic(topic);
            try {
                topicDao.deleteTopics(tempTopic);
                List<Topic> tempList = topicDao.getTopics();
                handler.post(() -> callback.onCompletedDeleteTopicByName(tempList));
            } catch (NullPointerException e) {
                Log.w("DEBUG", e.toString());
                handler.post(callback::onNullPointer);
            }

        });
    }
}

