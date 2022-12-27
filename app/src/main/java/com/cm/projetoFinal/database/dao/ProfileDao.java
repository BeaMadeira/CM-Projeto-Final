package com.cm.projetoFinal.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cm.projetoFinal.database.entities.Profile;

import java.util.List;
import java.util.UUID;

@Dao
public interface ProfileDao {
    @Query("SELECT * FROM Profile LIMIT 1")
    Profile getProfile();

    @Query("SELECT * FROM Profile WHERE uid IN (:ProfileId)")
    Profile getProfileById(Long ProfileId);

    @Insert
    Long insertProfile(Profile profile);

    @Update
    Integer updateProfile(Profile profile);

    @Delete
    Integer deleteProfile(Profile profile);
}