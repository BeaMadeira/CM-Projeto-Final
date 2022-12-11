package com.cm.challenge03.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cm.challenge03.database.entities.Temperature;

import java.util.List;

@Dao
public interface TemperatureDao {
    @Query("SELECT * FROM temperatures")
    List<Temperature> getTemperatures();

    @Query("SELECT * FROM temperatures ORDER BY date ASC")
    List<Temperature> getTemperaturesOrderByDateAsc();

    @Query("SELECT * FROM temperatures ORDER BY date DESC")
    List<Temperature> getTemperaturesOrderByDateDesc();

    @Query("SELECT * FROM temperatures WHERE uid IN (:temperaturesIds)")
    List<Temperature> getTemperaturesByIds(int... temperaturesIds);

    @Query("SELECT * FROM temperatures WHERE uid IN (:temperaturesIds) ORDER BY date DESC")
    List<Temperature> getTemperaturesByIdsOrderByDateDesc(int... temperaturesIds);

    @Query("SELECT * FROM temperatures WHERE uid = :uid")
    Temperature findTemperatureById(int uid);

    @Insert
    Long insertTemperature(Temperature temperature);

    @Insert
    List<Long> insertTemperatures(Temperature... temperatures);

    @Update
    Integer updateTemperature(Temperature temperature);

    @Update
    Integer updateTemperatures(Temperature... temperatures);

    @Delete
    Integer deleteTemperature(Temperature temperature);

    @Delete
    Integer deleteTemperatures(Temperature... temperatures);
}